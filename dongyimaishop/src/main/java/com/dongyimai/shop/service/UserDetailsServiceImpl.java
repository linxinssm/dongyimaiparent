package com.dongyimai.shop.service;

import com.dongyimai.pojo.TbSeller;
import com.dongyimai.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {

    //set注入
    private SellerService sellerService;

    public void setSellerService(SellerService sellerService){

        this.sellerService = sellerService;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //1权限集合
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();

        authList.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //2.认证
        TbSeller seller = sellerService.findOne(username);
        if (seller != null && "1".equals(seller.getStatus())){

                return new User(username,seller.getPassword(),authList);
        }else {
            return null;
        }

    }

}
