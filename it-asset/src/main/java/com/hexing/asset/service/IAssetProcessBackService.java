package com.hexing.asset.service;

import java.util.List;
import com.hexing.asset.domain.AssetProcessBack;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产归还流程Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetProcessBackService extends IService<AssetProcessBack>
{
    /**
     * 查询资产归还流程
     *
     * @param id 资产归还流程主键
     * @return 资产归还流程
     */
    public AssetProcessBack selectAssetProcessBackById(Long id);

    /**
     * 查询资产归还流程列表
     *
     * @param assetProcessBack 资产归还流程
     * @return 资产归还流程集合
     */
    public List<AssetProcessBack> selectAssetProcessBackList(AssetProcessBack assetProcessBack);

    /**
     * 新增资产归还流程
     *
     * @param assetProcessBack 资产归还流程
     * @return 结果
     */
    public int insertAssetProcessBack(AssetProcessBack assetProcessBack);

    /**
     * 修改资产归还流程
     *
     * @param assetProcessBack 资产归还流程
     * @return 结果
     */
    public int updateAssetProcessBack(AssetProcessBack assetProcessBack);

    /**
     * 批量删除资产归还流程
     *
     * @param ids 需要删除的资产归还流程主键集合
     * @return 结果
     */
    public int deleteAssetProcessBackByIds(Long[] ids);

    /**
     * 删除资产归还流程信息
     *
     * @param id 资产归还流程主键
     * @return 结果
     */
    public int deleteAssetProcessBackById(Long id);

    /**
     * 新增归还流程记录
     *
     * @param instanceId 实例ID
     * @param userCode 发起人工号
     * @param assetCode 平台资产编号
     * @param type 流程类型
     */
    int saveProcess(String instanceId, String userCode, String assetCode, String type);
}
