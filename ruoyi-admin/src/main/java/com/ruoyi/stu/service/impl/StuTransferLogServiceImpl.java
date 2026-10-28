package com.ruoyi.stu.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.stu.domain.StuTransferLog;
import com.ruoyi.stu.mapper.StuTransferLogMapper;
import com.ruoyi.stu.service.IStuTransferLogService;

/**
 * 调班记录 服务层实现
 *
 * @author campus
 */
@Service
public class StuTransferLogServiceImpl implements IStuTransferLogService
{
    @Autowired
    private StuTransferLogMapper stuTransferLogMapper;

    @Override
    public List<StuTransferLog> selectStuTransferLogList(StuTransferLog stuTransferLog)
    {
        return stuTransferLogMapper.selectStuTransferLogList(stuTransferLog);
    }

    @Override
    public int deleteStuTransferLogByIds(Long[] transferIds)
    {
        return stuTransferLogMapper.deleteStuTransferLogByIds(transferIds);
    }
}
