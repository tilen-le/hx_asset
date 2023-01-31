package com.hexing.assetNew.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.assetNew.domain.AssetHistory;
import org.springframework.stereotype.Repository;

/**
 * 资产历史记录Mapper接口
 *
 * @author zxy
 * @date 2022-11-01
 */
@Repository
public interface AssetHistoryMapper extends BaseMapper<AssetHistory>
{
    /**
     * 查询资产历史记录
     *
     * @param id 资产历史记录主键
     * @return 资产历史记录
     */
    public AssetHistory selectAssetHistoryById(Long id);

    /**
     * 查询资产历史记录列表
     *
     * @param assetHistory 资产历史记录
     * @return 资产历史记录集合
     */
    public List<AssetHistory> selectAssetHistoryList(AssetHistory assetHistory);

    /**
     * 新增资产历史记录
     *
     * @param assetHistory 资产历史记录
     * @return 结果
     */
    public int insertAssetHistory(AssetHistory assetHistory);

    /**
     * 修改资产历史记录
     *
     * @param assetHistory 资产历史记录
     * @return 结果
     */
    public int updateAssetHistory(AssetHistory assetHistory);

    /**
     * 删除资产历史记录
     *
     * @param id 资产历史记录主键
     * @return 结果
     */
    public int deleteAssetHistoryById(Long id);

    /**
     * 批量删除资产历史记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetHistoryByIds(Long[] ids);
}
