package com.dongyimai.page.service.impl;

import com.dongyimai.mapper.TbGoodsDescMapper;
import com.dongyimai.mapper.TbGoodsMapper;
import com.dongyimai.mapper.TbItemCatMapper;
import com.dongyimai.mapper.TbItemMapper;
import com.dongyimai.page.service.ItemPageService;
import com.dongyimai.pojo.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemPageServiceImpl implements ItemPageService {

    private String pagedir = "E:/Item/";
    @Autowired
    private TbGoodsMapper tbGoodsMapper;
    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Autowired
    private TbItemMapper tbItemMapper;

    @Override
    public boolean genItemHtml(Long goodsId) {

        try {
            //1.查询数据库
            TbGoods goods = tbGoodsMapper.selectByPrimaryKey(goodsId);
            TbGoodsDesc goodsDesc = tbGoodsDescMapper.selectByPrimaryKey(goodsId);

            //2.获取摸版对象
            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //3.创建map容器
            Map modelMap = new HashMap();
            modelMap.put("goods",goods);
            modelMap.put("goodsDesc",goodsDesc);

            //查询分类
            TbItemCat tbItemCat1 = tbItemCatMapper.selectByPrimaryKey(goods.getCategory1Id());
            TbItemCat tbItemCat2 = tbItemCatMapper.selectByPrimaryKey(goods.getCategory2Id());
            TbItemCat tbItemCat3 = tbItemCatMapper.selectByPrimaryKey(goods.getCategory3Id());
            modelMap.put("itemCat1",tbItemCat1.getName());
            modelMap.put("itemCat2",tbItemCat2.getName());
            modelMap.put("itemCat3",tbItemCat3.getName());

            //对规格产品查询
            TbItemExample tbItemExample = new TbItemExample();

            TbItemExample.Criteria criteria = tbItemExample.createCriteria();
            criteria.andStatusEqualTo("1");
            criteria.andGoodsIdEqualTo(goodsId);
            tbItemExample.setOrderByClause("is_default desc");

            List<TbItem> itemList = tbItemMapper.selectByExample(tbItemExample);

            modelMap.put("itemList",itemList);

            //4.创建write流   E:/item/123897493721.html
            FileWriter fileWriter = new FileWriter(pagedir+goodsId+".html");
            //5.摸版输出
            template.process(modelMap,fileWriter);

            //6.关流
            fileWriter.close();
            System.out.println("生成成功");
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void deleteItemHtml(Long[] ids) {
        for (Long id : ids) {
            new File(pagedir+id+".html").delete();
        }
    }
}
