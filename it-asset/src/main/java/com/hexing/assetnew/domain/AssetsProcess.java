package com.hexing.assetnew.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 资产流程对象 assets_process
 *
 * @author zxy
 * @date 2022-11-04
 */
@Data
@EqualsAndHashCode
public class AssetsProcess {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 流程类型编号
     */
    @Excel(name = "流程类型编号")
    private String processType;

    /**
     * 资产编号
     */
    @Excel(name = "资产编号")
    private String assetCode;

    /**
     * 流程状态
     */
    @Excel(name = "流程状态")
    private String status;

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
     * 备注
     */
    private String remark;

    /**
     * 字段值列表
     */
    @TableField(exist = false)
    private List<AssetProcessVariable> variableList;

}
