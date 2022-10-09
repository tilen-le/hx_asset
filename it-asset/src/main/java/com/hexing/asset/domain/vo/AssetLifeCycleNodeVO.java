package com.hexing.asset.domain.vo;

import com.hexing.asset.domain.AssetProcess;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AssetLifeCycleNodeVO extends AssetProcess {
    /** 发起人名称 */
    private String userName;
    /** 附件信息 */
    private String fileInfo;
}
