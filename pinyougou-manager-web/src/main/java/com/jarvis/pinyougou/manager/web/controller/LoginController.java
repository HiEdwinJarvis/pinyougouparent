package com.jarvis.pinyougou.manager.web.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:登录后在index.html页面上显示登录人
 * @CreateDate: 2019/6/12 16:57
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/6/12 16:57
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/name")
    public Map name(){
        //获取登录人name，从spring security的上下文中
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = new HashMap();
        map.put("loginName",name);
        return map;

    }
}
