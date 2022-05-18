package com.ooamo.server.controller;


import com.ooamo.server.pojo.RespBean;
import com.ooamo.server.pojo.Salary;
import com.ooamo.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary/sob")
public class SalaryController {

    @Autowired
    private ISalaryService salaryService;

    @ApiOperation(value = "获得所有工资账套")
    @GetMapping("/")
    public List<Salary> getAllSalaries(){
        return salaryService.list();
    }

    @ApiOperation(value = "添加员工工资账套")
    @PostMapping("/")
    public RespBean addSalary(@RequestBody Salary salary){
        if(salaryService.save(salary)){
            return RespBean.success("添加成功");
        }
        return RespBean.fail("添加失败");
    }

    @ApiOperation(value = "删除工资账套")
    @DeleteMapping("/{id}")
    public RespBean deleteSalary(@PathVariable("id") Integer id){
        if(salaryService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.fail("删除失败！");
    }

    @ApiOperation(value = "更新工资账套")
    @PutMapping("/")
    public RespBean updateSalary(@RequestBody Salary salary){
        if(salaryService.updateById(salary)){
            return RespBean.success("更新成功！");
        }
        return RespBean.fail("更新失败！");
    }
}
