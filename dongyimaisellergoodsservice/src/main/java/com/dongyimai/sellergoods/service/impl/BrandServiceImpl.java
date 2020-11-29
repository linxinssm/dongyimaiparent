package com.dongyimai.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dongyimai.entity.PageResult;
import com.dongyimai.mapper.TbBrandMapper;
import com.dongyimai.pojo.TbBrand;
import com.dongyimai.pojo.TbBrandExample;
import com.dongyimai.sellergoods.service.BrandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
   private TbBrandMapper tbBrandMapper;

    @Override
    public List<TbBrand> findAll() {

        return tbBrandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {

         PageHelper.startPage(pageNum, pageSize);

        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);


        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void save(TbBrand tbBrand) {
        tbBrandMapper.insert(tbBrand);
    }

    @Override//修改数据回显
    public TbBrand findOne(Long id) {
        return tbBrandMapper.selectByPrimaryKey(id);
    }
    //修改
    @Override
    public void update(TbBrand tbBrand) {

        tbBrandMapper.updateByPrimaryKey(tbBrand);
    }

    //删除
    @Override
    public void dele(Long[] ids) {
        for (Long id : ids) {
           tbBrandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public PageResult search(int page, int rows, TbBrand brand) {
        //1、分页属性
        PageHelper.startPage(page,rows);

        //2、拼接条件查询
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();

        if(brand != null){
            if(brand.getName() != null && brand.getName().length() > 0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if(brand.getFirstChar()!=null && !"".equals(brand.getFirstChar())){
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }

        //3、查询
        Page<TbBrand> pageResult = (Page<TbBrand>)tbBrandMapper.selectByExample(example);

        return new PageResult(pageResult.getTotal(),pageResult.getResult());
    }


    @Override
    public List<TbBrand> selectOptionList(){
        return tbBrandMapper.selectOptionList();
    }
}
