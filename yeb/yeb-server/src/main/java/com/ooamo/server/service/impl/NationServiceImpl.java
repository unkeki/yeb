package com.ooamo.server.service.impl;

import com.ooamo.server.pojo.Nation;
import com.ooamo.server.mapper.NationMapper;
import com.ooamo.server.service.INationService;
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
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

}
