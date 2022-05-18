package com.ooamo.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ooamo.server.pojo.AdminRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    Integer addRole(@Param("adminId") Integer adminId, @Param("rids") Integer[] rids);

}
