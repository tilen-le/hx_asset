package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetStatus {

    /**
     * 已转固
     */
    FIXED("1", "已转固"),
    /**
     * 未转固
     */

    UNFIXED("0", "未转固"),
    /**
     * 在库
     */
    IN_STORE("1", "在库"),
    /**
     * 闲置
     */
    UNUSED("2", "闲置"),
    /**
     * 已退货
     */
    RETURNED("3", "已退货"),
    /**
     * 试用
     */
    TRIAL("4", "试用"),
    /**
     * 盘亏
     */
    INVENTORY_LOSE("5", "盘亏"),
    /**
     * 在用
     */
    USING("6", "在用"),
    /**
     * 待外卖
     */
    WAITING_TAKE_OUT("7", "待外卖"),
    /**
     * 维修
     */
    MAINTAIN("8", "维修"),
    /**
     * 待报废
     */
    WAITING_SCRAP("9", "待报废"),
    /**
     * 已外卖
     */
    TOKE_OUT("10", "已外卖"),
    /**
     * 已报废
     */
    SCRAPED("11", "已报废");

    private final String code;
    private final String name;

}
