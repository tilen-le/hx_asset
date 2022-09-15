package com.hexing.asset.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountingTaskStatus {

    COUNTING("0", "盘点中"),
    FINISHED("1", "已完成"),
    ;

    private String status;
    private String name;

}
