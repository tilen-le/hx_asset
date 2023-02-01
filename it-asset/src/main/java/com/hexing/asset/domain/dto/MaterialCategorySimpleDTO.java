package com.hexing.asset.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资产类型DTO
 */
@Data
@EqualsAndHashCode
public class MaterialCategorySimpleDTO {
    /**
     * 资产类型
     */
    private String assetType;

    /**
     * 资产分类
     */
    private String assetCategory;

    /**
     * 资产大类
     */
    private String assetSubCategory;

}
