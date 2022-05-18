package com.ooamo.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ooamo.server.pojo.Employee;
import com.ooamo.server.pojo.MailConstants;
import com.ooamo.server.pojo.MailLog;
import com.ooamo.server.service.IEmployeeService;
import com.ooamo.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件发送定时任务
 */
@Component
public class    MailTask {

    @Autowired
    private IMailLogService mailLogService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IEmployeeService employeeService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask(){
        //状态为0，且重试时间小于当前时间的才能发送
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>()
                .eq("status", 0)
                .lt("tryTime", LocalDateTime.now()));

        list.forEach(mailLog -> {
            //重试次数大于三次
            if(mailLog.getCount() >= 3){
                mailLogService.update(new UpdateWrapper<MailLog>()
                        .set("status",2)
                        .eq("msgId",mailLog.getMsgId()));
            }
            mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("count",mailLog.getCount()+1)
                    .set("updateTime",LocalDateTime.now())
                    .set("tryTime",LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT)));
            Employee employee = employeeService.getEmployee(mailLog.getEid()).get(0);

            //发送消息
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY_NAME,
                    employee,new CorrelationData(mailLog.getMsgId()));
        });
    }
}
