package com.hexing.asset.service;

import java.util.List;
import com.hexing.asset.domain.AssetProcessCounting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 资产盘点流程Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetProcessCountingService extends IService<AssetProcessCounting>
{
    /**
     * 查询资产盘点流程
     *
     * @param id 资产盘点流程主键
     * @return 资产盘点流程
     */
    public AssetProcessCounting selectAssetProcessCountingById(Long id);

    /**
     * 查询资产盘点流程列表
     *
     * @param assetProcessCounting 资产盘点流程
     * @return 资产盘点流程集合
     */
    public List<AssetProcessCounting> selectAssetProcessCountingList(AssetProcessCounting assetProcessCounting);

    /**
     * 新增资产盘点流程
     *
     * @param assetProcessCounting 资产盘点流程
     * @return 结果
     */
    public int insertAssetProcessCounting(AssetProcessCounting vo);

    /**
     * 修改资产盘点流程
     *
     * @param assetProcessCounting 资产盘点流程
     * @return 结果
     */
    public int updateAssetProcessCounting(AssetProcessCounting assetProcessCounting);

    /**
     * 批量删除资产盘点流程
     *
     * @param ids 需要删除的资产盘点流程主键集合
     * @return 结果
     */
    public int deleteAssetProcessCountingByIds(Long[] ids);

    /**
     * 删除资产盘点流程信息
     *
     * @param id 资产盘点流程主键
     * @return 结果
     */
    public int deleteAssetProcessCountingById(Long id);
}
