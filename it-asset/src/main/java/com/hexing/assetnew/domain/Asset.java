package com.hexing.assetnew.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 资产表对象 asset
 *
 * @author zxy
 * @date 2022-09-08
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class Asset {

    private static final long serialVersionUID = 1L;

    /**
     * 资产id
     */
    @TableId(type = IdType.AUTO)
    private String assetId;

    /**
     * 固定资产名称
     */
    @Excel(name = "*固定资产名称")
    private String assetName;

    /**
     * 平台资产编号
     */
    @Excel(name = "平台资产编号", type = Excel.Type.EXPORT)
    private String assetCode;

    /**
     * 保管人工号（老工号）
     */
    @Excel(name = "*保管人工号")
    private String responsiblePersonCode;

    /**
     * 保管人姓名
     */
    @Excel(name = "保管人姓名", type = Excel.Type.EXPORT)
    private String responsiblePersonName;

    /**
     * 保管部门
     */
    @TableField(exist = false)
    private String responsiblePersonDept;

    /**
     * 资产状态描述
     */
    @Excel(name = "*资产状态描述")
    private String assetStatus;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

}
