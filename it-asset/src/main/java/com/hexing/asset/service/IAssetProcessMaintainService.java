package com.hexing.asset.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hexing.asset.domain.AssetProcessMaintain;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产维修流程Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetProcessMaintainService extends IService<AssetProcessMaintain>
{
    /**
     * 查询资产维修流程
     *
     * @param id 资产维修流程主键
     * @return 资产维修流程
     */
    public AssetProcessMaintain selectAssetProcessMaintainById(Long id);

    /**
     * 查询资产维修流程列表
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 资产维修流程集合
     */
    public List<AssetProcessMaintain> selectAssetProcessMaintainList(AssetProcessMaintain assetProcessMaintain);

    /**
     * 新增资产维修流程
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 结果
     */
    public int insertAssetProcessMaintain(AssetProcessMaintain assetProcessMaintain);

    /**
     * 修改资产维修流程
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 结果
     */
    public int updateAssetProcessMaintain(AssetProcessMaintain assetProcessMaintain);

    /**
     * 批量删除资产维修流程
     *
     * @param ids 需要删除的资产维修流程主键集合
     * @return 结果
     */
    public int deleteAssetProcessMaintainByIds(Long[] ids);

    /**
     * 删除资产维修流程信息
     *
     * @param id 资产维修流程主键
     * @return 结果
     */
    public int deleteAssetProcessMaintainById(Long id);


    public int maintainAssets(JSONObject param);
}
