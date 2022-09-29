package com.hexing.asset.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcess;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.dto.StatisQueryParam;

/**
 * 流程总Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetProcessService extends IService<AssetProcess>
{
    /**
     * 查询流程总
     *
     * @param id 流程总主键
     * @return 流程总
     */
    public AssetProcess selectAssetProcessById(Long id);

    /**
     * 查询流程总列表
     *
     * @param assetProcess 流程总
     * @return 流程总集合
     */
    public List<AssetProcess> selectAssetProcessList(AssetProcess assetProcess);

    /**
     * 新增流程总
     *
     * @param assetProcess 流程总
     * @return 结果
     */
    public int insertAssetProcess(AssetProcess assetProcess);

    /**
     * 修改流程总
     *
     * @param assetProcess 流程总
     * @return 结果
     */
    public int updateAssetProcess(AssetProcess assetProcess);

    /**
     * 批量删除流程总
     *
     * @param ids 需要删除的流程总主键集合
     * @return 结果
     */
    public int deleteAssetProcessByIds(Long[] ids);

    /**
     * 删除流程总信息
     *
     * @param id 流程总主键
     * @return 结果
     */
    public int deleteAssetProcessById(Long id);

    List<AssetProcess> queryAssetProcessForStatisticByType(String processType, StatisQueryParam params, List<Asset> assetList);
}
