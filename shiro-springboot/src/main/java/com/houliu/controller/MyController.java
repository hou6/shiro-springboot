package com.houliu.controller;

import com.houliu.pojo.User;
import com.houliu.service.impl.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author houliu
 * @create 2020-02-14 16:59
 */
@Controller
public class MyController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping({"/", "/index"})
    public String toIndex(Model model) {
        model.addAttribute("msg", "hello,shiro");
        return "index";
    }

    @RequestMapping("/user/add")
    public String add() {
        return "/user/add";
    }

    @RequestMapping("/user/update")
    public String update() {
        return "/user/update";
    }

    //跳转到登录页面
    @RequestMapping("toLogin")
    @RequiresGuest
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/noAuthc")
    public String noAuthc() {
        return "noAuthc";
    }

    /**
     * 登录逻辑
     */
    @RequestMapping("/login")
    public String login(String name,String pwd, Model model) {
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(name,pwd);
        try {
            token.setRememberMe(true);   //设置记住我
            subject.login(token);
            String perms = userService.queryPermsByName(token.getUsername());
            if (perms != null){
                return "index";
            }else {
                return "noAuthc";
            }
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名错误");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误");
            return "login";
        } catch (Exception e) {
            model.addAttribute("msg", "用户名或密码错误");
            return "login";
        }
    }

    @RequestMapping("logout")
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    /**
     * 跳转到注册用户
     */
    @RequestMapping("regist")
    public String insertUser() {
        System.out.println("goto regist page");
        return "regist";
    }

    /**
     * 注册用户逻辑
     * @param user
     * @return
     */
    @RequestMapping("insertUser")
    public String insertUser(User user) {
        userService.insertUser(user);
        return "login";
    }

}
