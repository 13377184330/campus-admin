package com.ruoyi.stu.service;

import java.util.Map;

/**
 * 首页概览 服务层
 *
 * @author campus
 */
public interface IStuDashboardService
{
    /**
     * 概览统计数据：学生总数、班级数、各班人数、积分分布、最近动态
     */
    public Map<String, Object> overview();
}
