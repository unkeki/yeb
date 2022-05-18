package com.ooamo.server.config.comment;

import com.ooamo.server.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //获取token
        String authHeader = request.getHeader(this.tokenHeader);
        //存在token
        if(authHeader!=null && authHeader.startsWith(this.tokenHead)){
            String authToken = authHeader.substring(this.tokenHead.length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            //token存在用户名但未登陆授权
            if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
                //获取userDetail
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                //验证token是否有效，重新设置用户对象
                if(jwtTokenUtil.validateToken(authToken,userDetails)){
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken
                            (userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        //放行，跳转下一个过滤器
        filterChain.doFilter(request,response);
    }
}
