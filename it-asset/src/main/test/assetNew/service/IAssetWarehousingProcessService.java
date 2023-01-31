package com.hexing.assetNew.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.assetNew.domain.AssetWarehousingProcessDomain;

/**
 * 资产入库流程Controller
 */
public interface IAssetWarehousingProcessService extends IService<AssetWarehousingProcessDomain> {

    /**
     * 新增入库流程记录
     * @param domain
     * @return
     */
    int add(AssetWarehousingProcessDomain domain);

    /**
     * 解析SAP请求体生成入库流程Domain对象
     *
     * @param sapRequestBody
     * @return
     */
    AssetWarehousingProcessDomain prepareWarehousingProcessDomain(String sapRequestBody);

    /**
     * 资产条目生成接口
     * @param var 入库单对象
     * @return 成功状态
     */
    String generateAsset(AssetWarehousingProcessDomain var);


}
