package com.ooamo.server.service.impl;

import com.ooamo.server.pojo.MailLog;
import com.ooamo.server.mapper.MailLogMapper;
import com.ooamo.server.service.IMailLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
@Service
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

}
