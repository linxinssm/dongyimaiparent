package com.dongyimai.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dongyimai.entity.PageResult;
import com.dongyimai.mapper.TbSpecificationOptionMapper;
import com.dongyimai.mapper.TbTypeTemplateMapper;
import com.dongyimai.pojo.TbSpecificationOption;
import com.dongyimai.pojo.TbSpecificationOptionExample;
import com.dongyimai.pojo.TbTypeTemplate;
import com.dongyimai.pojo.TbTypeTemplateExample;
import com.dongyimai.sellergoods.service.TypeTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;


@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;

	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;

	@Autowired
	private RedisTemplate redisTemplate;



	@Override
	public List<Map> findSpecList(Long id) {
		//1.查询模板中对应的规格
		TbTypeTemplate tbTypeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
		//2、取模版中的规格 由于是一个json格式的字符串 所以需要转型
		//[{"id":27,"text":"网络","options":[{},{}]},{"id":32,"text":"机身内存"},"options":[{},{}]]
		List<Map> specList = JSON.parseArray(tbTypeTemplate.getSpecIds(),Map.class);
		//3.遍历规格,查询规格对应的属性
		if (specList != null ){
			for (Map map : specList) {
				TbSpecificationOptionExample example = new TbSpecificationOptionExample();
				TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
				criteria.andSpecIdEqualTo(new Long((Integer)map.get("id")));

				List<TbSpecificationOption> options = specificationOptionMapper.selectByExample(example);
				map.put("options",options);
			}
		}


		return specList;
	}

	/**
	 * 查询全部
	 */
	@Override
	public List<TbTypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		Page page1 = PageHelper.startPage(pageNum, pageSize);
		Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}


	/**
	 * 增加
	 */
	@Override
	public void add(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.insert(typeTemplate);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbTypeTemplate typeTemplate){
		typeTemplateMapper.updateByPrimaryKey(typeTemplate);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbTypeTemplate findOne(Long id){
		return typeTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			typeTemplateMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbTypeTemplateExample example=new TbTypeTemplateExample();
		TbTypeTemplateExample.Criteria criteria = example.createCriteria();
		
		if(typeTemplate!=null){			
						if(typeTemplate.getName()!=null && typeTemplate.getName().length()>0){
				criteria.andNameLike("%"+typeTemplate.getName()+"%");
			}			if(typeTemplate.getSpecIds()!=null && typeTemplate.getSpecIds().length()>0){
				criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
			}			if(typeTemplate.getBrandIds()!=null && typeTemplate.getBrandIds().length()>0){
				criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
			}			if(typeTemplate.getCustomAttributeItems()!=null && typeTemplate.getCustomAttributeItems().length()>0){
				criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
			}	
		}

		Page<TbTypeTemplate> page= (Page<TbTypeTemplate>)typeTemplateMapper.selectByExample(example);

			//更新品牌和规格的缓存(redis)
			saveToRedis();

		return new PageResult(page.getTotal(), page.getResult());
	}

	//将品牌列表和规格列表存到redis中
	public void saveToRedis(){

		//1.查询所有的模板
		List<TbTypeTemplate> typeList = findAll();
		//2.循环遍历
		for (TbTypeTemplate tbTypeTemplate : typeList) {
			//2.1将品牌放到redis中
			List<Map> brandList = JSON.parseArray(tbTypeTemplate.getBrandIds(), Map.class);

			redisTemplate.boundHashOps("brandList").put(tbTypeTemplate.getId(),brandList);

			//2.2将规格放入redis中
			List<Map> specList = findSpecList(tbTypeTemplate.getId());
			redisTemplate.boundHashOps("specList").put(tbTypeTemplate.getId(),specList);
		}

		System.out.println("品牌和列表缓存更新::1");

	}

	
}
