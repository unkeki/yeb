package com.ooamo.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ooamo.server.mapper.MenuMapper;
import com.ooamo.server.pojo.Admin;
import com.ooamo.server.pojo.Menu;
import com.ooamo.server.pojo.Role;
import com.ooamo.server.service.IMenuService;
import com.ooamo.server.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List<Menu> getMenuByAdminId() {
        Integer adminId = ((Admin)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        String key = "menu:rank:"+adminId;
        List<Menu> menus = (List<Menu>) redisUtil.hget(key,"menu");
        if(CollectionUtils.isEmpty(menus)){
            menus = menuMapper.getMenuByAdminId(adminId);
            redisUtil.hset(key,"menu",menus);
        }
//        System.out.println(menus);
        return menus;
    }

    /**
     * 角色和菜单项进行对应，查找那个角色可以打开哪些菜单
     * @return
     */
    @Override
    public List<Menu> getAllMenuWithRole() {
        return menuMapper.getAllMenuWithRole();
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }
}
