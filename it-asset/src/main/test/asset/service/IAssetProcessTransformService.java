package com.hexing.assetNew.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hexing.assetNew.domain.AssetProcessTransform;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产改造流程Service接口
 *
 * @author zxy
 * @date 2022-09-20
 */
public interface IAssetProcessTransformService extends IService<AssetProcessTransform>
{
    /**
     * 查询资产改造流程
     *
     * @param id 资产改造流程主键
     * @return 资产改造流程
     */
    public AssetProcessTransform selectAssetProcessTransformById(Long id);

    /**
     * 查询资产改造流程列表
     *
     * @param assetProcessTransform 资产改造流程
     * @return 资产改造流程集合
     */
    public List<AssetProcessTransform> selectAssetProcessTransformList(AssetProcessTransform assetProcessTransform);

    /**
     * 新增资产改造流程
     *
     * @param assetProcessTransform 资产改造流程
     * @return 结果
     */
    public int insertAssetProcessTransform(AssetProcessTransform assetProcessTransform);

    /**
     * 修改资产改造流程
     *
     * @param assetProcessTransform 资产改造流程
     * @return 结果
     */
    public int updateAssetProcessTransform(AssetProcessTransform assetProcessTransform);

    /**
     * 批量删除资产改造流程
     *
     * @param ids 需要删除的资产改造流程主键集合
     * @return 结果
     */
    public int deleteAssetProcessTransformByIds(Long[] ids);

    /**
     * 删除资产改造流程信息
     *
     * @param id 资产改造流程主键
     * @return 结果
     */
    public int deleteAssetProcessTransformById(Long id);

    public int transformAssets(JSONObject param);
}
