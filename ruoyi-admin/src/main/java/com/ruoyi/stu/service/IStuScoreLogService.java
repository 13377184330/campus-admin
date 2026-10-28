package com.ruoyi.stu.service;

import java.util.List;
import com.ruoyi.stu.domain.StuScoreLog;

/**
 * 积分流水 服务层
 *
 * @author campus
 */
public interface IStuScoreLogService
{
    /**
     * 查询积分流水列表
     */
    public List<StuScoreLog> selectStuScoreLogList(StuScoreLog stuScoreLog);

    /**
     * 批量删除积分流水
     */
    public int deleteStuScoreLogByIds(Long[] logIds);
}
