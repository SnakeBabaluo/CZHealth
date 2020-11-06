package com.itheima.health.controller;


import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取用户名
     * @return
     */
    @GetMapping("/getUsername")
    public Result getUsername(){
        //从权限中获取
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //打印一下用户名
        System.out.println("登陆的用户名:" + user.getUsername());
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
    }

    /**
     * 登陆成功
     * @return
     *//*
    @RequestMapping("/loginSuccess")
    public Result loginSuccess(){
        //返回响应
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

    *//**
     * 登陆失败
     * @return
     *//*
    @RequestMapping("/loginFail")
    public Result loginFail(){
        //返回失败响应
        return new Result(false, "用户名或密码错误!!!");
    }*/
}
