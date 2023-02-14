package com.hexing.asset.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.coyote.http11.filters.IdentityInputFilter;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
@ApiModel(value = "AssetQueryParam")
public class AssetQueryParam {
    /**
     * 资产编码
     */
    @ApiModelProperty(value="资产编码")
    private String assetCode;
    /**
     * 资产大类
     */
    @ApiModelProperty(value="资产大类")
    private String assetType;
    /**
     * 资产中类
     */
    @ApiModelProperty(value="资产中类")
    private String assetCategory;
    /**
     * 资产小类
     */
    @ApiModelProperty(value="资产小类")
    private List<String> assetSubCategory;
    /**
     * 资产名称
     */
    @ApiModelProperty(value="资产名称")
    private String assetName;
    /**
     * 资产状态
     */
    @ApiModelProperty(value="资产状态")
    private List<String> assetStatus;
    /**
     * 资产保管部门
     */
    @ApiModelProperty(value="资产保管部门")
    private String responsiblePersonDept;
    /**
     * 所属公司
     */
    @ApiModelProperty(value="所属公司")
    private List<String> company;
    /**
     * 转固状态
     */
    @ApiModelProperty(value="转固状态")
    private List<String> fixed;
    /**
     * 资产化日期开始时间
     */
    @ApiModelProperty(value="资产化日期开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date capitalizationStartDate;
    /**
     * 资产化日期结束时间
     */
    @ApiModelProperty(value="资产化日期结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date capitalizationEndDate;
    /**
     * 创建时间开始
     */
    @ApiModelProperty(value="创建时间开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeBegin;
    /**
     * 创建时间结束
     */
    @ApiModelProperty(value="创建时间结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

}
