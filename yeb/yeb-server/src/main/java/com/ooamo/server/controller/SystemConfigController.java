package com.ooamo.server.controller;


import com.ooamo.server.pojo.Menu;
import com.ooamo.server.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/config")
public class SystemConfigController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "通过用户id获取菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenuByAdminId(){
        return menuService.getMenuByAdminId();
    }

}
