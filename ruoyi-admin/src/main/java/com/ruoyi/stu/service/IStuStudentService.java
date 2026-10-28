package com.ruoyi.stu.service;

import java.util.List;
import com.ruoyi.stu.domain.StuScoreLog;
import com.ruoyi.stu.domain.StuStudent;
import com.ruoyi.stu.domain.StuTransferLog;

/**
 * 学生档案 服务层
 *
 * @author campus
 */
public interface IStuStudentService
{
    /**
     * 查询学生档案列表
     */
    public List<StuStudent> selectStuStudentList(StuStudent stuStudent);

    /**
     * 根据学生ID查询学生档案
     */
    public StuStudent selectStuStudentById(Long studentId);

    /**
     * 校验学生编号是否唯一
     */
    public boolean checkStudentNoUnique(StuStudent stuStudent);

    /**
     * 新增学生档案
     */
    public int insertStuStudent(StuStudent stuStudent);

    /**
     * 修改学生档案
     */
    public int updateStuStudent(StuStudent stuStudent);

    /**
     * 批量删除学生档案（同时清理其积分流水与调班记录）
     */
    public int deleteStuStudentByIds(Long[] studentIds);

    /**
     * 学生加/减分：写入积分流水并同步当前积分
     *
     * @param scoreLog 积分变动信息（含学生ID、变动分值、项目、备注）
     * @return 结果
     */
    public int changeScore(StuScoreLog scoreLog);

    /**
     * 学生调班：更新学生所属班级并写入调班记录
     *
     * @param transferLog 调班信息（含学生ID、新班级、原因）
     * @return 结果
     */
    public int transferClass(StuTransferLog transferLog);

    /**
     * Excel 批量导入学生档案
     *
     * @param studentList 学生数据列表
     * @param updateSupport 是否更新已存在的学生
     * @param operName 操作人
     * @return 导入结果提示
     */
    public String importStudent(List<StuStudent> studentList, boolean updateSupport, String operName);
}
