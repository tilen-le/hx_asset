package com.hexing.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.Asset;
import com.hexing.common.core.domain.Result;

import java.util.List;

/**
 * 资产表Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetService extends IService<Asset> {
    /**
     * 根据平台资产编号查询资产
     *
     * @param assetCode
     * @return
     */
    Asset selectAssetByAssetCode(String assetCode);

    /**
     * 查询资产表列表
     *
     * @param
     * @return 资产表集合
     */
    List<Asset> selectAssetList(Asset asset);

    /**
     * 根据资产编号查询资产信息
     */
    Result queryAssetCard(Asset asset);

    /**
     * 新增资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    int insertAsset(Asset asset);

    /**
     * 修改资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    int updateAsset(Asset asset, String processId);

    /**
     * 根据平台资产编号删除资产信息
     */
    int deleteAssetByAssetCodes(List<String> assetCodes);

    /**
     * 根据部门ID查询部门下所有员工保管的资产
     */
    List<Asset> selectAssetListByDeptId(Long deptId);

    /**
     * 根据工号查询员工资产
     *
     * @param userCodeList
     * @return
     */
    List<Asset> selectAssetByUserName(List<String> userCodeList);

    /**
     * 查询部门下所有员工名下的资产
     */
    List<Asset> selectAssetByDeptId(Long deptId);
}
