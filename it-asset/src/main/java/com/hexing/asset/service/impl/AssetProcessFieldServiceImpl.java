package com.hexing.asset.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcessField;
import com.hexing.asset.mapper.AssetProcessFieldMapper;
import com.hexing.asset.service.IAssetProcessFieldService;
import com.hexing.common.utils.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author zxy
 * @date 2022-11-01
 */
@Service
public class AssetProcessFieldServiceImpl extends ServiceImpl<AssetProcessFieldMapper, AssetProcessField> implements IAssetProcessFieldService {

    @Autowired
    private AssetProcessFieldMapper assetProcessFieldMapper;

    /**
     * 查询流程字段详细信息
     *
     * @param id 流程字段主键
     * @return 流程字段对象
     */
    @Override
    public AssetProcessField selectAssetProcessFieldById(Long id) {
        return assetProcessFieldMapper.selectById(id);
    }

    /**
     * 查询流程字段列表
     *
     * @param processField 流程字段参数
     * @return 流程字段列表
     */
    @Override
    public List<AssetProcessField> selectAssetProcessFieldList(AssetProcessField processField) {
        LambdaQueryWrapper<AssetProcessField> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(processField.getProcessType())) {
            wrapper.like(AssetProcessField::getProcessType, processField.getProcessType());
        }
        return assetProcessFieldMapper.selectList(wrapper);
    }

    /**
     * 新增流程字段
     *
     * @param assetProcessField 流程字段对象
     * @return 结果
     */
    @Override
    public int insertAssetProcessField(AssetProcessField assetProcessField) {
        return assetProcessFieldMapper.insert(assetProcessField);
    }

    /**
     * 修改流程字段
     *
     * @param processField 流程字段对象
     * @return 结果
     */
    @Override
    public int updateAssetProcessField(AssetProcessField processField) {
        return assetProcessFieldMapper.updateById(processField);
    }

    /**
     * 批量删除流程字段
     *
     * @param ids 需要删除的流程字段主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessFieldByIds(Long[] ids) {
        return assetProcessFieldMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessFieldById(Long id) {
        return assetProcessFieldMapper.deleteAssetProcessFieldById(id);
    }

}
