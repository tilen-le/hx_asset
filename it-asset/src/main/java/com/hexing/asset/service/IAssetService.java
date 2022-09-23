package com.hexing.asset.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hexing.asset.domain.Asset;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.common.core.domain.Result;
import com.hexing.common.core.domain.entity.SysUser;

/**
 * 资产表Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetService extends IService<Asset>
{
    /**
     * 根据平台资产编号查询资产
     * @param assetCode
     * @return
     */
    Asset selectAssetByAssetCode(String assetCode);

    /**
     * 查询资产表
     *
     * @param assetId 资产表主键
     * @return 资产表
     */
    public Asset selectAssetByAssetId(String assetId);

    /**
     * 查询资产表列表
     *
     * @param
     * @return 资产表集合
     */
    public List<Asset> selectAssetList(Asset asset);

    /**
     * 根据资产编号查询资产信息
     */
    Result queryAssetCard(Asset asset);

    /**
     * 根据工号查询保管人信息及其名下资产
     *
     * @param
     * @return 人员资产查询
     */
    Result queryPersonInfoAndAssetsByUserCode(JSONObject params);

    /**
     * 资产变更
     *
     * @param params
     * @return
     */
    Result updateAssetExchange(JSONObject params);

    /**
     * 资产转移
     *
     * @param params
     * @return
     */
    Result updateAssetTransfer(JSONObject params);


    /**
     * 新增资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    public int insertAsset(Asset asset);

    /**
     * 修改资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    public int updateAsset(Asset asset);

    /**
     * 修改资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    public int updateAssetByAssetCode(Asset asset);

    /**
     * 删除资产表信息
     *
     * @param assetId 资产表主键
     * @return 结果
     */
    public int deleteAssetByAssetId(Long assetId);

    /**
     * 根据平台资产编号删除资产信息
     */
    int deleteAssetByAssetCodes(List<String> assetCodes);

    /**
     * 资产信息导入
     *
     * @param assetList 资产信息列表
     * @param isUpdateSupport 是否存在则覆盖
     * @param operName 操作人姓名
     * @return
     */
    String importAsset(List<Asset> assetList, Boolean isUpdateSupport, String operName);

}
