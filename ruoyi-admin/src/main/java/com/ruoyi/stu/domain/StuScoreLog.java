package com.ruoyi.stu.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 积分流水对象 stu_score_log
 *
 * @author campus
 */
public class StuScoreLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流水ID */
    private Long logId;

    /** 学生ID */
    private Long studentId;

    /** 学生姓名（关联展示，非表字段） */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 学生编号（关联展示，非表字段） */
    @Excel(name = "学生编号")
    private String studentNo;

    /** 变动分值（正数加分 负数扣分） */
    @Excel(name = "变动分值")
    private Integer changeScore;

    /** 变动后积分 */
    @Excel(name = "变动后积分")
    private Integer scoreAfter;

    /** 加/减分项目 */
    @Excel(name = "加/减分项目")
    private String reason;

    /** 操作老师姓名（关联 sys_user 展示，非表字段） */
    @Excel(name = "操作老师")
    private String createByName;

    public Long getLogId()
    {
        return logId;
    }

    public void setLogId(Long logId)
    {
        this.logId = logId;
    }

    public Long getStudentId()
    {
        return studentId;
    }

    public void setStudentId(Long studentId)
    {
        this.studentId = studentId;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public String getStudentNo()
    {
        return studentNo;
    }

    public void setStudentNo(String studentNo)
    {
        this.studentNo = studentNo;
    }

    public Integer getChangeScore()
    {
        return changeScore;
    }

    public void setChangeScore(Integer changeScore)
    {
        this.changeScore = changeScore;
    }

    public Integer getScoreAfter()
    {
        return scoreAfter;
    }

    public void setScoreAfter(Integer scoreAfter)
    {
        this.scoreAfter = scoreAfter;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getCreateByName()
    {
        return createByName;
    }

    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }
}
