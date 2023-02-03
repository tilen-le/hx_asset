package com.hexing.asset.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 资产管理配置对象 asset_management_config
 *
 * @author zxy
 * @date 2023-01-30
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class AssetManagementConfigSearchDTO
{
    private static final long serialVersionUID = 1L;
    /** 所属公司 */
    private List<String> company;
    /** 资产大类列表 */
    private List<String> assetType;
    /** 资产中类列表 */
    private List<String> assetCategory;
    /** 资产小类列表 */
    private List<String> assetSubCategory;
    /** 资产管理员列表 */
    private List<String> assetManager;
}
