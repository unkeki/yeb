package com.ooamo.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ooamo.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 返回所有部门
     * @return
     */
    List<Department> getAllDepartmentsByParentId(Integer id);

    /**
     * 添加部门
     * @param dep
     */
    void addDep(Department dep);


    /**
     * 删除部门
     * @param dep
     */
    void deleteDep(Department dep);

}
