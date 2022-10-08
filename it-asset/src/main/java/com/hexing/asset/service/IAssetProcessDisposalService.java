package com.hexing.asset.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hexing.asset.domain.AssetProcessDisposal;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产处置流程Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetProcessDisposalService extends IService<AssetProcessDisposal>
{
    /**
     * 查询资产处置流程
     *
     * @param id 资产处置流程主键
     * @return 资产处置流程
     */
    public AssetProcessDisposal selectAssetProcessDisposalById(Long id);

    /**
     * 查询资产处置流程列表
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 资产处置流程集合
     */
    public List<AssetProcessDisposal> selectAssetProcessDisposalList(AssetProcessDisposal assetProcessDisposal);

    /**
     * 新增资产处置流程
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 结果
     */
    public int insertAssetProcessDisposal(AssetProcessDisposal assetProcessDisposal);

    public int downLoadFile(AssetProcessDisposal assetProcessDisposal);

    /**
     * 修改资产处置流程
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 结果
     */
    public int updateAssetProcessDisposal(AssetProcessDisposal assetProcessDisposal);

    /**
     * 批量删除资产处置流程
     *
     * @param ids 需要删除的资产处置流程主键集合
     * @return 结果
     */
    public int deleteAssetProcessDisposalByIds(Long[] ids);

    /**
     * 删除资产处置流程信息
     *
     * @param id 资产处置流程主键
     * @return 结果
     */
    public int deleteAssetProcessDisposalById(Long id);


    public int disposalAssets(JSONObject param);
}
