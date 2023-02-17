package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ManagerType {

    ASSET_MANAGER("0", "资产管理员"),
    FINANCIAL_MANAGER("1", "账务管理员"),
    ;

    private final String type;
    private final String name;
}
