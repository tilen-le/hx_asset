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
     * 资产大类
     */
    private String assetType;

    /**
     * 资产中类
     */
    private String assetCategory;

    /**
     * 资产小类
     */
    private String assetSubCategory;

}
