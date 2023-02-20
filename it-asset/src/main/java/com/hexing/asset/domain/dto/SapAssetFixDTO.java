package com.hexing.asset.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 资产转固 DTO
 *
 * @author sty
 * @date 2023-01-31
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class SapAssetFixDTO {
    /**
     * 公司代码
     */
    private String BUKRS;
    /**
     * SAP卡片资产分类
     */
    private String ANLKL;
    /**
     * 资产名称
     */
    private String TXT50;
    /**
     * 规格型号
     */
    private String TXA50;
    /**
     * 归属项目
     */
    private String ANLHTXT;
    /**
     * 序列号（物料号）
     */
    private String SERNR;
    /**
     * 存货号
     */
    private String INVNR;
    /**
     * 数量
     */
    private Double MENGE;
    /**
     * 单位
     */
    private String MEINS;
    /**
     * 资产存放地点
     */
    private String INVZU;
    /**
     * 用途
     */
    private String ZYONGTU;
    /**
     * 资产使用场景
     */
    private String ZZANLU003;
    /**
     * 保管人工号
     */
    private String ZZANLU004;
    /**
     * 成本中心编码
     */
    private String KOSTL;
    /**
     * 成本中心描述
     */
    private String LTEXT;
    /**
     * 责任成本中心编码
     */
    private String KOSTLV;
    /**
     * 保管人
     */
    private String RAUMN;
    /**
     * 供应商编码
     */
    private String LIFNR;
    /**
     * 供应商名称
     */
    private String NAME1;
    /**
     * 制造商名称
     */
    private String NAME2;
    /**
     * 制造商名称
     */
    private String DEPS;

//    /**
//     * 资产管理分类
//     */
//    private String ORD42;

}
