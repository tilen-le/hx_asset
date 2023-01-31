package com.hexing.asset.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.asset.domain.AssetUpdateLog;
import org.springframework.stereotype.Repository;

/**
 * 资产信息更新日志Mapper接口
 *
 * @author zxy
 * @date 2022-10-19
 */
@Repository
public interface AssetUpdateLogMapper extends BaseMapper<AssetUpdateLog>
{
    /**
     * 查询资产信息更新日志
     *
     * @param id 资产信息更新日志主键
     * @return 资产信息更新日志
     */
    public AssetUpdateLog selectAssetUpdateLogById(Long id);

    /**
     * 查询资产信息更新日志列表
     *
     * @param assetUpdateLog 资产信息更新日志
     * @return 资产信息更新日志集合
     */
    public List<AssetUpdateLog> selectAssetUpdateLogList(AssetUpdateLog assetUpdateLog);

    /**
     * 新增资产信息更新日志
     *
     * @param assetUpdateLog 资产信息更新日志
     * @return 结果
     */
    public int insertAssetUpdateLog(AssetUpdateLog assetUpdateLog);

    /**
     * 修改资产信息更新日志
     *
     * @param assetUpdateLog 资产信息更新日志
     * @return 结果
     */
    public int updateAssetUpdateLog(AssetUpdateLog assetUpdateLog);

    /**
     * 删除资产信息更新日志
     *
     * @param id 资产信息更新日志主键
     * @return 结果
     */
    public int deleteAssetUpdateLogById(Long id);

    /**
     * 批量删除资产信息更新日志
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetUpdateLogByIds(Long[] ids);
}
