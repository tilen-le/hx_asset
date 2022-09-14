package com.hexing.asset.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessCountingMapper;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.service.IAssetProcessCountingService;

/**
 * 资产盘点流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessCountingServiceImpl extends ServiceImpl<AssetProcessCountingMapper, AssetProcessCounting> implements IAssetProcessCountingService
{
    @Autowired
    private AssetProcessCountingMapper assetProcessCountingMapper;

    /**
     * 查询资产盘点流程
     *
     * @param id 资产盘点流程主键
     * @return 资产盘点流程
     */
    @Override
    public AssetProcessCounting selectAssetProcessCountingById(Long id)
    {
        return assetProcessCountingMapper.selectAssetProcessCountingById(id);
    }

    /**
     * 查询资产盘点流程列表
     *
     * @param assetProcessCounting 资产盘点流程
     * @return 资产盘点流程
     */
    @Override
    public List<AssetProcessCounting> selectAssetProcessCountingList(AssetProcessCounting assetProcessCounting)
    {
        return assetProcessCountingMapper.selectAssetProcessCountingList(assetProcessCounting);
    }

    /**
     * 新增资产盘点流程
     *
     * @param assetProcessCounting 资产盘点流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessCounting(AssetProcessCounting vo)
    {
        int insert = 0;
        List<Asset> assets = vo.getAssets();
        for (int i = 0; i < assets.size(); i++) {
            AssetProcessCounting assetProcessCounting  = new AssetProcessCounting();
            assetProcessCounting.setAssetCode(assets.get(i).getAssetCode());
            assetProcessCounting.setUserCode(vo.getUserCode());

            insert = assetProcessCountingMapper.insert(assetProcessCounting);
        }
        return insert;

    }

    /**
     * 修改资产盘点流程
     *
     * @param assetProcessCounting 资产盘点流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessCounting(AssetProcessCounting assetProcessCounting)
    {
        assetProcessCounting.setUpdateTime(DateUtils.getNowDate());
        return assetProcessCountingMapper.updateAssetProcessCounting(assetProcessCounting);
    }

    /**
     * 批量删除资产盘点流程
     *
     * @param ids 需要删除的资产盘点流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessCountingByIds(Long[] ids)
    {
        return assetProcessCountingMapper.deleteAssetProcessCountingByIds(ids);
    }

    /**
     * 删除资产盘点流程信息
     *
     * @param id 资产盘点流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessCountingById(Long id)
    {
        return assetProcessCountingMapper.deleteAssetProcessCountingById(id);
    }
}
