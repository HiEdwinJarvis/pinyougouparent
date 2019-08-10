package com.jarvis.pinyougou.user.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @CreateDate: 2019/8/10 12:31
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/8/10 12:31
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {



    @RequestMapping("name")
    public Map showName(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Map map = new HashMap();

        map.put("loginName",name);

        return map;

    }
}
