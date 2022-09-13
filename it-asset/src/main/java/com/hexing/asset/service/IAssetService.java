package com.hexing.asset.service;

import java.util.List;

import cn.hutool.json.JSONObject;
import com.hexing.asset.domain.Asset;
import com.baomidou.mybatisplus.extension.service.IService;
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
     * 查询资产表
     *
     * @param assetId 资产表主键
     * @return 资产表
     */
    public Asset selectAssetByAssetId(Long assetId);

    /**
     * 查询资产表列表
     *
     * @param
     * @return 资产表集合
     */
    public List<Asset> selectAssetList();

    /**
     * 查询资产表列表通过管理部门和资产code
     *
     * @param
     * @return 资产表集合
     */
    String getAssetsByAssetCodes(JSONObject params);

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
     * 删除资产表信息
     *
     * @param assetId 资产表主键
     * @return 结果
     */
    public int deleteAssetByAssetId(Long assetId);

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
