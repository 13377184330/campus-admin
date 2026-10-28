package com.ruoyi.stu.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 调班记录对象 stu_transfer_log
 *
 * @author campus
 */
public class StuTransferLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 调班记录ID */
    private Long transferId;

    /** 学生ID */
    private Long studentId;

    /** 学生姓名（关联展示，非表字段） */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 学生编号（关联展示，非表字段） */
    @Excel(name = "学生编号")
    private String studentNo;

    /** 原班级 */
    private Long fromClassId;

    /** 原班级名称（关联展示，非表字段） */
    @Excel(name = "原班级")
    private String fromClassName;

    /** 新班级 */
    private Long toClassId;

    /** 新班级名称（关联展示，非表字段） */
    @Excel(name = "新班级")
    private String toClassName;

    /** 调班原因 */
    @Excel(name = "调班原因")
    private String reason;

    /** 操作人姓名（关联 sys_user 展示，非表字段） */
    @Excel(name = "操作人")
    private String createByName;

    public Long getTransferId()
    {
        return transferId;
    }

    public void setTransferId(Long transferId)
    {
        this.transferId = transferId;
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

    public Long getFromClassId()
    {
        return fromClassId;
    }

    public void setFromClassId(Long fromClassId)
    {
        this.fromClassId = fromClassId;
    }

    public String getFromClassName()
    {
        return fromClassName;
    }

    public void setFromClassName(String fromClassName)
    {
        this.fromClassName = fromClassName;
    }

    public Long getToClassId()
    {
        return toClassId;
    }

    public void setToClassId(Long toClassId)
    {
        this.toClassId = toClassId;
    }

    public String getToClassName()
    {
        return toClassName;
    }

    public void setToClassName(String toClassName)
    {
        this.toClassName = toClassName;
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
