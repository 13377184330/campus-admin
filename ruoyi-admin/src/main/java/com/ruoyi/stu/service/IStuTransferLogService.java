package com.ruoyi.stu.service;

import java.util.List;
import com.ruoyi.stu.domain.StuTransferLog;

/**
 * 调班记录 服务层
 *
 * @author campus
 */
public interface IStuTransferLogService
{
    /**
     * 查询调班记录列表
     */
    public List<StuTransferLog> selectStuTransferLogList(StuTransferLog stuTransferLog);

    /**
     * 批量删除调班记录
     */
    public int deleteStuTransferLogByIds(Long[] transferIds);
}
