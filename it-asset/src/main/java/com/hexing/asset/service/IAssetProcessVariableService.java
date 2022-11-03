package com.hexing.asset.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.AssetProcessVariable;

/**
 * 流程值Service接口
 *
 * @author zxy
 * @date 2022-11-03
 */
public interface IAssetProcessVariableService extends IService<AssetProcessVariable> {
    /**
     * 查询流程值
     *
     * @param id 流程值主键
     * @return 流程值
     */
    public AssetProcessVariable selectAssetProcessVariableById(Long id);

    /**
     * 查询流程值列表
     *
     * @param assetProcessVariable 流程值
     * @return 流程值集合
     */
    public List<AssetProcessVariable> selectAssetProcessVariableList(AssetProcessVariable assetProcessVariable);

    /**
     * 新增流程值
     *
     * @param assetProcessVariable 流程值
     * @return 结果
     */
    public int insertAssetProcessVariable(AssetProcessVariable assetProcessVariable);

    /**
     * 修改流程值
     *
     * @param assetProcessVariable 流程值
     * @return 结果
     */
    public int updateAssetProcessVariable(AssetProcessVariable assetProcessVariable);

    /**
     * 批量删除流程值
     *
     * @param ids 需要删除的流程值主键集合
     * @return 结果
     */
    public int deleteAssetProcessVariableByIds(Long[] ids);

    /**
     * 删除流程值信息
     *
     * @param id 流程值主键
     * @return 结果
     */
    public int deleteAssetProcessVariableById(Long id);
}
