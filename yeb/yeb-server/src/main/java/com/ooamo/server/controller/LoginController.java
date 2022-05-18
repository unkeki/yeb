package com.ooamo.server.controller;

import com.ooamo.server.pojo.Admin;
import com.ooamo.server.pojo.AdminLogin;
import com.ooamo.server.pojo.RespBean;
import com.ooamo.server.service.impl.AdminServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登陆控制器
 */
@Api(tags = "LoginController")
@RestController
public class LoginController {

    @Autowired
    private AdminServiceImpl adminService;


    @ApiOperation(value = "登陆之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLogin adminLogin, HttpServletRequest request){
        Admin admin = adminService.getAdminByUserName(adminLogin.getUsername());
        request.getSession().setAttribute("adminId",admin.getId());
        return adminService.login(adminLogin.getUsername(),adminLogin.getPassword(),adminLogin.getCode(),request);
    }

    @ApiOperation(value = "返回当前用户信息")
    @GetMapping("/admin/info")
    public Admin info(Principal principal){
        if(null == principal){
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        admin.setPassword(null);
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }

    @ApiOperation(value = "退出登陆")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功!");
    }


}
