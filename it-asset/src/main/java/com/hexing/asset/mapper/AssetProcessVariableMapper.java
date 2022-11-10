package com.hexing.asset.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.asset.domain.AssetProcessVariable;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 流程值Mapper接口
 *
 * @author zxy
 * @date 2022-11-03
 */
@Repository
public interface AssetProcessVariableMapper extends BaseMapper<AssetProcessVariable>
{
    /**
     * 查询流程值
     *
     * @param id 流程值主键
     * @return 流程值
     */
    public AssetProcessVariable selectAssetProcessVariableById(Long id);

    /**
     * 查询流程值列表
     *
     * @param assetProcessVariable 流程值
     * @return 流程值集合
     */
    public List<AssetProcessVariable> selectAssetProcessVariableList(AssetProcessVariable assetProcessVariable);

    /**
     * 新增流程值
     *
     * @param assetProcessVariable 流程值
     * @return 结果
     */
    public int insertAssetProcessVariable(AssetProcessVariable assetProcessVariable);

    /**
     * 修改流程值
     *
     * @param assetProcessVariable 流程值
     * @return 结果
     */
    public int updateAssetProcessVariable(AssetProcessVariable assetProcessVariable);

    /**
     * 删除流程值
     *
     * @param id 流程值主键
     * @return 结果
     */
    public int deleteAssetProcessVariableById(Long id);

    /**
     * 批量删除流程值
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessVariableByIds(Long[] ids);

    List<AssetProcessVariable> selectProcessVariableWithCondition(List<Long> processIdList);

}
