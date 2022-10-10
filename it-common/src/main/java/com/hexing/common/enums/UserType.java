package com.hexing.common.enums;

public enum UserType {
    /**
     * 系统用户
     */
    SYSTEM_USER("00", "系统用户"),
    /**
     * 钉钉用户
     */
    DINGTALK_USER("1", "钉钉用户");

    private final String code;
    private final String info;

    UserType(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
