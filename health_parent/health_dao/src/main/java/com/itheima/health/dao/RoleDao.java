package com.itheima.health.dao;

import com.itheima.health.pojo.Role;

import java.util.Set;

/**
 * Description：用户数据维护
 * User：JuZhao
 * Date：2020-11-06
 */
public interface RoleDao {

    /**
     * 查询所有角色
     * @return
     */
    Set<Role> findAll();
}
