package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UIPCodeEnum {

    FIX_ASSET_INTERFACE("ZFMFICOZC006","资产平台转固信息同步SAP"),
    RECEIVE_ASSET_INTERFACE("ZFMFICOZC008","资产平台派发资产修改"),
    TRANSFER_ASSET_INTERFACE("ZFMFICOZC007", "资产转移信息同步SAP"),
    ;

    private final String code;
    private final String msg;
}
