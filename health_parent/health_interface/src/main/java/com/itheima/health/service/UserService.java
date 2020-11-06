package com.itheima.health.service;

import com.itheima.health.pojo.User;

public interface UserService {
    /**
     * 根据登陆用户名称查询用户权限信息
     * @param username
     * @return
     */
    User findByUsername(String username);
}
