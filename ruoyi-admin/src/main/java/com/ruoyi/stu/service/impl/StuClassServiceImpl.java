package com.ruoyi.stu.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.stu.domain.StuClass;
import com.ruoyi.stu.mapper.StuClassMapper;
import com.ruoyi.stu.mapper.StuStudentMapper;
import com.ruoyi.stu.service.IStuClassService;

/**
 * 班级信息 服务层实现
 *
 * @author campus
 */
@Service
public class StuClassServiceImpl implements IStuClassService
{
    @Autowired
    private StuClassMapper stuClassMapper;

    @Autowired
    private StuStudentMapper stuStudentMapper;

    @Override
    public List<StuClass> selectStuClassList(StuClass stuClass)
    {
        return stuClassMapper.selectStuClassList(stuClass);
    }

    @Override
    public StuClass selectStuClassById(Long classId)
    {
        return stuClassMapper.selectStuClassById(classId);
    }

    @Override
    public boolean checkClassNameUnique(StuClass stuClass)
    {
        Long classId = stuClass.getClassId() == null ? -1L : stuClass.getClassId();
        StuClass info = stuClassMapper.checkClassNameUnique(stuClass.getClassName());
        if (StringUtils.isNotNull(info) && info.getClassId().longValue() != classId.longValue())
        {
            return false;
        }
        return true;
    }

    @Override
    public int insertStuClass(StuClass stuClass)
    {
        return stuClassMapper.insertStuClass(stuClass);
    }

    @Override
    public int updateStuClass(StuClass stuClass)
    {
        return stuClassMapper.updateStuClass(stuClass);
    }

    @Override
    public int deleteStuClassByIds(Long[] classIds)
    {
        for (Long classId : classIds)
        {
            StuClass stuClass = stuClassMapper.selectStuClassById(classId);
            if (StringUtils.isNull(stuClass))
            {
                continue;
            }
            int count = stuStudentMapper.countStudentByClassId(classId);
            if (count > 0)
            {
                throw new ServiceException(String.format("班级【%s】下还有 %d 名学生，请先为学生调班后再删除", stuClass.getClassName(), count));
            }
        }
        return stuClassMapper.deleteStuClassByIds(classIds);
    }
}
