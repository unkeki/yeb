package com.ooamo.server.controller;

import com.ooamo.server.pojo.Department;
import com.ooamo.server.pojo.RespBean;
import com.ooamo.server.service.IDepartmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "获得所有部门")
    @GetMapping("/")
    public List<Department> getAllDepartmentsByParentId(){
        return departmentService.getAllDepartmentsByParentId();
    }

    @ApiOperation(value = "添加部门")
    @PostMapping("/")
    public RespBean addDep(@RequestBody Department dep){
        return departmentService.addDep(dep);
    }

    @ApiOperation(value = "删除部门")
    @DeleteMapping("/{id}")
    public  RespBean deleteDep(@PathVariable Integer id){
        return departmentService.deleteDep(id);
    }
}
