package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SAPRespType {

    SUCCESS("S", "成功"),
    ERROR("E", "失败"),
    ;

    private final String type;
    private final String name;

}
