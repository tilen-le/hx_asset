package com.hexing.assetNew.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.assetNew.domain.vo.AssetProcessCountingVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.hexing.assetNew.domain.AssetProcessCounting;

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
     * 删除资产盘点流程
     *
     * @param id 资产盘点流程主键
     * @return 结果
     */
    public int deleteAssetProcessCountingById(Long id);


}
