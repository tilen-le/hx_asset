package com.hexing.framework.security;

import com.alibaba.fastjson.JSON;
import com.hexing.common.core.domain.model.LoginUser;
import com.hexing.common.utils.OkHttpUtil;
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

import javax.annotation.Resource;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserDetailsService userDetailsService;
    @Value("${ding.login}")
    private String loginUrl;

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
            getTokenFromAD(username, password);
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

    private String getTokenFromAD(String username, String password) {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername(username);
        loginRequestDTO.setPassword(password);
        String res_str = OkHttpUtil.postJson(loginUrl, JSON.toJSONString(loginRequestDTO));
        if (res_str != null) {
            LoginResponseDTO loginResponseDTO = JSON.parseObject(res_str, LoginResponseDTO.class);
            if ("200".equals(loginResponseDTO.getCode())) {
                return loginResponseDTO.getToken();
            } else {
                throw new RuntimeException(loginResponseDTO.getMsg());
            }
        }
        return null;
    }

}
