package com.ruoyi.stu.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.stu.domain.StuTransferLog;
import com.ruoyi.stu.service.IStuTransferLogService;

/**
 * 调班记录
 *
 * @author campus
 */
@RestController
@RequestMapping("/stu/transfer")
public class StuTransferLogController extends BaseController
{
    @Autowired
    private IStuTransferLogService stuTransferLogService;

    /**
     * 查询调班记录列表
     */
    @PreAuthorize("@ss.hasPermi('stu:transfer:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuTransferLog stuTransferLog)
    {
        startPage();
        List<StuTransferLog> list = stuTransferLogService.selectStuTransferLogList(stuTransferLog);
        return getDataTable(list);
    }

    /**
     * 导出调班记录
     */
    @Log(title = "调班记录", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('stu:transfer:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, StuTransferLog stuTransferLog)
    {
        List<StuTransferLog> list = stuTransferLogService.selectStuTransferLogList(stuTransferLog);
        ExcelUtil<StuTransferLog> util = new ExcelUtil<StuTransferLog>(StuTransferLog.class);
        util.exportExcel(response, list, "调班记录");
    }

    /**
     * 删除调班记录
     */
    @PreAuthorize("@ss.hasPermi('stu:transfer:remove')")
    @Log(title = "调班记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{transferIds}")
    public AjaxResult remove(@PathVariable Long[] transferIds)
    {
        return toAjax(stuTransferLogService.deleteStuTransferLogByIds(transferIds));
    }
}
