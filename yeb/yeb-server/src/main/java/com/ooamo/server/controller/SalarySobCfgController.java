package com.ooamo.server.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ooamo.server.pojo.Employee;
import com.ooamo.server.pojo.RespBean;
import com.ooamo.server.pojo.RespPageBean;
import com.ooamo.server.pojo.Salary;
import com.ooamo.server.service.IEmployeeService;
import com.ooamo.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary/sobcfg")
public class SalarySobCfgController {

    @Autowired
    private ISalaryService salaryService;

    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "获得所有员工账套")
    @GetMapping("/")
    public RespPageBean getEmployeeWithSalary(@RequestParam(defaultValue = "1") Integer currentPage,
                                              @RequestParam(defaultValue = "10") Integer size){
        return employeeService.getEmployeeWithSalary(currentPage,size);
    }

    @ApiOperation(value = "获取所有工资账套")
    @GetMapping("/salaries")
    public List<Salary> getAllSalaries(){
        return salaryService.list();
    }

    @ApiOperation(value = "更新员工账套")
    @PutMapping("/")
    public RespBean updateEmployeeSalary(Integer eid,Integer sid){
        if(employeeService.update(new UpdateWrapper<Employee>().set("salaryId",sid).eq("id",eid))){
            return RespBean.success("更新成功！");

        }
        return RespBean.fail("更新失败！");
    }
}
