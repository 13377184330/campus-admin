package com.ruoyi.stu.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.stu.domain.StuClass;
import com.ruoyi.stu.domain.StuScoreLog;
import com.ruoyi.stu.domain.StuStudent;
import com.ruoyi.stu.domain.StuTransferLog;
import com.ruoyi.stu.mapper.StuClassMapper;
import com.ruoyi.stu.mapper.StuScoreLogMapper;
import com.ruoyi.stu.mapper.StuStudentMapper;
import com.ruoyi.stu.mapper.StuTransferLogMapper;
import com.ruoyi.stu.service.IStuStudentService;

/**
 * 学生档案 服务层实现
 *
 * @author campus
 */
@Service
public class StuStudentServiceImpl implements IStuStudentService
{
    @Autowired
    private StuStudentMapper stuStudentMapper;

    @Autowired
    private StuClassMapper stuClassMapper;

    @Autowired
    private StuScoreLogMapper stuScoreLogMapper;

    @Autowired
    private StuTransferLogMapper stuTransferLogMapper;

    @Override
    public List<StuStudent> selectStuStudentList(StuStudent stuStudent)
    {
        return stuStudentMapper.selectStuStudentList(stuStudent);
    }

    @Override
    public StuStudent selectStuStudentById(Long studentId)
    {
        return stuStudentMapper.selectStuStudentById(studentId);
    }

    @Override
    public boolean checkStudentNoUnique(StuStudent stuStudent)
    {
        Long studentId = stuStudent.getStudentId() == null ? -1L : stuStudent.getStudentId();
        StuStudent info = stuStudentMapper.checkStudentNoUnique(stuStudent.getStudentNo());
        if (StringUtils.isNotNull(info) && info.getStudentId().longValue() != studentId.longValue())
        {
            return false;
        }
        return true;
    }

    @Override
    public int insertStuStudent(StuStudent stuStudent)
    {
        if (StringUtils.isEmpty(stuStudent.getStudentNo()))
        {
            stuStudent.setStudentNo(generateStudentNo());
        }
        if (stuStudent.getTotalScore() == null)
        {
            stuStudent.setTotalScore(0);
        }
        return stuStudentMapper.insertStuStudent(stuStudent);
    }

