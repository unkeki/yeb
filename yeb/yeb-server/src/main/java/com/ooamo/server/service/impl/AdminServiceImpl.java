package com.ooamo.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ooamo.server.mapper.AdminMapper;
import com.ooamo.server.mapper.AdminRoleMapper;
import com.ooamo.server.mapper.RoleMapper;
import com.ooamo.server.pojo.Admin;
import com.ooamo.server.pojo.AdminRole;
import com.ooamo.server.pojo.RespBean;
import com.ooamo.server.pojo.Role;
import com.ooamo.server.service.IAdminService;
import com.ooamo.server.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ooamo
 * @since 2022-03-23
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private IAdminService adminService;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RoleMapper roleMapper;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    HttpServletRequest req;

    /**
     * 登陆返回token
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        String captcha = (String) request.getSession().getAttribute("captcha");

        if(StringUtils.isEmpty(captcha) || !captcha.equals(code)){
            return RespBean.fail("验证码填写错误！");
        }
        //从数据库中获取用户，只有用户名和密码
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        if(userDetails == null || !passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.fail("用户名或密码不正确！");
        }
        if(!userDetails.isEnabled()){
            return RespBean.fail("账户被禁用，请联系管理员！");
        }
        //授权，用户第一次登陆就会给予权限
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        //将用户信息使用jwt进行加密
        String token = jwtTokenUtil.generateToken(userDetails);
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);

        return RespBean.success("登陆成功",tokenMap);
    }

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUserName(String username) {

        return adminMapper.selectOne(new QueryWrapper<Admin>()
                .eq("username", username)
                .eq("enabled", true));
    }

    /**
     * 根据用户id获取用户角色
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    /**
     * 获得所有操作员
     * @param keywords
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        Integer adminId = (Integer) req.getSession().getAttribute("adminId");
//        System.out.println(adminId);
        return adminMapper.getAllAdmins(adminId ,keywords);
    }

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>()
                .eq("adminId",adminId));
        Integer result = adminRoleMapper.addRole(adminId, rids);
        if(result == rids.length){
            return RespBean.success("更新成功！");
        }
        return RespBean.fail("更新失败！");
    }

    @Override
    public RespBean updatePassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(oldPass,admin.getPassword())){
            admin.setPassword(encoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if(result == 1){
                return RespBean.success("修改成功！");
            }
        }
        return RespBean.fail("修改失败！");
    }
}
