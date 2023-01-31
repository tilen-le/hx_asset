package com.hexing.assetNew.service;

import java.util.List;

import com.hexing.assetNew.domain.AssetAttachment;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产附件Service接口
 *
 * @author zxy
 * @date 2022-11-01
 */
public interface IAssetAttachmentService extends IService<AssetAttachment>
{
    /**
     * 查询资产附件
     *
     * @param id 资产附件主键
     * @return 资产附件
     */
    public AssetAttachment selectAssetAttachmentById(Long id);

    /**
     * 查询资产附件列表
     *
     * @param assetAttachment 资产附件
     * @return 资产附件集合
     */
    public List<AssetAttachment> selectAssetAttachmentList(AssetAttachment assetAttachment);

    /**
     * 新增资产附件
     *
     * @param assetAttachment 资产附件
     * @return 结果
     */
    public int insertAssetAttachment(AssetAttachment assetAttachment);

    /**
     * 修改资产附件
     *
     * @param assetAttachment 资产附件
     * @return 结果
     */
    public int updateAssetAttachment(AssetAttachment assetAttachment);

    /**
     * 批量删除资产附件
     *
     * @param ids 需要删除的资产附件主键集合
     * @return 结果
     */
    public int deleteAssetAttachmentByIds(Long[] ids);

    /**
     * 删除资产附件信息
     *
     * @param id 资产附件主键
     * @return 结果
     */
    public int deleteAssetAttachmentById(Long id);
}
