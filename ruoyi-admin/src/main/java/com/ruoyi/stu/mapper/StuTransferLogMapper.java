package com.ruoyi.stu.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.stu.domain.StuTransferLog;

/**
 * 调班记录 数据层
 *
 * @author campus
 */
public interface StuTransferLogMapper
{
    /**
     * 查询调班记录列表
     */
    public List<StuTransferLog> selectStuTransferLogList(StuTransferLog stuTransferLog);

    /**
     * 新增调班记录
     */
    public int insertStuTransferLog(StuTransferLog stuTransferLog);

    /**
     * 删除调班记录
     */
    public int deleteStuTransferLogById(Long transferId);

    /**
     * 批量删除调班记录
     */
    public int deleteStuTransferLogByIds(Long[] transferIds);

    /**
     * 按学生批量删除调班记录
     */
    public int deleteStuTransferLogByStudentIds(Long[] studentIds);

    /**
     * 查询最近调班记录
     */
    public List<Map<String, Object>> selectRecentTransfers(@Param("limit") int limit);
}
