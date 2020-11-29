package com.dongyimai.cart.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.dongyimai.cart.service.CartService;
import com.dongyimai.entity.Cart;
import com.dongyimai.entity.Result;
import com.dongyimai.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference(timeout = 6000)
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){

        //1、取cookie
        String cartListString = CookieUtil.getCookieValue(request,"cartList","utf-8");
        //2、如果cartListString为空 则创建个新的数组
        if(cartListString == null || cartListString.length() <=0){
            cartListString="[]";
        }
        //3、json转换
        List<Cart> cartList = JSON.parseArray(cartListString,Cart.class);



        return cartList;
    }

    @RequestMapping("/addGoodsToCart")
    public Result addGoodsToCart(Long itemId, Integer num){
        try {
            //1、查询购物车
            List<Cart> cartList = findCartList();

            //2、调用添加购物车的方法
            cartList = cartService.addGoodsToCart(cartList,itemId,num);

            //3、存入到cookie中
            CookieUtil.setCookie(request,response,"cartList",JSON.toJSONString(cartList),3600*24,"utf-8");

            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }

    }
}
