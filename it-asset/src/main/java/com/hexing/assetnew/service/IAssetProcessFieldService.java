package com.hexing.assetnew.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.assetnew.domain.AssetProcessField;

/**
 * 【请填写功能名称】Service接口
 *
 * @author zxy
 * @date 2022-11-01
 */
public interface IAssetProcessFieldService extends IService<AssetProcessField> {

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
     *  TODO 新增流程字段
     *
     * @param assetProcessField 流程字段对象
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
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteAssetProcessFieldByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteAssetProcessFieldById(Long id);

}
