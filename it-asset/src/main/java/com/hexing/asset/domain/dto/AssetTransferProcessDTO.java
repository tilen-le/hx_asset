package com.hexing.asset.domain.dto;

import com.hexing.asset.domain.AssetProcess;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资产转移流程DTO
 *
 * @author sty
 * @date 2023-02-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AssetTransferProcessDTO extends AssetProcess {
    /**
     * SAP资产编码
     */
    private String sapAssetCode;
    /**
     * 转出公司
     */
    private String oldCompany;
    /**
     * 转出人
     */
    private String oldEmployee;
    /**
     * 转出岗位
     */
    private String oldEmployeePosition;
    /**
     * 转出时成本中心
     */
    private String oldCostCenter;
    /**
     * 转出时所在位置
     */
    private String oldLocation;
    /**
     * 接收公司
     */
    private String newCompany;
    /**
     * 接收人
     */
    private String newEmployee;
    /**
     * 接收人岗位
     */
    private String newEmployeePosition;
    /**
     * 接收成本中心
     */
    private String newCostCenter;
    /**
     * 接收所在位置
     */
    private String newLocation;
}
