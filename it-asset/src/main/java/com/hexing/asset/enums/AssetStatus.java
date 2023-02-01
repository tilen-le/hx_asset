package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetStatus {

    IN_STORE("1", "在库"),
    UNUSED("2", "闲置"),
    RETURNED("3", "已退货"),
    TRIAL("4", "试用"),
    REPAIR("5", "返修"),
    USING("6", "在用"),
    WAITING_TAKE_OUT("7", "待外卖"),
    MAINTAIN("8", "维修"),
    WAITING_SCRAP("9", "待报废"),
    TOKE_OUT("10", "已外卖"),
    SCRAPED("11", "已报废")
    ;

    private final String code;
    private final String name;

}
