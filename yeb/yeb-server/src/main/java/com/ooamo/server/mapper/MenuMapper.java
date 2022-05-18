package com.ooamo.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ooamo.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 通过用户id获取菜单
     * @param id
     * @return
     */
    List<Menu>  getMenuByAdminId(Integer id);

    /**
     * 通过角色获取菜单列表
     * @return
     */
    List<Menu> getAllMenuWithRole();

    /**
     * 获取所有菜单
     * @return
     */
    List<Menu> getAllMenus();

}