    @Override
    public int updateStuStudent(StuStudent stuStudent)
    {
        return stuStudentMapper.updateStuStudent(stuStudent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStuStudentByIds(Long[] studentIds)
    {
        stuScoreLogMapper.deleteStuScoreLogByStudentIds(studentIds);
        stuTransferLogMapper.deleteStuTransferLogByStudentIds(studentIds);
        return stuStudentMapper.deleteStuStudentByIds(studentIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeScore(StuScoreLog scoreLog)
    {
        if (scoreLog.getStudentId() == null)
        {
            throw new ServiceException("请选择学生");
        }
        if (scoreLog.getChangeScore() == null || scoreLog.getChangeScore() == 0)
        {
            throw new ServiceException("变动分值不能为 0");
        }
        StuStudent student = stuStudentMapper.selectStuStudentById(scoreLog.getStudentId());
        if (StringUtils.isNull(student))
        {
            throw new ServiceException("学生不存在");
        }
        int current = student.getTotalScore() == null ? 0 : student.getTotalScore();
        int after = current + scoreLog.getChangeScore();
        scoreLog.setScoreAfter(after);
        stuStudentMapper.updateStudentScore(scoreLog.getStudentId(), after, scoreLog.getCreateBy());
        return stuScoreLogMapper.insertStuScoreLog(scoreLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int transferClass(StuTransferLog transferLog)
    {
        if (transferLog.getStudentId() == null)
        {
            throw new ServiceException("请选择学生");
        }
        if (transferLog.getToClassId() == null)
        {
            throw new ServiceException("请选择要调入的班级");
        }
        StuStudent student = stuStudentMapper.selectStuStudentById(transferLog.getStudentId());
        if (StringUtils.isNull(student))
        {
            throw new ServiceException("学生不存在");
        }
        if (transferLog.getToClassId().equals(student.getClassId()))
        {
            throw new ServiceException("该学生当前已在此班级");
        }
        StuClass target = stuClassMapper.selectStuClassById(transferLog.getToClassId());
        if (StringUtils.isNull(target))
        {
            throw new ServiceException("目标班级不存在");
        }
        transferLog.setFromClassId(student.getClassId());
        stuStudentMapper.updateStudentClass(transferLog.getStudentId(), transferLog.getToClassId(), transferLog.getCreateBy());
        return stuTransferLogMapper.insertStuTransferLog(transferLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importStudent(List<StuStudent> studentList, boolean updateSupport, String operName)
    {
        if (StringUtils.isEmpty(studentList))
        {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (int i = 0; i < studentList.size(); i++)
        {
            StuStudent student = studentList.get(i);
            int rowNo = i + 2;
            try
            {
                if (StringUtils.isEmpty(student.getName()))
                {
                    failureNum++;
                    failureMsg.append("<br/>第 " + rowNo + " 行：姓名为空，已跳过");
                    continue;
                }
                if (StringUtils.isNotEmpty(student.getClassName()))
                {
                    StuClass stuClass = stuClassMapper.checkClassNameUnique(student.getClassName().trim());
                    if (StringUtils.isNull(stuClass))
                    {
                        failureNum++;
                        failureMsg.append("<br/>第 " + rowNo + " 行：班级【" + student.getClassName() + "】不存在，请先在班级管理中创建");
                        continue;
                    }
                    student.setClassId(stuClass.getClassId());
                }
                StuStudent exist = StringUtils.isNotEmpty(student.getStudentNo())
                        ? stuStudentMapper.checkStudentNoUnique(student.getStudentNo().trim()) : null;
                if (StringUtils.isNull(exist))
                {
                    if (StringUtils.isEmpty(student.getStudentNo()))
                    {
                        student.setStudentNo(generateStudentNo());
                    }
                    if (student.getTotalScore() == null)
                    {
                        student.setTotalScore(0);
                    }
                    if (StringUtils.isEmpty(student.getStatus()))
                    {
                        student.setStatus("0");
                    }
                    student.setCreateBy(operName);
                    stuStudentMapper.insertStuStudent(student);
                    successNum++;
                }
                else if (updateSupport)
                {
                    student.setStudentId(exist.getStudentId());
                    student.setUpdateBy(operName);
                    stuStudentMapper.updateStuStudent(student);
                    successNum++;
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>第 " + rowNo + " 行：学生编号 " + student.getStudentNo() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                failureMsg.append("<br/>第 " + rowNo + " 行：" + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            String msg = "导入完成，成功 " + successNum + " 条，失败 " + failureNum + " 条。" + failureMsg;
            if (successNum > 0)
            {
                successMsg.insert(0, "导入完成，成功 " + successNum + " 条，失败 " + failureNum + " 条。" + failureMsg);
                return successMsg.toString();
            }
            throw new ServiceException(msg);
        }
        return "导入成功，共 " + successNum + " 条数据。";
    }

    /**
     * 生成学生编号（XS + 年份 + 4位序号）
     */
    private String generateStudentNo()
    {
        String prefix = "XS" + new java.text.SimpleDateFormat("yyyy").format(new java.util.Date());
        StuStudent query = new StuStudent();
        List<StuStudent> all = stuStudentMapper.selectStuStudentList(query);
        int max = 0;
        for (StuStudent s : all)
        {
            String no = s.getStudentNo();
            if (StringUtils.isNotEmpty(no) && no.startsWith(prefix))
            {
                try
                {
                    int seq = Integer.parseInt(no.substring(prefix.length()));
                    if (seq > max)
                    {
                        max = seq;
                    }
                }
                catch (NumberFormatException ignored)
                {
                }
            }
        }
        return prefix + String.format("%04d", max + 1);
    }
}
