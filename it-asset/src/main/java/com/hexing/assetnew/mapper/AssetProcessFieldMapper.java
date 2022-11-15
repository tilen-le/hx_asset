package com.hexing.assetnew.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.assetnew.domain.AssetProcessField;
import org.springframework.stereotype.Repository;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author zxy
 * @date 2022-11-01
 */
@Repository
public interface AssetProcessFieldMapper extends BaseMapper<AssetProcessField> {

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public AssetProcessField selectAssetProcessFieldById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param assetProcessField 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<AssetProcessField> selectAssetProcessFieldList(AssetProcessField assetProcessField);

    /**
     * 新增【请填写功能名称】
     *
     * @param assetProcessField 【请填写功能名称】
     * @return 结果
     */
    public int insertAssetProcessField(AssetProcessField assetProcessField);

    /**
     * 修改【请填写功能名称】
     *
     * @param assetProcessField 【请填写功能名称】
     * @return 结果
     */
    public int updateAssetProcessField(AssetProcessField assetProcessField);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteAssetProcessFieldById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessFieldByIds(Long[] ids);
}
