package com.hexing.asset.domain.vo;

import com.hexing.asset.domain.AssetProcess;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "生命周期VO类", description = "生命周期VO类")
public class AssetLifeCycleVO {
    /** 资产流程列表 */
    @ApiModelProperty(value="资产流程列表", name="assetProcessList")
    List<AssetProcess> assetProcessList;
    /** 维修次数 */
    @ApiModelProperty(value="维修次数", name="maintainCount")
    private Long maintainCount;
    /** 改造次数 */
    @ApiModelProperty(value="改造次数", name="transformCount")
    private Long transformCount;
    /** 外卖次数 */
    @ApiModelProperty(value="外卖次数", name="sellOutCount")
    private Long sellOutCount;
}
