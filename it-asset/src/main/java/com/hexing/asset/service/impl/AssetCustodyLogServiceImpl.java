package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetCustodyLogMapper;
import com.hexing.asset.domain.AssetCustodyLog;
import com.hexing.asset.service.IAssetCustodyLogService;

/**
 * 资产保管记录Service业务层处理
 *
 * @author zxy
 * @date 2023-02-10
 */
@Service
public class AssetCustodyLogServiceImpl extends ServiceImpl<AssetCustodyLogMapper, AssetCustodyLog> implements IAssetCustodyLogService
{
    @Autowired
    private AssetCustodyLogMapper assetCustodyLogMapper;

    /**
     * 查询资产保管记录
     *
     * @param id 资产保管记录主键
     * @return 资产保管记录
     */
    @Override
    public AssetCustodyLog selectAssetCustodyLogById(Long id)
    {
        return null;
    }

    /**
     * 查询资产保管记录列表
     *
     * @param assetCustodyLog 资产保管记录
     * @return 资产保管记录
     */
    @Override
    public List<AssetCustodyLog> selectAssetCustodyLogList(AssetCustodyLog assetCustodyLog)
    {
        return null;
    }

    /**
     * 新增资产保管记录
     *
     * @param assetCustodyLog 资产保管记录
     * @return 结果
     */
    @Override
    public int insertAssetCustodyLog(AssetCustodyLog assetCustodyLog)
    {
        return assetCustodyLogMapper.insert(assetCustodyLog);
    }

   }
