package com.jarvis.pinyougou.cart.service;

import com.jarvis.pinyougou.pojogroup.Cart;

import java.util.List;

/**
 * @Description:
 * @CreateDate: 2019/8/11 21:35
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/8/11 21:35
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface CartService {


    /**
     * 添加商品到购物车
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);

    /**
     * 从redis查询购物车
     * @param username
     * @return
     */
    public List<Cart> findCartListFromRedis(String username);


    /**
     * 将购物车保存到redis
     * @param username
     * @param cart
     */
    public void saveCartListToRedis(String username,List<Cart> cart);


    /**
     *
     * 合并购物车
     * @param cartList1
     * @param cartList2
     * @return
     */
    public List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);
}
