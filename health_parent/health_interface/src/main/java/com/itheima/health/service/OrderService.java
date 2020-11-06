package com.itheima.health.service;

import com.itheima.health.pojo.Order;

import java.util.Map;

public interface OrderService {
    /**
     * 提交预约信息
     * @param orderInfo
     * @return
     */
    Order submit(Map<String, String> orderInfo);

    /**
     * 根据订单id查询订单信息
     * @param id
     * @return
     */
    Map<String, Object> findById4Detail(int id);
}
