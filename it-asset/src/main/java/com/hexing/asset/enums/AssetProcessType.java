package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetProcessType {

    /**
     * 资产创建
     */
    PROCESS_ASSET_CREATE("111", "资产创建"),
    /**
     * 资产派发
     */
    PROCESS_RECEIVE("101", "资产派发"),
    /**
     * 资产转移
     */
    PROCESS_TRANSFORM("201", "资产转移"),
    /**
     * 账务资产转移
     */
    PROCESS_ACCOUNT_TRANSFORM("210", "账务资产转移"),
    /**
     * 已退货
     */
    RETURNED("301", "已退货"),
    /**
     * 资产转固
     */
    PROCESS_FIXED("401", "资产转固"),
    /**
     * 资产维修
     */
    PROCESS_MAINTAIN("501", "资产维修"),
    /**
     * 已维修
     */
    PROCESS_MAINTAINED("502", "已维修"),
    /**
     * 资产外卖
     */
    WAITING_TAKE_OUT("601", "资产外卖"),
    /**
     * 已外卖
     */
    TOKE_OUT("602", "已外卖"),
    /**
     * 报废
     */
    SCRAP("701", "报废"),
    /**
     * 已报废
     */
    SCRAPED("702", "已报废"),
    /**
     * 盘亏
     */
    PROCESS_INVENTORY_LOSE("801", "盘亏"),
    /**
     * 闲置
     */
    PROCESS_UNUSED("901", "闲置"),
    /**
     * 资产盘点流程
     */
    COUNTING_PROCESS("100", "资产盘点流程"),
    /**
     * 资产编辑
     */
    ASSET_MODIFIED("110", "资产编辑"),
    ;

    private final String code;
    private final String name;
}
