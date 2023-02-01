package com.hexing.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.AssetManagementConfig;

import java.util.List;

/**
 * 资产管理配置Service接口
 *
 * @author zxy
 * @date 2023-01-30
 */
public interface IAssetManagementConfigService extends IService<AssetManagementConfig>
{
    /**
     * 查询资产管理配置
     *
     * @param id 资产管理配置主键
     * @return 资产管理配置
     */
    public AssetManagementConfig selectAssetManagementConfigById(Long id);

    /**
     * 查询资产管理配置列表
     *
     * @param assetManagementConfig 资产管理配置
     * @return 资产管理配置集合
     */
    public List<AssetManagementConfig> selectAssetManagementConfigList(AssetManagementConfig assetManagementConfig);

    /**
     * 资产、财务管理员资产数据权限查询接口
     *
     * @param user 资产管理配置
     * @return 资产管理配置集合
     */
    public List<AssetManagementConfig> listManagementConfig(String user);


    /**
     * 新增资产管理配置
     *
     * @param assetManagementConfig 资产管理配置
     * @return 结果
     */
    public int insertAssetManagementConfig(AssetManagementConfig assetManagementConfig);

    /**
     * 修改资产管理配置
     *
     * @param assetManagementConfig 资产管理配置
     * @return 结果
     */
    public int updateAssetManagementConfig(AssetManagementConfig assetManagementConfig);

    /**
     * 批量删除资产管理配置
     *
     * @param ids 需要删除的资产管理配置主键集合
     * @return 结果
     */
    public int deleteAssetManagementConfigByIds(Long[] ids);
}
