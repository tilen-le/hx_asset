package com.hexing.assetNew.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.hexing.assetNew.domain.AssetProcessExchange;

/**
 * 资产更换流程Mapper接口
 *
 * @author zxy
 * @date 2022-09-08
 */
@Repository
public interface AssetProcessExchangeMapper extends BaseMapper<AssetProcessExchange>
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
     * 删除资产更换流程
     *
     * @param id 资产更换流程主键
     * @return 结果
     */
    public int deleteAssetProcessExchangeById(Long id);

    /**
     * 批量删除资产更换流程
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessExchangeByIds(Long[] ids);
}
