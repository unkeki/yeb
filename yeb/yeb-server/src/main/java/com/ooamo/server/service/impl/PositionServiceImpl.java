package com.ooamo.server.service.impl;

import com.ooamo.server.pojo.Position;
import com.ooamo.server.mapper.PositionMapper;
import com.ooamo.server.service.IPositionService;
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
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

}
