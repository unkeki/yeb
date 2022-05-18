package com.ooamo.server.config.comment;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {

    /**
     * @param authentication    当前登陆用户信息
     * @param object    当前请求对象（FilterInvocation对象）
     * @param configAttributes  FilterInvocationSecurityMetadataSource接口实现类中getAttributes方法的返回值
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute>
            configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        for(ConfigAttribute configAttribute : configAttributes){
            //当前url所需角色
            String needRole = configAttribute.getAttribute();
            if("ROLE_LOGIN".equals(needRole)){
                //判断是否登陆
                if(authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("尚未登陆，请登录！");
                }
                else {
                    return;
                }
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for(GrantedAuthority authority : authorities){
                if(authority.getAuthority().equals(needRole)){
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足，请联系管理员！");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
