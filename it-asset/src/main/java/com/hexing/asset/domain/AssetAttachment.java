package com.hexing.asset.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.hexing.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 资产附件对象 asset_attachment
 *
 * @author zxy
 * @date 2022-11-01
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(description= "资产附件")
public class AssetAttachment {
    private static final long serialVersionUID = 1L;

    /** 资产附件id */
    private Long id;

    /** 资产id */
    @ApiModelProperty(name = "资产id")
    private String assetId;

    /** 流程id */
    @ApiModelProperty(name = "流程id")
    private String processId;

    /** 附件地址 */
    @ApiModelProperty(name = "附件地址")
    private String attachmentPath;

    /** 附件名称 */
    @ApiModelProperty(name = "附件名称")
    private String attachmentName;

    /** 类型 */
    @ApiModelProperty(name = "类型")
    private String type;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 备注 */
    private String remark;
}
