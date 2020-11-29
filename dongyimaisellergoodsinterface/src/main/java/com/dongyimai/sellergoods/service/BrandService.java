package com.dongyimai.sellergoods.service;


import com.dongyimai.entity.PageResult;
import com.dongyimai.pojo.TbBrand;

import java.util.List;

public interface BrandService {

    public List<TbBrand> findAll();

    public PageResult findPage(int pageNum,int pageSize);

    public void save(TbBrand tbBrand);

    public TbBrand findOne(Long id);

    void update(TbBrand tbBrand);

    void dele(Long[] ids);

    PageResult search(int page, int rows, TbBrand brand);

    List<TbBrand> selectOptionList();

}
