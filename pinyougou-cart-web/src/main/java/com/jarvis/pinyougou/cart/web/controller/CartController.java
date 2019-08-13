package com.jarvis.pinyougou.cart.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.jarvis.entity.Result;
import com.jarvis.pinyougou.cart.service.CartService;
import com.jarvis.pinyougou.common.CookieUtil;
import com.jarvis.pinyougou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description:
 * @CreateDate: 2019/8/11 22:20
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/8/11 22:20
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference(timeout=6000)
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /**
     * 获取购物车列表
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
        System.out.println("进来1");
        //得到登陆人账号,判断当前是否有人登陆
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println(username);



            String cartListString = CookieUtil.getCookieValue(request,"cartList","UTF-8");

            if(cartListString==null || cartListString.equals("")){
                cartListString="[]";

            }

            List<Cart> cartList_cookie = JSON.parseArray(cartListString,Cart.class);

         if(username.equals("anonymousUser")){ //未登录从cookie中返回结果
            return cartList_cookie;
        }else{

            //一登录

            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);

            if(cartList_cookie.size()>0){//cookie中有数据
                //合并购物车
                cartList_redis = cartService.mergeCartList(cartList_cookie, cartList_redis);

                //清除本地的cookie
                CookieUtil.deleteCookie(request,response,"cartList");

                //将合并后的数据存入redis

                cartService.saveCartListToRedis(username,cartList_redis);



            }


            return cartList_redis;

        }


    }

    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins="http://localhost:9104/pinyougou_page_web_war_exploded")
    public Result addGoodsIoCartList(Long itemId,Integer num){

        /**
         * cors 解决js跨域请求
         *
         */
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:9104/pinyougou_page_web_war_exploded/");
        //response.setHeader("Access-Control-Allow-Credentials", "true");



        System.out.println("进来2");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println(username);


        try{
            List<Cart> cartList = findCartList();//获取购物车列表
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);

            if(username.equals("anonymousUser")){
                //如果未登录就存到cookie
                CookieUtil.setCookie(request,response,"cartList",JSON.toJSONString(cartList),3600*24,"UTF-8");
                System.out.println("数据存到了cookie中");
            }else{
                //已登录的数据添加到redis中

                cartService.saveCartListToRedis(username,cartList);

            }


            return new Result(true,"添加成功");
        }catch (Exception e){

            return new Result(false,"添加失败");
        }

    }

}
