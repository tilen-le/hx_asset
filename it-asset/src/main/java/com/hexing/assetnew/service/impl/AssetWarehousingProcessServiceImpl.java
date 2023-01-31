package com.hexing.assetnew.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.assetnew.domain.AssetWarehousingProcessDomain;
import com.hexing.assetnew.mapper.AssetWarehousingProcessMapper;
import com.hexing.assetnew.service.IAssetWarehousingProcessService;
import org.springframework.stereotype.Service;

/**
 * 资产入库流程Controller
 */
@Service
public class AssetWarehousingProcessServiceImpl extends ServiceImpl<AssetWarehousingProcessMapper, AssetWarehousingProcessDomain> implements IAssetWarehousingProcessService {

    /**
     * 新增入库流程记录
     *
     * @param domain
     * @return
     */
    public int add(AssetWarehousingProcessDomain domain) {
        return 0;
    }

    /**
     * 解析SAP请求体生成入库流程Domain对象
     *
     * @param sapRequestBody
     * @return
     */
    public AssetWarehousingProcessDomain prepareWarehousingProcessDomain(String sapRequestBody) {
        return null;
    }

    /**
     * 资产条目生成接口
     *
     * @param var 入库单对象
     * @return 成功状态
     */
    public String generateAsset(AssetWarehousingProcessDomain var) {
        return null;
    }


}
