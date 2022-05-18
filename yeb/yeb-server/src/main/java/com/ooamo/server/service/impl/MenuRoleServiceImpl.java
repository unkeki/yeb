package com.ooamo.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooamo.server.pojo.MenuRole;
import com.ooamo.server.mapper.MenuRoleMapper;
import com.ooamo.server.pojo.RespBean;
import com.ooamo.server.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {


    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Override
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        menuRoleMapper.delete(new QueryWrapper<MenuRole>()
                .eq("rid",rid));
        if(mids == null || mids.length == 0){
            return RespBean.success("更新成功！");
        }
        Integer record = menuRoleMapper.insertRecord(mids, rid);
        if(record == mids.length){
            return RespBean.success("更新成功！");
        }
        return RespBean.fail("更新失败！");
    }
}
