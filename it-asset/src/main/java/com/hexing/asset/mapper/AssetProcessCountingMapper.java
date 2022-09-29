package com.hexing.asset.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.asset.domain.vo.AssetProcessCountingVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.hexing.asset.domain.AssetProcessCounting;

/**
 * 资产盘点流程Mapper接口
 *
 * @author zxy
 * @date 2022-09-08
 */
@Repository
public interface AssetProcessCountingMapper extends BaseMapper<AssetProcessCounting>
{
    /**
     * 查询资产盘点流程
     *
     * @param id 资产盘点流程主键
     * @return 资产盘点流程
     */
    public AssetProcessCounting selectAssetProcessCountingById(Long id);

    /**
     * 查询资产盘点流程列表
     *
     * @param assetProcessCounting 资产盘点流程
     * @return 资产盘点流程集合
     */
    public List<AssetProcessCounting> selectAssetProcessCountingList(AssetProcessCounting assetProcessCounting);

    /**
     * 新增资产盘点流程
     *
     * @param assetProcessCounting 资产盘点流程
     * @return 结果
     */
    public int insertAssetProcessCounting(AssetProcessCounting assetProcessCounting);

    /**
     * 修改资产盘点流程
     *
     * @param assetProcessCounting 资产盘点流程
     * @return 结果
     */
    public int updateAssetProcessCounting(AssetProcessCounting assetProcessCounting);

    /**
     * 删除资产盘点流程
     *
     * @param id 资产盘点流程主键
     * @return 结果
     */
    public int deleteAssetProcessCountingById(Long id);

    /**
     * 批量删除资产盘点流程
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessCountingByIds(Long[] ids);

}
