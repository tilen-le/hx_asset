package com.hexing.assetNew.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.assetNew.domain.AssetsProcess;

import java.util.List;

/**
 * 资产流程Service接口
 *
 * @author zxy
 * @date 2022-11-04
 */
public interface IAssetsProcessService extends IService<AssetsProcess> {

    /**
     * 查询资产流程
     */
    AssetsProcess getOne(AssetsProcess process);

    /**
     * 根据流程id更新值表
     */
    void updateByProcessId(AssetsProcess process);

    /**
     * 分页查询资产流程列表
     */
    List<AssetsProcess> listByPage(AssetsProcess process);

    /**
     * 查询资产流程列表
     */
    List<AssetsProcess> list(AssetsProcess process);

    <T> T convertProcess(AssetsProcess process, T domain);

    void saveBatchProcess(List<? extends AssetsProcess> processList);

}
