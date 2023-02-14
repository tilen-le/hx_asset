package com.hexing.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.vo.AssetProcessParam;

import java.util.List;

/**
 * 资产表Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetProcessService extends IService<AssetProcess> {

    /**
     * 资产操作-派发
     *
     * @param assetProcess 资产表
     * @return 结果
     */
    int receiveAsset(AssetProcessParam assetProcess);

    /**
     * 资产转移
     *
     * @return 结果
     */
    int transferAsset(AssetProcessParam assetProcess);

    /**
     * 资产操作-已退货
     *
     * @return 结果
     */
    int returnAsset(AssetProcessParam assetProcess);

    /**
     * 资产转固
     *
     * @return 结果
     */
    int fixationAsset(AssetProcessParam assetProcess);

    /**
     * 资产操作-维修
     *
     * @return 结果
     */
    int maintainAsset(AssetProcessParam assetProcess);

    /**
     * 资产操作-闲置
     *
     * @return 结果
     */
    int unusedAsset(AssetProcessParam assetProcess);

    /**
     * 资产操作-报废
     *
     * @return 结果
     */
    int scrapAsset(AssetProcessParam assetProcess);

    /**
     * 资产待外卖
     *
     * @return 结果
     */
    int waiteTakeOutAsset(AssetProcessParam assetProcess);

    /**
     * 资产操作-盘亏
     *
     * @return 结果
     */
    int inventoryLossAsset(AssetProcessParam assetProcess);

    /**
     * 资产操作-已维修
     *
     * @return 结果
     */
    int maintainedAsset(AssetProcessParam assetProcess);

    /**
     * 资产已外卖
     *
     * @return 结果
     */
    int takeOutAsset(AssetProcessParam assetProcess);

    /**
     * 资产操作-已报废
     *
     * @return 结果
     */
    int scrapedAsset(AssetProcessParam assetProcess);

    /**
     * 查询资产流程
     */
    AssetProcess getOne(AssetProcess process);

    /**
     * 根据流程id更新值表
     */
    void updateByProcessId(AssetProcess process);

    /**
     * 分页查询资产流程列表
     */
    List<AssetProcess> listByPage(AssetProcess process);

    /**
     * 查询资产流程列表
     */
    List<AssetProcess> list(AssetProcess process);

    <T> T convertProcess(AssetProcess process, T domain);

    void saveOne(AssetProcess process);

    void saveBatchProcess(List<? extends AssetProcess> processList);

    void saveProcess(AssetProcessParam processParam, String type);
}
