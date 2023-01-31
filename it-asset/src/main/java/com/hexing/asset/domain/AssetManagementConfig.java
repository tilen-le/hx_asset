package com.hexing.asset.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

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
    private Long id;

    /** 资产类型 */
    @Excel(name = "资产类型")
    private String assetType;

    /** 资产分类 */
    @Excel(name = "资产分类")
    private String assetCategory;

    /** 归属主体 */
    @Excel(name = "归属主体")
    private String company;

    /** 地区 */
    @Excel(name = "地区")
    private String location;

    /** 资产管理部门 */
    @Excel(name = "资产管理部门")
    private String manageDept;

    /** 资产管理员 */
    @Excel(name = "资产管理员")
    private String assetManager;

    /** 账务管理员 */
    @Excel(name = "账务管理员")
    private String financialManager;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 发起人 */
    @Excel(name = "创建人")
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 修改人 */
    @Excel(name = "修改人")
    private String updateBy;
}
