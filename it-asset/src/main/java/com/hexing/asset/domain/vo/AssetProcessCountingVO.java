package com.hexing.asset.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AssetProcessCountingVO {

    /** 盘点人工号 */
    private String userCode;

    /** 盘点人 */
    private String userNickName;

    /** 公司代码描述 */
    private String companyName;

    /** 平台资产编号 */
    private String assetCode;

    /** 固定资产名称 */
    private String assetName;

    /** 出厂编号 */
    private String factoryNo;

    /** 规格型号 */
    private String standard;

    /** 资产使用场景 */
    private String usageScenario;

    /** 管理部门描述 */
    private String manageDept;

    /** 保管人姓名 */
    private String responsiblePersonName;

    /** 保管人工号（老工号） */
    private String responsiblePersonCode;

    /** 保管部门 */
    private String responsiblePersonDept;

    /** 存放地点 */
    private String location;
}
