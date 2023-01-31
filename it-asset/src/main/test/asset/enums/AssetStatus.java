package com.hexing.assetNew.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetStatus {

    /**
     * 闲置
     */
    UNUSED("0", "闲置"),
    /**
     * 在用
     */
    USING("1", "在用"),
    ;

    private final String status;
    private final String name;

}
