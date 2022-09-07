package com.hexing.framework.security;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String password;
    private String username;
    private Integer loginType = 1;
    private Boolean rememberMe = false;
}
