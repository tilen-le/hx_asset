package com.hexing.asset.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.hexing.asset.domain.AssetProcessTransform;

/**
 * 资产改造流程Mapper接口
 *
 * @author zxy
 * @date 2022-09-20
 */
@Repository
public interface AssetProcessTransformMapper extends BaseMapper<AssetProcessTransform>
{
    /**
     * 查询资产改造流程
     *
     * @param id 资产改造流程主键
     * @return 资产改造流程
     */
    public AssetProcessTransform selectAssetProcessTransformById(Long id);

    /**
     * 查询资产改造流程列表
     *
     * @param assetProcessTransform 资产改造流程
     * @return 资产改造流程集合
     */
    public List<AssetProcessTransform> selectAssetProcessTransformList(AssetProcessTransform assetProcessTransform);

    /**
     * 新增资产改造流程
     *
     * @param assetProcessTransform 资产改造流程
     * @return 结果
     */
    public int insertAssetProcessTransform(AssetProcessTransform assetProcessTransform);

    /**
     * 修改资产改造流程
     *
     * @param assetProcessTransform 资产改造流程
     * @return 结果
     */
    public int updateAssetProcessTransform(AssetProcessTransform assetProcessTransform);

    /**
     * 删除资产改造流程
     *
     * @param id 资产改造流程主键
     * @return 结果
     */
    public int deleteAssetProcessTransformById(Long id);

    /**
     * 批量删除资产改造流程
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessTransformByIds(Long[] ids);
}
