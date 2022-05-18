package com.ooamo.server.service.impl;

import com.ooamo.server.pojo.Department;
import com.ooamo.server.mapper.DepartmentMapper;
import com.ooamo.server.pojo.RespBean;
import com.ooamo.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getAllDepartmentsByParentId() {
        return departmentMapper.getAllDepartmentsByParentId(-1);
    }

    @Override
    public RespBean addDep(Department dep) {
        dep.setEnabled(true);
        departmentMapper.addDep(dep);
        if(dep.getResult()==1){
            return RespBean.success("添加成功！");
        }
        return RespBean.fail("添加失败！");

    }

    @Override
    public RespBean deleteDep(Integer id) {
        Department dep = new Department();
        dep.setId(id);
        departmentMapper.deleteDep(dep);
        if (-2 == dep.getResult()) {
            return RespBean.fail("该部门下有子部门，删除失败！");
        }
        if (-1 == dep.getResult()) {
            return RespBean.fail("该部门下有员工，删除失败！");
        }
        if (1 == dep.getResult()) {
            return RespBean.success("删除成功！");
        }
        return RespBean.fail("删除失败！");
    }
}
