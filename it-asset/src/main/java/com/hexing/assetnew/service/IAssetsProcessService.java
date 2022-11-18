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
    AssetsProcess getOne(AssetsProcess process);

    /**
     * 根据流程id更新值表
     */
    void updateByProcessId(AssetsProcess process);

    /**
     * 根据流程id查询值表列表
     */
    List<AssetProcessVariable> selectVariableListByProcessId(Long processId);

    /**
     * 分页查询资产流程列表
     */
    List<AssetsProcess> listByPage(AssetsProcess process);

    /**
     * 查询资产流程列表
     */
    List<AssetsProcess> list(AssetsProcess process);

    <T> T convertProcess(AssetsProcess process, T domain);

    /**
     * 盘点状态统计
     * @param taskCode 盘点任务编号
     * @return
     */
    CountingStatusNumDTO countingStatusCount(String taskCode);

    void saveBatchProcess(List<? extends AssetsProcess> processList);
}
