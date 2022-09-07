package com.hexing.framework.security;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String code;//200 成功 400 失败
    private String expire;
    private String token;
    private String msg;
}
