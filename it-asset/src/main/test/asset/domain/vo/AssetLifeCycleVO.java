package com.hexing.assetNew.domain.vo;

import com.hexing.assetNew.domain.AssetProcess;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "AssetLifeCycleVO")
public class AssetLifeCycleVO {
    /** 资产流程列表 */
    @ApiModelProperty(value="资产流程列表")
    private List<AssetProcess> assetProcessList;
    /** 资产状态 */
    @ApiModelProperty(value="资产状态")
    private String assetStatus;
    /** 维修次数 */
    @ApiModelProperty(value="维修次数")
    private Long maintainCount;
    /** 改造次数 */
    @ApiModelProperty(value="改造次数")
    private Long transformCount;
    /** 外卖次数 */
    @ApiModelProperty(value="外卖次数")
    private Long sellOutCount;
}
