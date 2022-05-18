package com.ooamo.server.config;

import com.ooamo.server.config.comment.*;
import com.ooamo.server.pojo.Admin;
import com.ooamo.server.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * security配置类
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    @Autowired
    private CustomFilter customFilter;

    @Override
    public void configure(WebSecurity web) throws Exception{

        //放行静态资源
        web.ignoring().antMatchers(
                "/login",
                "/logout",
                "/favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha",
                "/ws/**",
                "/css/**",
                "/js/**",
                "/index.html"
        );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        //使用jwt，不使用csrf
        http.csrf()
                .disable()

                //基于token，不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)

                .and()
                .authorizeRequests()

                //允许登陆访问
                .antMatchers("/login","/logout","/")
                .permitAll()

                //除了上面的，其他都需要认证
                .anyRequest()
                .authenticated()
                //动态权限设置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        //权限决策器
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        //判断url需要什么权限
                        object.setSecurityMetadataSource(customFilter);
                        return object;
                    }
                })
                .and()

                //禁用缓冲
                .headers()
                .cacheControl();

        //添加jwt登陆授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);
    }


    @Override
    @Bean
    public UserDetailsService userDetailsService(){

        //获取登陆用户信息
        return username -> {
            Admin admin = adminService.getAdminByUserName(username);
            if(admin != null){
                admin.setRoles(adminService.getRoles(admin.getId()));
                return admin;
            }
            throw new UsernameNotFoundException("用户或密码不正确！");
        };

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }


}

