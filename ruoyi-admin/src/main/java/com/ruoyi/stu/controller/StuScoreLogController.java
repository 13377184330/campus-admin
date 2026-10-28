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
import com.ruoyi.stu.domain.StuScoreLog;
import com.ruoyi.stu.service.IStuScoreLogService;

/**
 * 积分流水
 *
 * @author campus
 */
@RestController
@RequestMapping("/stu/score")
public class StuScoreLogController extends BaseController
{
    @Autowired
    private IStuScoreLogService stuScoreLogService;

    /**
     * 查询积分流水列表
     */
    @PreAuthorize("@ss.hasPermi('stu:score:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuScoreLog stuScoreLog)
    {
        startPage();
        List<StuScoreLog> list = stuScoreLogService.selectStuScoreLogList(stuScoreLog);
        return getDataTable(list);
    }

    /**
     * 导出积分流水
     */
    @Log(title = "积分流水", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('stu:score:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, StuScoreLog stuScoreLog)
    {
        List<StuScoreLog> list = stuScoreLogService.selectStuScoreLogList(stuScoreLog);
        ExcelUtil<StuScoreLog> util = new ExcelUtil<StuScoreLog>(StuScoreLog.class);
        util.exportExcel(response, list, "积分流水");
    }

    /**
     * 删除积分流水
     */
    @PreAuthorize("@ss.hasPermi('stu:score:remove')")
    @Log(title = "积分流水", businessType = BusinessType.DELETE)
    @DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable Long[] logIds)
    {
        return toAjax(stuScoreLogService.deleteStuScoreLogByIds(logIds));
    }
}
