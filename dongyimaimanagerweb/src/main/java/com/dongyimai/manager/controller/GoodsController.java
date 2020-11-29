package com.dongyimai.manager.controller;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.dongyimai.group.Goods;
import com.dongyimai.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.dongyimai.pojo.TbGoods;
import com.dongyimai.sellergoods.service.GoodsService;

import com.dongyimai.entity.PageResult;
import com.dongyimai.entity.Result;


import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;



@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination queueSolrDestination;

	@Autowired
	private Destination queueSolrDeleteDestination;

	@Autowired
	private  Destination topicPageDestination;

	@Autowired
	private  Destination topicPageDeleteDestination;
	

	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	

	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	

	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	

	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {

			//可选逻辑
			//判断当前登录用户是否为商品添加的用户 如果是 具有修改权限 若不是 非法操作
			//取当前登录人
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			//查询商品添加的用户
			Goods goods1 = goodsService.findOne(goods.getTbGoods().getId());
			if(!goods1.getTbGoods().getSellerId().equals(name) || !goods.getTbGoods().getSellerId().equals(name)){
				return new Result(false,"非法操作");
			}

			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败:controller");
		}
	}

	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}

	@RequestMapping("/delete")
	public Result delete(final Long [] ids){
		try {
			//itemSearchService.deleteByGoodsIds(ids);
			goodsService.delete(ids);

			//删除solr
			jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					//内部类用到外部类的成员必须+final
					return session.createObjectMessage(ids);
				}
			});

			//删除页面
			jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});


			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	

	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){

        return goodsService.findPage(goods, page, rows);
	}


	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids,String status){

		try {

			if (status.equals("1")){
				//查询回审核通过后的
				List<TbItem> itemList =  goodsService.findItemsByIdAndStatus(ids,status);
				System.out.println(itemList);

				if (itemList.size()>0){
				//	itemSearchService.importList(itemList);

					//将solr需要更新的数据,存入mq中
					//把集合转成JSON格式的字符串
					final String jsonString = JSON.toJSONString(itemList);
					jmsTemplate.send(queueSolrDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return  session.createTextMessage(jsonString);
						}
					});

					for (final Long id : ids) {

						//生成商品详情页
						jmsTemplate.send(topicPageDestination, new MessageCreator() {
							@Override
							public Message createMessage(Session session) throws JMSException {
								return session.createTextMessage(id+"");
							}
						});

					}



				}else {
					System.out.println("没有详细数据");
				}
			}
						//静态页生成
			for(Long goodsId:ids){
				//itemPageService.genItemHtml(goodsId);
			}

			goodsService.updateStatus(ids,status);
			return new Result(true,"审核成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"审核失败");
		}
	}

	//freemarker静态原型
	@RequestMapping("/html")
	public void genItemHtml (Long id) {
		//itemPageService.genItemHtml(id);


	}

}
