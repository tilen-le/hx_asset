package com.hexing.assetnew.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.assetnew.domain.AssetProcessField;

/**
 * 流程字段Service接口
 *
 * @author zxy
 * @date 2022-11-01
 */
public interface IAssetProcessFieldService extends IService<AssetProcessField> {

    /**
     * 查询流程字段
     */
    AssetProcessField selectAssetProcessFieldById(Long id);

    /**
     * 查询【请填写功能名称】列表
     */
    List<AssetProcessField> selectAssetProcessFieldList(AssetProcessField assetProcessField);

    /**
     * 新增流程字段
     */
    int insertAssetProcessField(AssetProcessField assetProcessField);

    /**
     * 修改流程字段
     */
    int updateAssetProcessField(AssetProcessField assetProcessField);

    /**
     * 批量删除流程字段
     */
    int deleteAssetProcessFieldByIds(Long[] ids);

}
