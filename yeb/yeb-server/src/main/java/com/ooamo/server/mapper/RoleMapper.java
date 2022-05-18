package com.ooamo.server.mapper;

import com.ooamo.server.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id获取权限列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);

}
