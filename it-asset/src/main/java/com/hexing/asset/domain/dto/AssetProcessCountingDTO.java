package com.hexing.asset.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.asset.domain.Process;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "盘点记录DTO")
public class AssetProcessCountingDTO extends Process {

    private static final long serialVersionUID = 1L;

    /**
     * 实例ID
     */
    @ApiModelProperty(value = "实例ID")
    private String instanceId;

    /**
     * 盘点任务编码
     */
    @ApiModelProperty(value = "盘点任务编码")
    private String taskCode;

    /**
     * 发起人工号
     */
    @ApiModelProperty(value = "发起人工号")
    private String userCode;

    /**
     * 发起人名称
     */
    @ApiModelProperty(value = "发起人名称")
    private String userName;

    /**
     * 平台资产编码
     */
    @ApiModelProperty(value = "平台资产编码")
    private String assetCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 盘点状态
     */
    @ApiModelProperty(value = "盘点状态")
    private String countingStatus;

    /**
     * 盘点时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "盘点时间")
    private Date countingTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comment;

}
