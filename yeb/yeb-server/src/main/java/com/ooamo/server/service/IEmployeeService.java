package com.ooamo.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ooamo.server.pojo.Employee;
import com.ooamo.server.pojo.RespBean;
import com.ooamo.server.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
public interface IEmployeeService extends IService<Employee> {


    /**
     * 通过分页获得员工列表
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */
    RespPageBean getAllEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);


    /**
     * 获取工号
     * @return
     */
    RespBean maxWorkId();


    /**
     * 添加员工
     * @param employee
     * @return
     */
    RespBean insertEmployee(Employee employee);

    /**
     * 查询员工
     * @param id
     * @return
     */
    List<Employee> getEmployee(Integer id);

    /**
     * 获得员工工资账套
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size);

}
