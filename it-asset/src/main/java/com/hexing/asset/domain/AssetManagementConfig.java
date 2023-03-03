package com.hexing.asset.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 资产管理配置对象 asset_management_config
 *
 * @author zxy
 * @date 2023-01-30
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class AssetManagementConfig
{
    private static final long serialVersionUID = 1L;

    /** 资产管理配置人员id */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "资产管理配置人员id")
    private Long id;

    /** 资产大类 */
    @Excel(name = "资产大类")
    @ApiModelProperty(value = "资产大类")
    private String assetType;

    /** 资产中类 */
    @Excel(name = "资产中类")
    @ApiModelProperty(value = "资产中类")
    private String assetCategory;

    /** 资产小类 */
    @Excel(name = "资产小类")
    @ApiModelProperty(value = "资产小类")
    private String assetSubCategory;

    /** 所属公司 */
    @Excel(name = "所属公司")
    @ApiModelProperty(value = "所属公司")
    private String company;

    /** 资产管理员 */
    @Excel(name = "资产管理员")
    @ApiModelProperty(value = "资产管理员")
    private String assetManager;

    /** 账务管理员 */
    @Excel(name = "账务管理员")
    @ApiModelProperty(value = "账务管理员")
    private String financialManager;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 发起人 */
    @Excel(name = "创建人")
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /** 修改人 */
    @Excel(name = "修改人")
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    /** 资产管理部门 */
    @Excel(name = "资产管理部门")
    @ApiModelProperty(value = "资产管理部门")
    private String assetManageDept;

    /** 财务管理部门 */
    @Excel(name = "财务管理部门")
    @TableField(exist = false)
    @ApiModelProperty(value = "财务管理部门")
    private String financialManageDept;
}
