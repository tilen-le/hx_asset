package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DingTalkAssetProcessType {

    PROCESS_RECEIVE("100", "领用流程"),
    PROCESS_RECEIVE_BY_ADMIN("110", "代领用流程"),
    PROCESS_BACK("200", "归还流程"),
    PROCESS_BACK_BY_ADMIN("210", "代归还流程"),
    PROCESS_EXCHANGE("300", "更换流程"),
    PROCESS_TRANSFER("400", "转移流程"),
    PROCESS_SCRAP("500", "处置流程（报废）"),
    PROCESS_SALE_OUT("510", "处置流程（外卖）"),
    PROCESS_TRANSFORM("600", "改造流程"),
    ;

    private final String code;
    private final String name;

}
