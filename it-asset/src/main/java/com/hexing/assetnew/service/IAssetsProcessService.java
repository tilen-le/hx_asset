package com.hexing.assetnew.service;

import java.util.List;
import java.util.Map;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.assetnew.domain.AssetProcessVariable;
import com.hexing.assetnew.domain.AssetsProcess;
import com.hexing.asset.domain.Process;
import com.hexing.assetnew.domain.dto.CountingStatusNumDTO;

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
    List<AssetsProcess> list(AssetsProcess process);

    List<AssetsProcess> selectProcessWithCondition(String processType, Map<String, Object> params);

    void updateByProcessId(Object obj);

    /**
     * 盘点状态统计
     * @param taskCode 盘点任务编号
     * @return
     */
    CountingStatusNumDTO countingStatusCount(String taskCode);
}
