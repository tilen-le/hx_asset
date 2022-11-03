package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.AssetProcessVariable;
import com.hexing.asset.mapper.AssetProcessVariableMapper;
import com.hexing.asset.service.IAssetProcessVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 流程值Service业务层处理
 *
 * @author zxy
 * @date 2022-11-03
 */
@Service
public class AssetProcessVariableServiceImpl extends ServiceImpl<AssetProcessVariableMapper, AssetProcessVariable> implements IAssetProcessVariableService {
    @Autowired
    private AssetProcessVariableMapper assetProcessVariableMapper;

    /**
     * 查询流程值
     *
     * @param id 流程值主键
     * @return 流程值
     */
    @Override
    public AssetProcessVariable selectAssetProcessVariableById(Long id) {
        return assetProcessVariableMapper.selectAssetProcessVariableById(id);
    }

    /**
     * 查询流程值列表
     *
     * @param assetProcessVariable 流程值
     * @return 流程值
     */
    @Override
    public List<AssetProcessVariable> selectAssetProcessVariableList(AssetProcessVariable assetProcessVariable) {
        return assetProcessVariableMapper.selectAssetProcessVariableList(assetProcessVariable);
    }

    /**
     * 新增流程值
     */
    @Override
    public int insertAssetProcessVariable(AssetProcessVariable assetProcessVariable) {
        return assetProcessVariableMapper.insert(assetProcessVariable);
    }

    /**
     * 修改流程值
     *
     * @param assetProcessVariable 流程值
     * @return 结果
     */
    @Override
    public int updateAssetProcessVariable(AssetProcessVariable assetProcessVariable) {
        return assetProcessVariableMapper.updateAssetProcessVariable(assetProcessVariable);
    }

    /**
     * 批量删除流程值
     *
     * @param ids 需要删除的流程值主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessVariableByIds(Long[] ids) {
        return assetProcessVariableMapper.deleteAssetProcessVariableByIds(ids);
    }

    /**
     * 删除流程值信息
     *
     * @param id 流程值主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessVariableById(Long id) {
        return assetProcessVariableMapper.deleteAssetProcessVariableById(id);
    }
}
