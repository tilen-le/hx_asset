package com.hexing.assetNew.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.hexing.assetNew.domain.AssetProcessBack;

/**
 * 资产归还流程Mapper接口
 *
 * @author zxy
 * @date 2022-09-08
 */
@Repository
public interface AssetProcessBackMapper extends BaseMapper<AssetProcessBack>
{
    /**
     * 查询资产归还流程
     *
     * @param id 资产归还流程主键
     * @return 资产归还流程
     */
    public AssetProcessBack selectAssetProcessBackById(Long id);

    /**
     * 查询资产归还流程列表
     *
     * @param assetProcessBack 资产归还流程
     * @return 资产归还流程集合
     */
    public List<AssetProcessBack> selectAssetProcessBackList(AssetProcessBack assetProcessBack);

    /**
     * 新增资产归还流程
     *
     * @param assetProcessBack 资产归还流程
     * @return 结果
     */
    public int insertAssetProcessBack(AssetProcessBack assetProcessBack);

    /**
     * 修改资产归还流程
     *
     * @param assetProcessBack 资产归还流程
     * @return 结果
     */
    public int updateAssetProcessBack(AssetProcessBack assetProcessBack);

    /**
     * 删除资产归还流程
     *
     * @param id 资产归还流程主键
     * @return 结果
     */
    public int deleteAssetProcessBackById(Long id);

    /**
     * 批量删除资产归还流程
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessBackByIds(Long[] ids);
}
