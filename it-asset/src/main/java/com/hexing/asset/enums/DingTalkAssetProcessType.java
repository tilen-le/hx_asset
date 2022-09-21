package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DingTalkAssetProcessType {

    PROCESS_RECEIVE("100", "领用流程"),
    PROCESS_BACK("200", "归还流程"),
    PROCESS_EXCHANGE("300", "更换流程"),
    PROCESS_TRANSFER("400", "转移流程"),
    ;

    private final String code;
    private final String name;

}
