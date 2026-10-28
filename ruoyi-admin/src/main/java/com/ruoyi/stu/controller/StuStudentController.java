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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.stu.domain.StuScoreLog;
import com.ruoyi.stu.domain.StuStudent;
import com.ruoyi.stu.domain.StuTransferLog;
import com.ruoyi.stu.service.IStuScoreLogService;
import com.ruoyi.stu.service.IStuStudentService;
import com.ruoyi.stu.service.IStuTransferLogService;

/**
 * 学生档案
 *
 * @author campus
 */
@RestController
@RequestMapping("/stu/student")
public class StuStudentController extends BaseController
{
    @Autowired
    private IStuStudentService stuStudentService;

    @Autowired
    private IStuScoreLogService stuScoreLogService;

    @Autowired
    private IStuTransferLogService stuTransferLogService;

    /**
     * 查询学生档案列表
     */
    @PreAuthorize("@ss.hasPermi('stu:student:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuStudent stuStudent)
    {
        startPage();
        List<StuStudent> list = stuStudentService.selectStuStudentList(stuStudent);
        return getDataTable(list);
    }

    /**
     * 查询全部学生（下拉选择用）
     */
    @GetMapping("/allList")
    public AjaxResult allList(StuStudent stuStudent)
    {
        return success(stuStudentService.selectStuStudentList(stuStudent));
    }

    /**
     * 导出学生档案
     */
    @Log(title = "学生档案", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('stu:student:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, StuStudent stuStudent)
    {
        List<StuStudent> list = stuStudentService.selectStuStudentList(stuStudent);
        ExcelUtil<StuStudent> util = new ExcelUtil<StuStudent>(StuStudent.class);
        util.exportExcel(response, list, "学生档案");
    }

    /**
     * 下载导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<StuStudent> util = new ExcelUtil<StuStudent>(StuStudent.class);
        util.importTemplateExcel(response, "学生档案");
    }

    /**
     * 导入学生档案
     */
    @Log(title = "学生档案", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('stu:student:import')")
    @PostMapping("/importData")
    public AjaxResult importData(@RequestParam("file") MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<StuStudent> util = new ExcelUtil<StuStudent>(StuStudent.class);
        List<StuStudent> list = util.importExcel(file.getInputStream());
        String message = stuStudentService.importStudent(list, updateSupport, getUsername());
        return success(message);
    }

    /**
     * 获取学生详细信息
     */
    @PreAuthorize("@ss.hasPermi('stu:student:query')")
    @GetMapping(value = "/{studentId}")
    public AjaxResult getInfo(@PathVariable Long studentId)
    {
        return success(stuStudentService.selectStuStudentById(studentId));
    }

    /**
     * 新增学生
     */
    @PreAuthorize("@ss.hasPermi('stu:student:add')")
    @Log(title = "学生档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody StuStudent stuStudent)
    {
        if (!stuStudentService.checkStudentNoUnique(stuStudent))
        {
            return error("新增学生失败，学生编号已存在");
        }
        stuStudent.setCreateBy(getUsername());
        return toAjax(stuStudentService.insertStuStudent(stuStudent));
    }

    /**
     * 修改学生
     */
    @PreAuthorize("@ss.hasPermi('stu:student:edit')")
    @Log(title = "学生档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody StuStudent stuStudent)
    {
        if (!stuStudentService.checkStudentNoUnique(stuStudent))
        {
            return error("修改学生失败，学生编号已存在");
        }
        stuStudent.setUpdateBy(getUsername());
        return toAjax(stuStudentService.updateStuStudent(stuStudent));
    }

    /**
     * 删除学生
     */
    @PreAuthorize("@ss.hasPermi('stu:student:remove')")
    @Log(title = "学生档案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{studentIds}")
    public AjaxResult remove(@PathVariable Long[] studentIds)
    {
        return toAjax(stuStudentService.deleteStuStudentByIds(studentIds));
    }

    /**
     * 学生加/减分
     */
    @PreAuthorize("@ss.hasPermi('stu:student:score')")
    @Log(title = "学生积分", businessType = BusinessType.UPDATE)
    @PostMapping("/score")
    public AjaxResult score(@RequestBody StuScoreLog scoreLog)
    {
        scoreLog.setCreateBy(getUsername());
        return toAjax(stuStudentService.changeScore(scoreLog));
    }

    /**
     * 学生调班
     */
    @PreAuthorize("@ss.hasPermi('stu:student:transfer')")
    @Log(title = "学生调班", businessType = BusinessType.UPDATE)
    @PostMapping("/transfer")
    public AjaxResult transfer(@RequestBody StuTransferLog transferLog)
    {
        transferLog.setCreateBy(getUsername());
        return toAjax(stuStudentService.transferClass(transferLog));
    }

    /**
     * 查询某学生的积分流水
     */
    @PreAuthorize("@ss.hasPermi('stu:student:query')")
    @GetMapping("/scoreLog/{studentId}")
    public AjaxResult scoreLog(@PathVariable Long studentId)
    {
        StuScoreLog query = new StuScoreLog();
        query.setStudentId(studentId);
        return success(stuScoreLogService.selectStuScoreLogList(query));
    }

    /**
     * 查询某学生的调班记录
     */
    @PreAuthorize("@ss.hasPermi('stu:student:query')")
    @GetMapping("/transferLog/{studentId}")
    public AjaxResult transferLog(@PathVariable Long studentId)
    {
        StuTransferLog query = new StuTransferLog();
        query.setStudentId(studentId);
        return success(stuTransferLogService.selectStuTransferLogList(query));
    }
}
