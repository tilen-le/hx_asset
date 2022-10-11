package com.hexing.asset.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.hexing.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 流程总对象 asset_process
 *
 * @author zxy
 * @date 2022-09-08
 */
@Data
@Accessors(chain = true)
public class AssetProcess
{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 流程类型 */
    @Excel(name = "流程类型")
    private String processType;

    /** 发起人工号 */
    @Excel(name = "发起人工号")
    private String userCode;

    /** 发起人名称 */
    @Excel(name = "发起人姓名")
    @TableField(exist = false)
    private String userName;

    /** 资产编码 */
    @Excel(name = "资产编码")
    private String assetCode;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 附件信息 */
    @TableField(exist = false)
    private String fileInfo;

}
