package com.ruoyi.stu.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.stu.domain.StuStudent;

/**
 * 学生档案 数据层
 *
 * @author campus
 */
public interface StuStudentMapper
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
     * 新增学生档案
     */
    public int insertStuStudent(StuStudent stuStudent);

    /**
     * 修改学生档案
     */
    public int updateStuStudent(StuStudent stuStudent);

    /**
     * 更新学生所在班级（调班使用）
     */
    public int updateStudentClass(@Param("studentId") Long studentId, @Param("classId") Long classId,
            @Param("updateBy") String updateBy);

    /**
     * 更新学生当前积分（加减分使用）
     */
    public int updateStudentScore(@Param("studentId") Long studentId, @Param("totalScore") Integer totalScore,
            @Param("updateBy") String updateBy);

    /**
     * 删除学生档案
     */
    public int deleteStuStudentById(Long studentId);

    /**
     * 批量删除学生档案
     */
    public int deleteStuStudentByIds(Long[] studentIds);

    /**
     * 校验学生编号是否唯一
     */
    public StuStudent checkStudentNoUnique(String studentNo);

    /**
     * 统计某班级学生人数
     */
    public int countStudentByClassId(Long classId);

    /**
     * 统计在册学生总数
     */
    public int countAllStudent();

    /**
     * 按班级统计学生人数
     */
    public List<Map<String, Object>> countGroupByClass();

    /**
     * 统计积分分布区间
     */
    public List<Map<String, Object>> countScoreDistribution();
}
