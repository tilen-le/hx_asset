package com.hexing.asset.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.hexing.common.core.domain.BaseEntity;

/**
 * 流程字段对象 asset_process_field
 *
 * @author zxy
 * @date 2022-11-01
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(description= "流程字段类")
public class AssetProcessField {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "流程类型编号")
    @Excel(name = "流程类型编号")
    private String processType;

    @ApiModelProperty(value = "字段键名")
    @Excel(name = "字段键名")
    private String fieldKey;

    @ApiModelProperty(value = "字段标签")
    @Excel(name = "字段标签")
    private String fieldLabel;

    @ApiModelProperty(value = "状态（0：启用，1：禁用）")
    @Excel(name = "状态", readConverterExp="0：启用，1：禁用")
    private String status;

    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    private String remark;

    /** 显示/隐藏 */
    @ApiModelProperty(value = "显示/隐藏")
    private String visible;

    /** 字典 */
    @ApiModelProperty(value = "字典")
    private String dictType;

    /** 时间戳格式 */
    @ApiModelProperty(value = "时间戳格式")
    private String timeFormat;

    /** 是否查询 */
    @ApiModelProperty(value = "是否查询")
    private String queryable;

    /** 是否可编辑 */
    @ApiModelProperty(value = "是否可编辑")
    private String editable;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    private String orderNum;

    /** 列宽 */
    @ApiModelProperty(value = "列宽")
    private String width;

    /** 列宽 */
    @ApiModelProperty(value = "是否存储")
    private String isSave;

    /** 列宽 */
    @ApiModelProperty(value = "是否多选")
    private String isMulti;


}
