package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetCountingStatus {

    NOT_COUNTED("0", "待盘点"),
    COUNTED("1", "已盘点"),
    ABNORMAL("2", "盘点异常"),
    ;

    private final String status;
    private final String name;

}
