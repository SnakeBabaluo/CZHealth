package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

public interface MemberDao {
    /**
     * 通过手机号码查询会员是否存在
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员信息
     * @param member
     */
    void add(Member member);

    /**
     * 查询每个月的会员数据
     * @param date
     * @return
     */
    Integer findMemberCountBeforeDate(String date);

    /**
     * 今日会员增加的数量
     * @param reportDate
     * @return
     */
    int findMemberCountByDate(String reportDate);

    /**
     * 总会员数量
     * @return
     */
    int findMemberTotalCount();

    /**
     * 本周新增的会员数量
     * @param monday
     * @return
     */
    int findMemberCountAfterDate(String monday);
}
