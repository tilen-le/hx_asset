package com.hexing.asset.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.hexing.asset.domain.AssetProcessMaintain;

/**
 * 资产维修流程Mapper接口
 *
 * @author zxy
 * @date 2022-09-08
 */
@Repository
public interface AssetProcessMaintainMapper extends BaseMapper<AssetProcessMaintain>
{
    /**
     * 查询资产维修流程
     *
     * @param id 资产维修流程主键
     * @return 资产维修流程
     */
    public AssetProcessMaintain selectAssetProcessMaintainById(Long id);

    /**
     * 查询资产维修流程列表
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 资产维修流程集合
     */
    public List<AssetProcessMaintain> selectAssetProcessMaintainList(AssetProcessMaintain assetProcessMaintain);

    /**
     * 新增资产维修流程
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 结果
     */
    public int insertAssetProcessMaintain(AssetProcessMaintain assetProcessMaintain);

    /**
     * 修改资产维修流程
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 结果
     */
    public int updateAssetProcessMaintain(AssetProcessMaintain assetProcessMaintain);

    /**
     * 删除资产维修流程
     *
     * @param id 资产维修流程主键
     * @return 结果
     */
    public int deleteAssetProcessMaintainById(Long id);

    /**
     * 批量删除资产维修流程
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessMaintainByIds(Long[] ids);
}
