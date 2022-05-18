package com.ooamo.server.service.impl;

import com.ooamo.server.pojo.Oplog;
import com.ooamo.server.mapper.OplogMapper;
import com.ooamo.server.service.IOplogService;
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
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
