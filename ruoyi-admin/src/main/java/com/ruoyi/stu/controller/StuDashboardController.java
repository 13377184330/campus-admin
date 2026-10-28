package com.ruoyi.stu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.stu.service.IStuDashboardService;

/**
 * 首页概览
 *
 * @author campus
 */
@RestController
@RequestMapping("/stu/dashboard")
public class StuDashboardController extends BaseController
{
    @Autowired
    private IStuDashboardService stuDashboardService;

    /**
     * 概览统计
     */
    @GetMapping("/overview")
    public AjaxResult overview()
    {
        return success(stuDashboardService.overview());
    }
}
