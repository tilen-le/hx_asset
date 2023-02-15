package com.hexing.asset.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 资产转移 DTO
 *
 * @author sty
 * @date 2023-02-13
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class SapAssetTransferDTO {
    /**
     * 资产编码
     */
    private String ZNUM;
    /**
     * 原始公司代码
     */
    private String ZBUKRS;
    /**
     * 接收公司代码
     */
    private String BUKRS;
    /**
     * 接收人
     */
    private String Rname;
    /**
     * 接收人岗位
     */
    private String Post;
    /**
     * 成本中心编码
     */
    private String KOSTL;
    /**
     * 所在位置
     */
    private String Stage;
    /**
     * SAP资产编码
     */
    private String Anln1;
}
