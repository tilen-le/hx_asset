package com.hexing.asset.service;

import java.util.List;
import com.hexing.asset.domain.AssetProcessTransfer;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产转移流程Service接口
 *
 * @author zxy
 * @date 2022-09-20
 */
public interface IAssetProcessTransferService extends IService<AssetProcessTransfer>
{
    /**
     * 查询资产转移流程
     *
     * @param id 资产转移流程主键
     * @return 资产转移流程
     */
    public AssetProcessTransfer selectAssetProcessTransferById(Long id);

    /**
     * 查询资产转移流程列表
     *
     * @param assetProcessTransfer 资产转移流程
     * @return 资产转移流程集合
     */
    public List<AssetProcessTransfer> selectAssetProcessTransferList(AssetProcessTransfer assetProcessTransfer);

    /**
     * 新增资产转移流程
     *
     * @param assetProcessTransfer 资产转移流程
     * @return 结果
     */
    public int insertAssetProcessTransfer(AssetProcessTransfer assetProcessTransfer);

    /**
     * 修改资产转移流程
     *
     * @param assetProcessTransfer 资产转移流程
     * @return 结果
     */
    public int updateAssetProcessTransfer(AssetProcessTransfer assetProcessTransfer);

    /**
     * 批量删除资产转移流程
     *
     * @param ids 需要删除的资产转移流程主键集合
     * @return 结果
     */
    public int deleteAssetProcessTransferByIds(Long[] ids);

    /**
     * 删除资产转移流程信息
     *
     * @param id 资产转移流程主键
     * @return 结果
     */
    public int deleteAssetProcessTransferById(Long id);

    /**
     * 新增转移流程记录
     *
     * @param instanceId 实例ID
     * @param userCode 发起人工号
     * @param assetCode 平台资产编号
     */
    void saveProcess(String instanceId, String userCode, String assetCode);
}
