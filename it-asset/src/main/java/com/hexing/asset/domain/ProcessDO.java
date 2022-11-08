package com.hexing.asset.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.common.annotation.Excel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProcessDO {

    /** 主键 */
    private Long id;

    /** 流程类型编号 */
    private String processType;

    /** 资产id */
    private String assetId;

    /** 流程状态 */
    private String status;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 备注 */
    private String remark;

    /** 字段值列表 */
    private List<VariableDO> variableList;

}
