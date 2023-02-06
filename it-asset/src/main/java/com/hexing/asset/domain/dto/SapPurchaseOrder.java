package com.hexing.asset.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * SAP采购订单DTO类
 *
 * @author sty
 * @date 2023-01-31
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class SapPurchaseOrder {

    /**
     * 公司代码 BUKRS
     */
    @JsonProperty("BUKRS")
    private String companyCode;

    /**
     * 公司代码名称 BUTXT
     */
    @JsonProperty("BUTXT")
    private String companyCodeName;

    /**
     * 采购订单 EBELN
     */
    @JsonProperty("EBELN")
    private String purchaseOrder;

    /**
     * 下单日期 AEDAT
     */
    @JsonProperty("AEDAT")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    private String orderDate;

    /**
     * 供应商 LIFNR
     */
    @JsonProperty("LIFNR")
    private String provider;

    /**
     * 供应商描述 NAME1
     */
    @JsonProperty("NAME1")
    private String providerDescription;

    /**
     * 物料号 MATNR
     */
    @JsonProperty("MATNR")
    private String materialNumber;

    /**
     * 物料文本（资产名称） TXZ01
     */
    @JsonProperty("TXZ01")
    private String materialText;

    /**
     * 数量 MENGE
     */
    @JsonProperty("MENGE")
    private Integer amount;

    /**
     * 单位 MEINS
     */
    @JsonProperty("MEINS")
    private String unit;

    /**
     * 价格 NETWR
     */
    @JsonProperty("NETWR")
    private Double price;

    /**
     * 价格单位 PEINH
     */
    @JsonProperty("PEINH")
    private String priceUnit;

    /**
     * 币种 WAERS
     */
    @JsonProperty("WAERS")
    private String moneyType;

    /**
     * 在建资产号（SAP资产编号） ANLN1
     */
    @JsonProperty("ANLN1")
    private String assetSapCode;

    /**
     * 到货日期 ZDHRQ
     */
    @JsonProperty("ZDHRQ")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    private String arrivalDate;

    /**
     * 到货数量 ZDHSL
     */
    @JsonProperty("ZDHSL")
    private Double numberOfArrival;

    /**
     * 物料凭证 MBLNR
     */
    @JsonProperty("MBLNR")
    private String proofOfMaterial;

}
