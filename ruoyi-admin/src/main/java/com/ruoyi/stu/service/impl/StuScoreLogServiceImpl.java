package com.ruoyi.stu.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.stu.domain.StuScoreLog;
import com.ruoyi.stu.mapper.StuScoreLogMapper;
import com.ruoyi.stu.service.IStuScoreLogService;

/**
 * 积分流水 服务层实现
 *
 * @author campus
 */
@Service
public class StuScoreLogServiceImpl implements IStuScoreLogService
{
    @Autowired
    private StuScoreLogMapper stuScoreLogMapper;

    @Override
    public List<StuScoreLog> selectStuScoreLogList(StuScoreLog stuScoreLog)
    {
        return stuScoreLogMapper.selectStuScoreLogList(stuScoreLog);
    }

    @Override
    public int deleteStuScoreLogByIds(Long[] logIds)
    {
        return stuScoreLogMapper.deleteStuScoreLogByIds(logIds);
    }
}
