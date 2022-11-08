package com.hexing.asset.domain;

import lombok.Data;

@Data
public class VariableDO {

    /** 主键 */
    private Long id;

    /** 流程id */
    private String processId;

    /** 字段id */
    private String fieldId;

    /** 字段标签 */
    private String fieldKey;

    /** 字段值 */
    private String fieldValue;

}
