package com.hexing.assetNew.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.hexing.assetNew.domain.AssetProcessReceive;

/**
 * 资产领用流程Mapper接口
 *
 * @author zxy
 * @date 2022-09-08
 */
@Repository
public interface AssetProcessReceiveMapper extends BaseMapper<AssetProcessReceive>
{
    /**
     * 查询资产领用流程
     *
     * @param id 资产领用流程主键
     * @return 资产领用流程
     */
    public AssetProcessReceive selectAssetProcessReceiveById(Long id);

    /**
     * 查询资产领用流程列表
     *
     * @param assetProcessReceive 资产领用流程
     * @return 资产领用流程集合
     */
    public List<AssetProcessReceive> selectAssetProcessReceiveList(AssetProcessReceive assetProcessReceive);

    /**
     * 新增资产领用流程
     *
     * @param assetProcessReceive 资产领用流程
     * @return 结果
     */
    public int insertAssetProcessReceive(AssetProcessReceive assetProcessReceive);

    /**
     * 修改资产领用流程
     *
     * @param assetProcessReceive 资产领用流程
     * @return 结果
     */
    public int updateAssetProcessReceive(AssetProcessReceive assetProcessReceive);

    /**
     * 删除资产领用流程
     *
     * @param id 资产领用流程主键
     * @return 结果
     */
    public int deleteAssetProcessReceiveById(Long id);

    /**
     * 批量删除资产领用流程
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessReceiveByIds(Long[] ids);
}
