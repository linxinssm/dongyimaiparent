package com.dongyimai.cart.service;

import com.dongyimai.entity.Cart;

import java.util.List;

public interface CartService {

    public List<Cart> addGoodsToCart(List<Cart> cartList,Long itemId,Integer num);
}
