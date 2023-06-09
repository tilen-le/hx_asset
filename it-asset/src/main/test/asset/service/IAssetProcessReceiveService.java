package com.hexing.assetNew.service;

import java.util.List;
import com.hexing.assetNew.domain.AssetProcessReceive;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产领用流程Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetProcessReceiveService extends IService<AssetProcessReceive>
{
    /**
     * 查询资产领用流程
     *
     * @param id 资产领用流程主键
     * @return 资产领用流程
     */
    public AssetProcessReceive selectAssetProcessReceiveById(Long id);

    /**
     * 查询资产领用流程列表
     *
     * @param assetProcessReceive 资产领用流程
     * @return 资产领用流程集合
     */
    public List<AssetProcessReceive> selectAssetProcessReceiveList(AssetProcessReceive assetProcessReceive);

    /**
     * 新增资产领用流程
     *
     * @param assetProcessReceive 资产领用流程
     * @return 结果
     */
    public int insertAssetProcessReceive(AssetProcessReceive assetProcessReceive);

    /**
     * 修改资产领用流程
     *
     * @param assetProcessReceive 资产领用流程
     * @return 结果
     */
    public int updateAssetProcessReceive(AssetProcessReceive assetProcessReceive);

    /**
     * 批量删除资产领用流程
     *
     * @param ids 需要删除的资产领用流程主键集合
     * @return 结果
     */
    public int deleteAssetProcessReceiveByIds(Long[] ids);

    /**
     * 删除资产领用流程信息
     *
     * @param id 资产领用流程主键
     * @return 结果
     */
    public int deleteAssetProcessReceiveById(Long id);

    /**
     * 新增领用流程记录
     *
     * @param instanceId 实例ID
     * @param userCode 发起人工号
     * @param assetCode 平台资产编号
     * @param type 流程类型
     */
    int saveProcess(String instanceId, String userCode, String assetCode, String type);
}
