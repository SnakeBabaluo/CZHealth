package com.itheima.health.service;

import com.itheima.health.exception.HealthException;

import java.util.Map;

public interface ReportService {
    /**
     * 获取运营统计数据
     * @return
     */
    Map<String, Object> getBusinessReportData() throws HealthException;
}
