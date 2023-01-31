package com.hexing.assetNew.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SAPRespType {

    /**
     * 成功
     */
    SUCCESS("S", "成功"),
    /**
     * 失败
     */
    ERROR("E", "失败"),
    ;

    private final String type;
    private final String name;

}
