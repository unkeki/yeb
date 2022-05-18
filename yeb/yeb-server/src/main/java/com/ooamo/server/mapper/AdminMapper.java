package com.ooamo.server.mapper;

import com.ooamo.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 查询所有操作员
     * @param id
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmins(@Param("id") Integer id, @Param("keywords") String keywords);

}
