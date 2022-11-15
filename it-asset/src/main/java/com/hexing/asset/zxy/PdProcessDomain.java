package com.hexing.asset.zxy;

import com.hexing.asset.domain.AssetsProcess;

import java.io.Serializable;

/**
 * 盘点字段表
 * @author 80010641
 */
public class PdProcessDomain extends AssetsProcess implements Serializable {


    //除了assetsprocess中的其余独立字段

    //至于关联字段的信息展示
    private Object assetCode;


}
