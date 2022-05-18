package com.ooamo.server.mapper;

import com.ooamo.server.pojo.MenuRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    Integer insertRecord(@Param("mids") Integer[] mids, @Param("rid") Integer rid);

}
