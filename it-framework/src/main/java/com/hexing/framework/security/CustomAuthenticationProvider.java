package com.hexing.framework.security;

import com.alibaba.fastjson.JSON;
import com.hexing.common.core.domain.model.LoginUser;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.OkHttpUtil;
import com.hexing.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ldap.core.LdapTemplate;
import javax.annotation.Resource;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private LdapTemplate ldapTemplate;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    /** 自定义验证方式 */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserDetails node = userDetailsService.loadUserByUsername(username);
        // 此处钉钉域账号登录验证模式
        String userType = ((LoginUser) node).getUser().getUserType();
        if ("1".equals(userType)) {
            boolean authAD = authAD(username, password);
            if (!authAD) {
                throw new ServiceException("账户密码AD认证异常");
            }
        } else {
            if (!encoder.matches(password, node.getPassword())) {
                throw new BadCredentialsException("用户名或密码不正确!");
            }
        }
        return new UsernamePasswordAuthenticationToken(node, password, node.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    private boolean authAD(String username, String password) {
        if (StringUtils.isAnyBlank(username, password)) {
            throw new ServiceException("用户名密码不能为空");
        }
        boolean authenticate;
        try {
            String filter = "sAMAccountName=" + username;
            authenticate = ldapTemplate.authenticate("", filter, password);
        } catch (Exception e) {
            e.printStackTrace();
            authenticate = Boolean.FALSE;
        }
        return authenticate;
    }

}
