package com.ooamo.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ooamo.server.mapper.RoleMapper;
import com.ooamo.server.pojo.Role;
import com.ooamo.server.service.IRoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
}
