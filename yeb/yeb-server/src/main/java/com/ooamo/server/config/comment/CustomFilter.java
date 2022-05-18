package com.ooamo.server.config.comment;

import com.ooamo.server.pojo.Menu;
import com.ooamo.server.pojo.Role;
import com.ooamo.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;


/**
 * 权限控制
 * 根据请求url 分析出请求所需角色
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {
    
    @Autowired
    private IMenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取请求的url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        //获取菜单
         List<Menu> menus = menuService.getAllMenuWithRole();
        for(Menu menu : menus){
            //根据请求url与菜单角色是否匹配
            if(antPathMatcher.match(menu.getUrl(),requestUrl)){
                String[] str = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(str);
            }
        }
        //没有匹配的url默认为登陆即可访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
