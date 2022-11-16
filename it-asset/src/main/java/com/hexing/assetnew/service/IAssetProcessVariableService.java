package com.hexing.assetnew.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.assetnew.domain.AssetProcessVariable;

import java.util.List;

/**
 * 流程值Service接口
 *
 * @author zxy
 * @date 2022-11-03
 */
public interface IAssetProcessVariableService extends IService<AssetProcessVariable> {

    Integer insertBatch(List<AssetProcessVariable> variableList);

}
