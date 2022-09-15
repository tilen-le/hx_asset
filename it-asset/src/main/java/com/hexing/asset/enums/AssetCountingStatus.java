package com.hexing.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetCountingStatus {

    COUNTED("0", "未盘点"),
    NOT_COUNTED("1", "已盘点"),
    ABNORMAL("2", "盘点异常"),
    ;

    private String status;
    private String name;
}
