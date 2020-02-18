package com.houliu.controller;

import org.apache.shiro.authz.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author houliu
 * @create 2020-02-18 14:45
 */

/**
 * 测试使用注解，此处不涉及业务
 */
@Controller
public class OrderController {

    @RequiresAuthentication   //必须登录才能访问
    @RequestMapping("add")
    public String add(){
        return "user/add";
    }

    @RequiresAuthentication
    @RequiresPermissions(value = {"user:update","user:query"},logical = Logical.AND)  //必须有这多个权限
    @RequestMapping("update")
    public String udpate(){
        return "index";
    }

    @RequiresAuthentication
    @RequiresRoles(value = {"超级管理员","仓库管理员"},logical = Logical.OR)  //必须有这多个角色的一个或多个
    @RequestMapping("delete")
    public String delete(){
        return "index";
    }

    @RequiresUser  //必须记住我
    @RequestMapping("query")
    public String query(){
        return "index";
    }

}
