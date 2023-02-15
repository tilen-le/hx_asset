package com.hexing.asset.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资产派发 VO
 *
 * @author sty
 * @date 2023-02-14
 */
@Data
@EqualsAndHashCode
public class AssetReceiveVO {
    /**
     * 领用人
     */
    private String Rname;
    /**
     * 领用部门
     */
    private String Post;
    /**
     * 所在位置
     */
    private String Stage;
    /**
     * SAP资产编码
     */
    private String Anln1;
    /**
     * 出厂编码
     */
    private String Znum;
    /**
     * 公司代码
     */
    private String BUKRS;
}
