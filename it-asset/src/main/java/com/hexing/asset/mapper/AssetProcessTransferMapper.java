package com.hexing.asset.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.hexing.asset.domain.AssetProcessTransfer;

/**
 * 资产转移流程Mapper接口
 *
 * @author zxy
 * @date 2022-09-20
 */
@Repository
public interface AssetProcessTransferMapper extends BaseMapper<AssetProcessTransfer>
{
    /**
     * 查询资产转移流程
     *
     * @param id 资产转移流程主键
     * @return 资产转移流程
     */
    public AssetProcessTransfer selectAssetProcessTransferById(Long id);

    /**
     * 查询资产转移流程列表
     *
     * @param assetProcessTransfer 资产转移流程
     * @return 资产转移流程集合
     */
    public List<AssetProcessTransfer> selectAssetProcessTransferList(AssetProcessTransfer assetProcessTransfer);

    /**
     * 新增资产转移流程
     *
     * @param assetProcessTransfer 资产转移流程
     * @return 结果
     */
    public int insertAssetProcessTransfer(AssetProcessTransfer assetProcessTransfer);

    /**
     * 修改资产转移流程
     *
     * @param assetProcessTransfer 资产转移流程
     * @return 结果
     */
    public int updateAssetProcessTransfer(AssetProcessTransfer assetProcessTransfer);

    /**
     * 删除资产转移流程
     *
     * @param id 资产转移流程主键
     * @return 结果
     */
    public int deleteAssetProcessTransferById(Long id);

    /**
     * 批量删除资产转移流程
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessTransferByIds(Long[] ids);
}
