package com.ruoyi.stu.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.stu.mapper.StuClassMapper;
import com.ruoyi.stu.mapper.StuScoreLogMapper;
import com.ruoyi.stu.mapper.StuStudentMapper;
import com.ruoyi.stu.mapper.StuTransferLogMapper;
import com.ruoyi.stu.service.IStuDashboardService;

/**
 * 首页概览 服务层实现
 *
 * @author campus
 */
@Service
public class StuDashboardServiceImpl implements IStuDashboardService
{
    @Autowired
    private StuStudentMapper stuStudentMapper;

    @Autowired
    private StuClassMapper stuClassMapper;

    @Autowired
    private StuScoreLogMapper stuScoreLogMapper;

    @Autowired
    private StuTransferLogMapper stuTransferLogMapper;

    @Override
    public Map<String, Object> overview()
    {
        Map<String, Object> data = new HashMap<>();

        // 学生总数（在校）
        data.put("studentTotal", stuStudentMapper.countAllStudent());

        // 班级数量
        List<Map<String, Object>> classCounts = stuStudentMapper.countGroupByClass();
        data.put("classTotal", classCounts.size());

        // 各班人数
        data.put("classCounts", classCounts);

        // 积分分布
        List<Map<String, Object>> distList = stuStudentMapper.countScoreDistribution();
        Map<String, Object> dist = distList.isEmpty() ? new HashMap<>() : distList.get(0);
        data.put("scoreDistribution", dist);

        // 最近积分动态
        data.put("recentScores", stuScoreLogMapper.selectRecentScoreLogs(8));

        // 最近调班动态
        data.put("recentTransfers", stuTransferLogMapper.selectRecentTransfers(5));

        return data;
    }
}
