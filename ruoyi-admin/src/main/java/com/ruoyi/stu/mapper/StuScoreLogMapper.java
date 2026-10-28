package com.ruoyi.stu.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.stu.domain.StuScoreLog;

/**
 * 积分流水 数据层
 *
 * @author campus
 */
public interface StuScoreLogMapper
{
    /**
     * 查询积分流水列表
     */
    public List<StuScoreLog> selectStuScoreLogList(StuScoreLog stuScoreLog);

    /**
     * 新增积分流水
     */
    public int insertStuScoreLog(StuScoreLog stuScoreLog);

    /**
     * 删除积分流水
     */
    public int deleteStuScoreLogById(Long logId);

    /**
     * 批量删除积分流水
     */
    public int deleteStuScoreLogByIds(Long[] logIds);

    /**
     * 按学生批量删除积分流水
     */
    public int deleteStuScoreLogByStudentIds(Long[] studentIds);

    /**
     * 查询最近积分变动记录
     */
    public List<Map<String, Object>> selectRecentScoreLogs(@Param("limit") int limit);
}
