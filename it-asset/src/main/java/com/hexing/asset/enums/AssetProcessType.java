package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetProcessType {
    /**
     * 资产盘点流程
     */
    ASSET_COUNTING("100", "资产盘点流程"),
    ;

    private final String code;
    private final String name;

}
