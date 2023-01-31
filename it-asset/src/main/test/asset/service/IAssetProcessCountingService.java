package com.hexing.assetNew.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hexing.assetNew.domain.AssetProcessCounting;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.assetNew.domain.vo.AssetProcessCountingVO;
import com.hexing.common.core.domain.AjaxResult;

/**
 * 资产盘点流程Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetProcessCountingService extends IService<AssetProcessCounting>
{

    /**
     * 查询资产盘点流程列表
     *
     * @param params 查询参数
     * @return 资产盘点流程集合
     */
//    public List<Map<String, Object>> selectAssetProcessCountingList(Map<String, Object> params);

    /**
     * 新增资产盘点流程
     *
     * @param entity 资产盘点流程
     * @return 结果
     */
    public int insertAssetProcessCounting(AssetProcessCounting entity);

    /**
     *
     * @param list
     * @return
     */
//    List<AssetProcessCountingVO> toAssetProcessCountingVOList(List<AssetProcessCounting> list);

    /**
     * 删除资产盘点流程信息
     *
     * @param id 资产盘点流程主键
     * @return 结果
     */
    public int deleteAssetProcessCountingById(Long id);

    List<AssetProcessCounting> inventoryCountList(String startDate, String endDate);

    JSONObject inventoryCount(String type,String startDate, String endDate);

    /**
     * 批量添加资产盘点流程信息
     */
    void saveBatch(List<AssetProcessCounting> processCountingList) throws Exception;
}
