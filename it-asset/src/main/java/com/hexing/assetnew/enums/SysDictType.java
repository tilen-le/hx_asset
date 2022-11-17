package com.hexing.assetnew.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysDictType {

    /**
     * 资产盘点流程
     */
    ASSET_IMPORT_REQUIRED_FIELD("资产导入必填字段", "asset_import_required_field"),
    ;

    private final String desc;
    private final String value;

}
