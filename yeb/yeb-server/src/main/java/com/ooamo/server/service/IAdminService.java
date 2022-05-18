package com.ooamo.server.service;

import com.ooamo.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ooamo.server.pojo.RespBean;
import com.ooamo.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
public interface IAdminService extends IService<Admin> {


    /**
     * 登陆返回token
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    Admin getAdminByUserName(String username);

    /**
     * 通过用户id获取角色
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);

    /**
     * 获得所哟操作员
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmins(String keywords);

    /**
     * 为操作员更新角色
     * @param adminId
     * @param rids
     * @return
     */
    RespBean updateAdminRole(Integer adminId,Integer[] rids);

    /**
     * 修改密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    RespBean updatePassword(String oldPass,String pass,Integer adminId);
}
