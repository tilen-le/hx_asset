package com.hexing.assetNew.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.assetNew.domain.AssetProcessVariable;
import com.hexing.assetNew.mapper.AssetProcessVariableMapper;
import com.hexing.assetNew.service.IAssetProcessVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Override
    public List<AssetProcessVariable> selectVariableListByProcessId(Long processId) {
        return assetProcessVariableMapper.selectVariableListByProcessId(processId);
    }

    @Override
    public List<AssetProcessVariable> selectVarWithProcessIds(List<Long> processIds) {
        return assetProcessVariableMapper.selectVarWithProcessIds(processIds);
    }
}
