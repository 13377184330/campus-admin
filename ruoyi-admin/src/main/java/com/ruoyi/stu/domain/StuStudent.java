package com.ruoyi.stu.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学生档案对象 stu_student
 *
 * @author campus
 */
public class StuStudent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学生ID */
    private Long studentId;

    /** 学生编号 */
    @Excel(name = "学生编号")
    private String studentNo;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 性别（0男 1女） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String gender;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 14, dateFormat = "yyyy-MM-dd")
    private Date birthDate;

    /** 入学日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入学日期", width = 14, dateFormat = "yyyy-MM-dd")
    private Date enrollDate;

    /** 所在班级 */
    private Long classId;

    /** 所在班级名称（关联展示，非表字段） */
    @Excel(name = "所在班级")
    private String className;

    /** 当前积分 */
    @Excel(name = "当前积分")
    private Integer totalScore;

    /** 家长联系人 */
    @Excel(name = "家长联系人")
    private String parentName;

    /** 与学生关系 */
    @Excel(name = "与学生关系")
    private String parentRelation;

    /** 家长手机号 */
    @Excel(name = "家长手机号")
    private String parentPhone;

    /** 状态（0在校 1离校） */
    @Excel(name = "状态", readConverterExp = "0=在校,1=离校")
    private String status;

    public Long getStudentId()
    {
        return studentId;
    }

    public void setStudentId(Long studentId)
    {
        this.studentId = studentId;
    }

    public String getStudentNo()
    {
        return studentNo;
    }

    public void setStudentNo(String studentNo)
    {
        this.studentNo = studentNo;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public Date getEnrollDate()
    {
        return enrollDate;
    }

    public void setEnrollDate(Date enrollDate)
    {
        this.enrollDate = enrollDate;
    }

    public Long getClassId()
    {
        return classId;
    }

    public void setClassId(Long classId)
    {
        this.classId = classId;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public Integer getTotalScore()
    {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore)
    {
        this.totalScore = totalScore;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public String getParentRelation()
    {
        return parentRelation;
    }

    public void setParentRelation(String parentRelation)
    {
        this.parentRelation = parentRelation;
    }

    public String getParentPhone()
    {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone)
    {
        this.parentPhone = parentPhone;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
