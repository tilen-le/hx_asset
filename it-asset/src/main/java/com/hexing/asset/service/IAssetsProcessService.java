package com.hexing.asset.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.AssetProcessVariable;
import com.hexing.asset.domain.AssetsProcess;
import com.hexing.asset.domain.Process;

/**
 * 资产流程Service接口
 *
 * @author zxy
 * @date 2022-11-04
 */
public interface IAssetsProcessService extends IService<AssetsProcess>
{

    /**
     * 查询资产流程
     */
    Object getOne(String processType, Map<String, Object> params);

    List<AssetProcessVariable> selectVariableListByProcessId(String processId);

    /**
     * 查询资产流程列表
     */
    List<Map<String, Object>> list(Process assetProcess);

    List<AssetsProcess> selectProcessWithCondition(String processType, Map<String, Object> params);

    void updateByProcessId(Object obj);
}
