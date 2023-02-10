package com.hexing.asset.service;

import java.util.List;
import com.hexing.asset.domain.AssetCustodyLog;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产保管记录Service接口
 *
 * @author zxy
 * @date 2023-02-10
 */
public interface IAssetCustodyLogService extends IService<AssetCustodyLog>
{
    /**
     * 查询资产保管记录
     *
     * @param id 资产保管记录主键
     * @return 资产保管记录
     */
    public AssetCustodyLog selectAssetCustodyLogById(Long id);

    /**
     * 查询资产保管记录列表
     *
     * @param assetCustodyLog 资产保管记录
     * @return 资产保管记录集合
     */
    public List<AssetCustodyLog> selectAssetCustodyLogList(AssetCustodyLog assetCustodyLog);

    /**
     * 新增资产保管记录
     *
     * @param assetCustodyLog 资产保管记录
     * @return 结果
     */
    public int insertAssetCustodyLog(AssetCustodyLog assetCustodyLog);

}
