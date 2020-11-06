package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Description：用户数据维护
 * User：JuZhao
 * Date：2020-11-06
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    /**
     * 查询所有角色
     * @return
     */
    @PostMapping("/findAll")
    public Result findAll() {
        //调用服务findAll方法，返回Set<Role>
        Set<Role> roleSet = roleService.findAll();
        //封装结果集（flag，message，data）并返回
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, roleSet);
    }
}
