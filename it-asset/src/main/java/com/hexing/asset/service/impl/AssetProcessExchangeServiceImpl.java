package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessExchangeMapper;
import com.hexing.asset.domain.AssetProcessExchange;
import com.hexing.asset.service.IAssetProcessExchangeService;

/**
 * 资产更换流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessExchangeServiceImpl extends ServiceImpl<AssetProcessExchangeMapper, AssetProcessExchange> implements IAssetProcessExchangeService
{
    @Autowired
    private AssetProcessExchangeMapper assetProcessExchangeMapper;

    /**
     * 查询资产更换流程
     *
     * @param id 资产更换流程主键
     * @return 资产更换流程
     */
    @Override
    public AssetProcessExchange selectAssetProcessExchangeById(Long id)
    {
        return assetProcessExchangeMapper.selectAssetProcessExchangeById(id);
    }

    /**
     * 查询资产更换流程列表
     *
     * @param assetProcessExchange 资产更换流程
     * @return 资产更换流程
     */
    @Override
    public List<AssetProcessExchange> selectAssetProcessExchangeList(AssetProcessExchange assetProcessExchange)
    {
        return assetProcessExchangeMapper.selectAssetProcessExchangeList(assetProcessExchange);
    }

    /**
     * 新增资产更换流程
     *
     * @param assetProcessExchange 资产更换流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessExchange(AssetProcessExchange assetProcessExchange)
    {
        assetProcessExchange.setCreateTime(DateUtils.getNowDate());
        return assetProcessExchangeMapper.insertAssetProcessExchange(assetProcessExchange);
    }

    /**
     * 修改资产更换流程
     *
     * @param assetProcessExchange 资产更换流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessExchange(AssetProcessExchange assetProcessExchange)
    {
        assetProcessExchange.setUpdateTime(DateUtils.getNowDate());
        return assetProcessExchangeMapper.updateAssetProcessExchange(assetProcessExchange);
    }

    /**
     * 批量删除资产更换流程
     *
     * @param ids 需要删除的资产更换流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessExchangeByIds(Long[] ids)
    {
        return assetProcessExchangeMapper.deleteAssetProcessExchangeByIds(ids);
    }

    /**
     * 删除资产更换流程信息
     *
     * @param id 资产更换流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessExchangeById(Long id)
    {
        return assetProcessExchangeMapper.deleteAssetProcessExchangeById(id);
    }
}
