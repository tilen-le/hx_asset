package com.hexing.asset.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * SAP价值传输DTO类
 *
 * @author sty
 * @date 2023-02-06
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class SapValueDTO {
    /**
     * 公司代码 BUKRS
     */
    @JsonProperty("BUKRS")
    private String companyCode;
    /**
     * SAP资产号码 ANLN1
     */
    @JsonProperty("ANLN1")
    private String sapCode;
    /**
     * 平台资产号码 ZNUM
     */
    @JsonProperty("ZNUM")
    private String assetCode;
    /**
     * 年度 YEAR
     */
    @JsonProperty("YEAR")
    private String year;
    /**
     * 期间 MONTH
     */
    @JsonProperty("MONTH")
    private String month;
    /**
     * 原值 ZYZ
     */
    @JsonProperty("ZYZ")
    private String originalValue;
    /**
     * 折旧 ZZJ
     */
    @JsonProperty("ZZJ")
    private String depreciation;
    /**
     * 净值 ZJZ
     */
    @JsonProperty("ZJZ")
    private String netValue;
    /**
     * 币种 WAERS
     */
    @JsonProperty("WAERS")
    private String moneyType;
    /**
     * 资产使用年限 NDJAR
     * 对应资产信息的预计使用寿命（年）字段
     */
    @JsonProperty("NDJAR")
    private String canUseYears;
    /**
     * 资产使用月份 NDPER
     * 对应资产信息的预计使用寿命（月）字段
     */
    @JsonProperty("NDPER")
    private String canUseMonths;
    /**
     * 资本化日期 AKTIV
     */
    @JsonProperty("AKTIV")
    @JsonFormat(pattern = "yyyyMMdd")
    private Date capitalizationDate;
    /**
     * 是否更新成功
     */
    private Boolean success;
    /**
     * 错误原因
     */
    private String reason;
}
