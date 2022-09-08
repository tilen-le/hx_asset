package com.hexing.asset.service;

import java.util.List;
import com.hexing.asset.domain.AssetProcessExchange;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资产更换流程Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetProcessExchangeService extends IService<AssetProcessExchange>
{
    /**
     * 查询资产更换流程
     *
     * @param id 资产更换流程主键
     * @return 资产更换流程
     */
    public AssetProcessExchange selectAssetProcessExchangeById(Long id);

    /**
     * 查询资产更换流程列表
     *
     * @param assetProcessExchange 资产更换流程
     * @return 资产更换流程集合
     */
    public List<AssetProcessExchange> selectAssetProcessExchangeList(AssetProcessExchange assetProcessExchange);

    /**
     * 新增资产更换流程
     *
     * @param assetProcessExchange 资产更换流程
     * @return 结果
     */
    public int insertAssetProcessExchange(AssetProcessExchange assetProcessExchange);

    /**
     * 修改资产更换流程
     *
     * @param assetProcessExchange 资产更换流程
     * @return 结果
     */
    public int updateAssetProcessExchange(AssetProcessExchange assetProcessExchange);

    /**
     * 批量删除资产更换流程
     *
     * @param ids 需要删除的资产更换流程主键集合
     * @return 结果
     */
    public int deleteAssetProcessExchangeByIds(Long[] ids);

    /**
     * 删除资产更换流程信息
     *
     * @param id 资产更换流程主键
     * @return 结果
     */
    public int deleteAssetProcessExchangeById(Long id);
}
