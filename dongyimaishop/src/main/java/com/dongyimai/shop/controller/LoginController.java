package com.dongyimai.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController

public class LoginController {

    @RequestMapping("/showName")
    public Map  showName (){
        //使用security登录
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println("当前登录人:"+name+"LoginController:showName()");

        HashMap<String, String> map = new HashMap<>();
        map.put("loginName",name);

        return map;
    }


}
