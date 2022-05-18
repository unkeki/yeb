package com.ooamo.server.service;

import com.ooamo.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ooamo.server.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(Integer rid,Integer[] mids);

}
