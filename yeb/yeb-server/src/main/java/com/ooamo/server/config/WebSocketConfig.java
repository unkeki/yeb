package com.ooamo.server.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ooamo.server.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    /**
     * 添加这个Endpoint，这样在网页中就可以通过websocket连接上服务,
     * 也就是我们配置websocket的服务地址,并且可以指定是否使用socketJs
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * 1. 将 /ws/ep路径注册为STOMP的端点，用户连接了这个端点后就可以进行websocket通
         * 讯，支持socketJs
         * 2. setAllowedOrigins("*")允许跨域
         * 3. withSockJS()表示支持socketJS访问
         */

        registry.addEndpoint("/ws/ep").setAllowedOrigins("*").withSockJS();
    }

    /**
     * 输入通道参数设置
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                //如果是连接，获取token，设置用户对象
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Auth-Token");
                    if (!StringUtils.isBlank(token)) {
                        String authToken = token.substring(tokenHead.length());
                        String username = jwtTokenUtil.getUserNameFromToken(authToken);
                        //token中存在用户名
                        if (null != username) {
                            //登录
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            //验证token是否有效，重新设置用户对象
                            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                                userDetails.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                accessor.setUser(authentication);
                            }
                        }
                    }
                }
                return message;
            }
        });
}

    /**
     * 配置消息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        /**
         * /配置代理域，可以配置多个，此段代码配置代理目的地的前缀为 /queue，我们就可以在配置的
         * 域上向客户端推送消息
         */
        registry.enableSimpleBroker("/queue");

    }
}
