package com.hexing.assetNew.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 资产改造流程对象 asset_process_transform
 *
 * @author zxy
 * @date 2022-09-20
 */
@Data
@Accessors(chain = true)
public class AssetProcessTransform
{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 流程总表id */
    private Long processId;

    /** 实例ID */
    @Excel(name = "实例ID")
    private String instanceId;

    /** 发起人工号 */
    @Excel(name = "发起人工号")
    private String userCode;

    /** 发起人名称 */
    @Excel(name = "发起人姓名")
    private String userName;

    /** 平台资产编码 */
    @Excel(name = "平台资产编码")
    private String assetCode;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 文件信息 */
    private String fileInfo;

    /** 补充文件信息 */
    private String fileInfoAdd;

    /** 状态 */
    @Excel(name = "状态")
    private String status;
}
