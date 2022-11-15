package com.hexing.asset.zxy;

import com.hexing.asset.domain.AssetsProcess;
import lombok.Data;

import java.io.Serializable;

/**
 * 盘点字段表
 * @author 80010641
 */
@Data
public class PdProcessDomain extends AssetsProcess implements Serializable {


    //除了assetsprocess中的其余独立字段

    private String taskCode;

    private String userCode;
}
