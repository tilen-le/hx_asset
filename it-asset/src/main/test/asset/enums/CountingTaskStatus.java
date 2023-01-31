package com.hexing.assetNew.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountingTaskStatus {

    /**
     * 盘点中
     */
    COUNTING("0", "盘点中"),
    /**
     * 已完成
     */
    FINISHED("1", "已完成"),
    ;

    private final String status;
    private final String name;

}
