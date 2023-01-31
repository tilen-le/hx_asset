package com.hexing.assetNew.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.assetNew.domain.AssetProcessVariable;

import java.util.List;

/**
 * 流程值Service接口
 *
 * @author zxy
 * @date 2022-11-03
 */
public interface IAssetProcessVariableService extends IService<AssetProcessVariable> {
    /**
     * 根据流程id查询值表列表
     */
    List<AssetProcessVariable> selectVariableListByProcessId(Long processId);
    /**
     * 根据流程id查询值表列表
     */
    List<AssetProcessVariable> selectVarWithProcessIds(List<Long> processIds);
}
