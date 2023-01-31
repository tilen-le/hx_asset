package com.hexing.assetNew.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.assetNew.domain.AssetAttachment;
import com.hexing.assetNew.mapper.AssetAttachmentMapper;
import com.hexing.assetNew.service.IAssetAttachmentService;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 资产附件Service业务层处理
 *
 * @author zxy
 * @date 2022-11-01
 */
@Service
public class AssetAttachmentServiceImpl extends ServiceImpl<AssetAttachmentMapper, AssetAttachment> implements IAssetAttachmentService
{
    @Autowired
    private AssetAttachmentMapper assetAttachmentMapper;

    /**
     * 查询资产附件
     *
     * @param id 资产附件主键
     * @return 资产附件
     */
    @Override
    public AssetAttachment selectAssetAttachmentById(Long id)
    {
        return assetAttachmentMapper.selectAssetAttachmentById(id);
    }

    /**
     * 查询资产附件列表
     *
     * @param assetAttachment 资产附件
     * @return 资产附件
     */
    @Override
    public List<AssetAttachment> selectAssetAttachmentList(AssetAttachment assetAttachment)
    {
        return assetAttachmentMapper.selectAssetAttachmentList(assetAttachment);
    }

    /**
     * 新增资产附件
     *
     * @param assetAttachment 资产附件
     * @return 结果
     */
    @Override
    public int insertAssetAttachment(AssetAttachment assetAttachment)
    {
        assetAttachment.setCreateTime(DateUtils.getNowDate());
        return assetAttachmentMapper.insertAssetAttachment(assetAttachment);
    }

    /**
     * 修改资产附件
     *
     * @param assetAttachment 资产附件
     * @return 结果
     */
    @Override
    public int updateAssetAttachment(AssetAttachment assetAttachment)
    {
        return assetAttachmentMapper.updateAssetAttachment(assetAttachment);
    }

    /**
     * 批量删除资产附件
     *
     * @param ids 需要删除的资产附件主键
     * @return 结果
     */
    @Override
    public int deleteAssetAttachmentByIds(Long[] ids)
    {
        return assetAttachmentMapper.deleteAssetAttachmentByIds(ids);
    }

    /**
     * 删除资产附件信息
     *
     * @param id 资产附件主键
     * @return 结果
     */
    @Override
    public int deleteAssetAttachmentById(Long id)
    {
        return assetAttachmentMapper.deleteAssetAttachmentById(id);
    }
}
