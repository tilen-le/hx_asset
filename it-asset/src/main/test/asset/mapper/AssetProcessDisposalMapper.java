package com.hexing.assetNew.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.hexing.assetNew.domain.AssetProcessDisposal;

/**
 * 资产处置流程Mapper接口
 *
 * @author zxy
 * @date 2022-09-08
 */
@Repository
public interface AssetProcessDisposalMapper extends BaseMapper<AssetProcessDisposal>
{
    /**
     * 查询资产处置流程
     *
     * @param id 资产处置流程主键
     * @return 资产处置流程
     */
    public AssetProcessDisposal selectAssetProcessDisposalById(Long id);

    /**
     * 查询资产处置流程列表
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 资产处置流程集合
     */
    public List<AssetProcessDisposal> selectAssetProcessDisposalList(AssetProcessDisposal assetProcessDisposal);

    /**
     * 新增资产处置流程
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 结果
     */
    public int insertAssetProcessDisposal(AssetProcessDisposal assetProcessDisposal);

    /**
     * 修改资产处置流程
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 结果
     */
    public int updateAssetProcessDisposal(AssetProcessDisposal assetProcessDisposal);

    /**
     * 删除资产处置流程
     *
     * @param id 资产处置流程主键
     * @return 结果
     */
    public int deleteAssetProcessDisposalById(Long id);

    /**
     * 批量删除资产处置流程
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessDisposalByIds(Long[] ids);
}
