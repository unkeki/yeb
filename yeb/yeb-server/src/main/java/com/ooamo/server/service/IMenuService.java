package com.ooamo.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ooamo.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 通过用户id获取菜单
     * @return
     */
    List<Menu> getMenuByAdminId();


    /**
     * 通过觉得获取菜单列表
     * @return
     */
    List<Menu> getAllMenuWithRole();

    /**
     * 获取所有菜单
     * @return
     */
    List<Menu> getAllMenus();

}
