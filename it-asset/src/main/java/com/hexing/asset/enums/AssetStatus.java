package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetStatus {

    UNUSED("0", "闲置"),
    USING("1", "在用"),
    ;

    private final String status;
    private final String name;

}
