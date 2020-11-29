package com.dongyimai.sellergoods.service.impl;
import java.util.List;

import com.dongyimai.group.Specification;
import com.dongyimai.mapper.TbSpecificationOptionMapper;
import com.dongyimai.pojo.TbSpecificationOption;
import com.dongyimai.pojo.TbSpecificationOptionExample;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.dongyimai.mapper.TbSpecificationMapper;
import com.dongyimai.pojo.TbSpecification;
import com.dongyimai.pojo.TbSpecificationExample;
import com.dongyimai.pojo.TbSpecificationExample.Criteria;
import com.dongyimai.sellergoods.service.SpecificationService;

import com.dongyimai.entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper tbSpecificationMapper;

	@Autowired
	private TbSpecificationOptionMapper tbSpecificationOptionMapper;


	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return tbSpecificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbSpecification> page=   (Page<TbSpecification>) tbSpecificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {


		tbSpecificationMapper.insert(specification.getTbSpecification());

		for (TbSpecificationOption option : specification.getTbSpecificationOptionList()){
			option.setSpecId(specification.getTbSpecification().getId());
			tbSpecificationOptionMapper.insert(option);
		}
	}


	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		//1.修改规格表
		tbSpecificationMapper.updateByPrimaryKey(specification.getTbSpecification());
		//2.修改规格选项表
		//2.1先删除旧的数据
		TbSpecificationOptionExample optionExample = new TbSpecificationOptionExample();
		optionExample.createCriteria().andSpecIdEqualTo(specification.getTbSpecification().getId());

		tbSpecificationOptionMapper.deleteByExample(optionExample);
		//2.2在新增数据
		for (TbSpecificationOption option :specification.getTbSpecificationOptionList()) {
			option.setSpecId(specification.getTbSpecification().getId());

			tbSpecificationOptionMapper.insertSelective(option);
		}


	}

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){

		Specification specification = new Specification();
		//根据id主键查询规格表
		TbSpecification tbSpecification = tbSpecificationMapper.selectByPrimaryKey(id);
		//根据外键查询TBSpecificationOption规格选项表
		TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();

		criteria.andSpecIdEqualTo(id);
		List<TbSpecificationOption> optionLists = tbSpecificationOptionMapper.selectByExample(tbSpecificationOptionExample);
		//4.封装结果
		specification.setTbSpecification(tbSpecification);
		specification.setTbSpecificationOptionList(optionLists);


		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			//先删除规格选项表
				TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
				tbSpecificationOptionExample.createCriteria().andSpecIdEqualTo(id);
				tbSpecificationOptionMapper.deleteByExample(tbSpecificationOptionExample);
			tbSpecificationMapper.deleteByPrimaryKey(id);
		}

	}


		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();

		if(specification!=null){
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
		}

		Page<TbSpecification> page= (Page<TbSpecification>)tbSpecificationMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
    public List<TbSpecification> selectSpecificationList() {

		return tbSpecificationMapper.selectSpecificationList();
    }

}
