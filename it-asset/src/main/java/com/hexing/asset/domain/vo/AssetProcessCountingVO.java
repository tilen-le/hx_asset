package com.hexing.asset.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hexing.common.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class AssetProcessCountingVO {

    /** 盘点人工号 */
    @Excel(name = "盘点人工号")
    private String userCode;

    /** 盘点人 */
    @Excel(name = "盘点人名称")
    private String userName;

    /** 公司代码描述 */
    @Excel(name = "公司代码描述")
    private String companyName;

    /** 平台资产编号 */
    @Excel(name = "平台资产编号")
    private String assetCode;

    /** 固定资产名称 */
    @Excel(name = "固定资产名称")
    private String assetName;

    /** 出厂编号 */
    @Excel(name = "出厂编号")
    private String factoryNo;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String standard;

    /** 资产使用场景 */
    @Excel(name = "资产使用场景")
    private String usageScenario;

    /** 管理部门描述 */
    @Excel(name = "管理部门描述")
    private String manageDept;

    /** 保管人姓名 */
    @Excel(name = "保管人姓名")
    private String responsiblePersonName;

    /** 保管人工号（老工号） */
    @Excel(name = "保管人工号（老工号）")
    private String responsiblePersonCode;

    /** 保管部门 */
    @Excel(name = "保管部门")
    private String responsiblePersonDept;

    /** 存放地点 */
    @Excel(name = "存放地点")
    private String location;

    /** 盘点时间 */
    @Excel(name = "盘点时间")
    private Date countingTime;

    /** 盘点状态 */
    @Excel(name = "盘点状态")
    private String countingStatus;

    /** 备注 */
    @Excel(name = "备注")
    private String comment;


}
