package com.dongyimai.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.dongyimai.entity.PageResult;
import com.dongyimai.entity.Result;
import com.dongyimai.pojo.TbBrand;
import com.dongyimai.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(Integer page,Integer rows){
        if (page==0||page==null){
            page=1;
        }
        return brandService.findPage(page, rows);
    }

    @RequestMapping("/add")
    public Result save(@RequestBody TbBrand tbBrand){
        try {
            brandService.save(tbBrand);
            return new Result(true,"新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"新增失败");
        }
    }

    @RequestMapping("/findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }

    //修改
    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand tbBrand){
        try {
            brandService.update(tbBrand);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }
    //删除
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        System.out.println("ids : " + ids);
        try {
            brandService.dele(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

    @RequestMapping("/search")
    public PageResult search(int page, int rows, @RequestBody TbBrand brand){
        System.out.println(brand);
        PageResult search = brandService.search(page, rows, brand);
        return search;
    }

    @RequestMapping("/selectOptionList")
    public List<TbBrand> selectOptionList(){

        return brandService.selectOptionList();
    }


}
