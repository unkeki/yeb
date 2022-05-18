package com.ooamo.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ooamo.server.pojo.Department;
import com.ooamo.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 获得所有部门
     * @return
     */
    List<Department> getAllDepartmentsByParentId();

    /**
     * 添加部门
     * @param dep
     * @return
     */
    RespBean addDep(Department dep);

    /**
     * 删除部门
     * @param id
     * @return
     */
    RespBean deleteDep(Integer id);

}
