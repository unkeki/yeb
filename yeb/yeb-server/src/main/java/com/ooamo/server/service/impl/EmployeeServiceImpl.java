package com.ooamo.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ooamo.server.pojo.*;
import com.ooamo.server.mapper.EmployeeMapper;
import com.ooamo.server.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ooamo.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IMailLogService mailLogService;

    /**
     * 分页获取员工
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */
    @Override
    public RespPageBean getAllEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope) {
        Page<Employee> page = new Page<>(currentPage, size);
        IPage<Employee> employeePage = employeeMapper.getAllEmployeeByPage(page, employee, beginDateScope);
        RespPageBean respPageBean = new RespPageBean(employeePage.getTotal(), employeePage.getRecords());
        return respPageBean;
    }

    /**
     * 获取工号
     * @return
     */
    @Override
    public RespBean maxWorkId() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>()
                .select("max(workID)"));
        return RespBean.success(null,
                String.format("%08d",Integer.parseInt(maps.get(0).get("max(workID)").toString())+1));
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @Override
    @Transactional
    public RespBean insertEmployee(Employee employee) {
        //合同时间
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        //合同期限
        long until = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(until/365.00)));
        //向员工发送录用邮件
        if(employeeMapper.insert(employee) == 1){

            Employee emp = employeeMapper.getEmployee(employee.getId()).get(0);
            //数据库记录发送消息
            String msgId = UUID.randomUUID().toString();
            MailLog mailLog = new MailLog();
            mailLog.setMsgId(msgId);
            mailLog.setEid(emp.getId());
            mailLog.setStatus(0);
            mailLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailLog.setCount(0);
            mailLog.setTryTime(LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT));
            mailLog.setCreateTime(LocalDateTime.now());
            mailLog.setUpdateTime(LocalDateTime.now());
            mailLogService.save(mailLog);
            //向消息队列发送消息
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,
                    MailConstants.MAIL_ROUTING_KEY_NAME,
                    emp,new CorrelationData(msgId));
            return RespBean.success("添加成功!");
        }
        return RespBean.fail("添加失败！");
    }

    @Override
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);
    }

    @Override
    public RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size) {
        Page<Employee> page = new Page<>(currentPage, size);

        IPage<Employee> employeePage = employeeMapper.getEmployeeWithSalary(page);

        RespPageBean respPageBean = new RespPageBean(employeePage.getTotal(), employeePage.getRecords());
        return respPageBean;
    }
}
