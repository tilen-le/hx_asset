package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UIPCodeEnum {

    FIX_ASSET_INTERFACE("","资产转固"),
    ;

    private final String code;
    private final String msg;
}
