package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UIPCodeEnum {

    FIX_ASSET_INTERFACE("ZFMFICOZC006","资产平台转固信息同步SAP"),
    ;

    private final String code;
    private final String msg;
}
