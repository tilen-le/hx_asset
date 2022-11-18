package com.hexing.assetnew.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysDictTypeEnum {

    /**
     * 资产盘点流程
     */
    ASSET_IMPORT_REQUIRED_FIELD("资产导入必填字段", "asset_import_required_field"),
    COMPANY_LIST("集团公司", "company_list"),
    MANAGE_DEPT("资产导入必填字段", "manage_dept"),
    ASSET_CATEGORY("资产导入必填字段", "asset_category"),
    ;

    private final String desc;
    private final String value;

}
