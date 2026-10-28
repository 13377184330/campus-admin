package com.ruoyi.stu.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.stu.domain.StuClass;
import com.ruoyi.stu.service.IStuClassService;

/**
 * 班级信息
 *
 * @author campus
 */
@RestController
@RequestMapping("/stu/class")
public class StuClassController extends BaseController
{
    @Autowired
    private IStuClassService stuClassService;

    /**
     * 查询班级列表
     */
    @PreAuthorize("@ss.hasPermi('stu:class:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuClass stuClass)
    {
        startPage();
        List<StuClass> list = stuClassService.selectStuClassList(stuClass);
        return getDataTable(list);
    }

    /**
     * 查询全部班级（下拉选择用）
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect(StuClass stuClass)
    {
        return success(stuClassService.selectStuClassList(stuClass));
    }

    /**
     * 导出班级列表
     */
    @Log(title = "班级管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('stu:class:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, StuClass stuClass)
    {
        List<StuClass> list = stuClassService.selectStuClassList(stuClass);
        ExcelUtil<StuClass> util = new ExcelUtil<StuClass>(StuClass.class);
        util.exportExcel(response, list, "班级信息");
    }

    /**
     * 获取班级详细信息
     */
    @PreAuthorize("@ss.hasPermi('stu:class:query')")
    @GetMapping(value = "/{classId}")
    public AjaxResult getInfo(@PathVariable Long classId)
    {
        return success(stuClassService.selectStuClassById(classId));
    }

    /**
     * 新增班级
     */
    @PreAuthorize("@ss.hasPermi('stu:class:add')")
    @Log(title = "班级管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody StuClass stuClass)
    {
        if (!stuClassService.checkClassNameUnique(stuClass))
        {
            return error("新增班级失败，班级名称已存在");
        }
        stuClass.setCreateBy(getUsername());
        return toAjax(stuClassService.insertStuClass(stuClass));
    }

    /**
     * 修改班级
     */
    @PreAuthorize("@ss.hasPermi('stu:class:edit')")
    @Log(title = "班级管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody StuClass stuClass)
    {
        if (!stuClassService.checkClassNameUnique(stuClass))
        {
            return error("修改班级失败，班级名称已存在");
        }
        stuClass.setUpdateBy(getUsername());
        return toAjax(stuClassService.updateStuClass(stuClass));
    }

    /**
     * 删除班级
     */
    @PreAuthorize("@ss.hasPermi('stu:class:remove')")
    @Log(title = "班级管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{classIds}")
    public AjaxResult remove(@PathVariable Long[] classIds)
    {
        return toAjax(stuClassService.deleteStuClassByIds(classIds));
    }
}
