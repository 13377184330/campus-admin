package com.ruoyi.stu.service;

import java.util.List;
import com.ruoyi.stu.domain.StuClass;

/**
 * 班级信息 服务层
 *
 * @author campus
 */
public interface IStuClassService
{
    /**
     * 查询班级信息列表
     */
    public List<StuClass> selectStuClassList(StuClass stuClass);

    /**
     * 根据班级ID查询班级信息
     */
    public StuClass selectStuClassById(Long classId);

    /**
     * 校验班级名称是否唯一
     */
    public boolean checkClassNameUnique(StuClass stuClass);

    /**
     * 新增班级信息
     */
    public int insertStuClass(StuClass stuClass);

    /**
     * 修改班级信息
     */
    public int updateStuClass(StuClass stuClass);

    /**
     * 批量删除班级信息
     */
    public int deleteStuClassByIds(Long[] classIds);
}
