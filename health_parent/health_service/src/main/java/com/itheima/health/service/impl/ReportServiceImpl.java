package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 获取运营统计数据
     *
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws HealthException {
        //创建一个map用来存储返回的数据
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //创建一个现在的时间
        Date today = new Date();
        //创建时间格式转换API
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        //使用工具类获取星期一
        String monday = sdf.format(DateUtils.getFirstDayOfWeek(today));
        //使用工具类获取星期天
        String sunday = sdf.format(DateUtils.getLastDayOfWeek(today));
        //使用工具类获取1号(每个月的1号)
        String firstDayOfThisMonth = sdf.format(DateUtils.getFirstDayOfThisMonth());
        //获取本月的最后一天
        String lastDayOfThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());

        //======会员数量======
        //格式化当前时间
        String reportDate = sdf.format(today);
        //todayNewMember 今日会员增加的数量
        int todayNewMember = memberDao.findMemberCountByDate(reportDate);
        //totalMember 总会员数量
        int totalMember = memberDao.findMemberTotalCount();
        //thisWeekNewMember 本周新增的会员数量
        int thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //thisMonthNewMember 本月新增的会员数量
        int thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);

        //=======订单统计=======
        //todayOrderNumber 今日预约数
        int todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        //todayVisitsNumber 今日到诊数
        int todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        //thisWeekOrderNumber 本周预约数
        int thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday, sunday);
        //thisWeekVisitsNumber 本周到诊数
        int thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //thisMonthOrderNumber 本月预约数
        int thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth, lastDayOfThisMonth);
        //thisMonthVisitsNumber 本月到诊数
        int thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);

        //========热门套餐=======
        //调用dao查询热门套餐
        List<Map<String, Object>> hotSetmeal = orderDao.findHotSetmeal();

        //将参数都添加进map集合中返回
        resultMap.put("reportDate",reportDate);
        resultMap.put("todayNewMember",todayNewMember);
        resultMap.put("totalMember",totalMember);
        resultMap.put("thisWeekNewMember",thisWeekNewMember);
        resultMap.put("thisMonthNewMember",thisMonthNewMember);
        resultMap.put("todayOrderNumber",todayOrderNumber);
        resultMap.put("todayVisitsNumber",todayVisitsNumber);
        resultMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        resultMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        resultMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        resultMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        resultMap.put("hotSetmeal",hotSetmeal);

        return resultMap;
    }
}
