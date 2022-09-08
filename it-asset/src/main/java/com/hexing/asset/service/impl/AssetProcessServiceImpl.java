package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessMapper;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.service.IAssetProcessService;

/**
 * 流程总Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessServiceImpl extends ServiceImpl<AssetProcessMapper, AssetProcess> implements IAssetProcessService
{
    @Autowired
    private AssetProcessMapper assetProcessMapper;

    /**
     * 查询流程总
     *
     * @param id 流程总主键
     * @return 流程总
     */
    @Override
    public AssetProcess selectAssetProcessById(Long id)
    {
        return assetProcessMapper.selectAssetProcessById(id);
    }

    /**
     * 查询流程总列表
     *
     * @param assetProcess 流程总
     * @return 流程总
     */
    @Override
    public List<AssetProcess> selectAssetProcessList(AssetProcess assetProcess)
    {
        return assetProcessMapper.selectAssetProcessList(assetProcess);
    }

    /**
     * 新增流程总
     *
     * @param assetProcess 流程总
     * @return 结果
     */
    @Override
    public int insertAssetProcess(AssetProcess assetProcess)
    {
        assetProcess.setCreateTime(DateUtils.getNowDate());
        return assetProcessMapper.insertAssetProcess(assetProcess);
    }

    /**
     * 修改流程总
     *
     * @param assetProcess 流程总
     * @return 结果
     */
    @Override
    public int updateAssetProcess(AssetProcess assetProcess)
    {
        assetProcess.setUpdateTime(DateUtils.getNowDate());
        return assetProcessMapper.updateAssetProcess(assetProcess);
    }

    /**
     * 批量删除流程总
     *
     * @param ids 需要删除的流程总主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessByIds(Long[] ids)
    {
        return assetProcessMapper.deleteAssetProcessByIds(ids);
    }

    /**
     * 删除流程总信息
     *
     * @param id 流程总主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessById(Long id)
    {
        return assetProcessMapper.deleteAssetProcessById(id);
    }
}
