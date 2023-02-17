package com.hexing.asset.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.dto.SapAssetTransferDTO;
import com.hexing.asset.domain.dto.SapPurchaseOrder;
import com.hexing.asset.domain.dto.SapValueDTO;
import com.hexing.asset.domain.dto.SimpleOuterDTO;
import com.hexing.asset.domain.vo.AssetFixVO;
import com.hexing.asset.domain.vo.AssetQueryParam;
import com.hexing.asset.domain.vo.AssetReceiveVO;
import com.hexing.asset.domain.vo.AssetTransferVO;
import com.hexing.common.core.domain.Result;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 资产表Service接口
 *
 * @author zxy
 * @date 2022-09-08
 */
public interface IAssetService extends IService<Asset> {
    /**
     * 根据平台资产编号查询资产
     *
     * @param assetCode
     * @return
     */
    Asset selectAssetByAssetCode(String assetCode);

    /**
     * 资产信息导入
     *
     * @param assetList       资产信息列表
     * @param isUpdateSupport 是否存在则覆盖
     * @param operName        操作人姓名
     * @return
     */
    String importAsset(List<Asset> assetList, Boolean isUpdateSupport, String operName);

    /**
     * 查询资产表列表
     *
     * @param param 资产信息查询参数
     * @return
     */
    List<Asset> selectAssetList(AssetQueryParam param);

    List<Asset> selectAllAsset(AssetQueryParam param);

    /**
     * 根据资产编号查询资产信息
     */
    Result queryAssetCard(Asset asset);

    /**
     * 新增资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    int insertAsset(Asset asset);

    /**
     * 修改资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    int updateAsset(Asset asset, AssetProcess process);

    /**
     * 根据平台资产编号删除资产信息
     */
    int deleteAssetByAssetCodes(List<String> assetCodes);

    /**
     * 根据部门ID查询部门下所有员工保管的资产
     */
    List<Asset> selectAssetListByDeptId(Long deptId);

    /**
     * 根据工号查询员工资产
     *
     * @param userCodeList
     * @return
     */
    List<Asset> selectAssetByUserName(List<String> userCodeList);

    /**
     * 查询部门下所有员工名下的资产
     */
    List<Asset> selectAssetByDeptId(Long deptId);

    /**
     * SAP采购单同步接口
     */
    void sapAdd(List<SapPurchaseOrder> orderList);

    /**
     * SAP价值传输接口
     */
    List<SapValueDTO> sapSyncValue(List<SapValueDTO> sapValueList);

    /**
     * 资产转固
     */
    JSONObject fixAsset(AssetFixVO vo) throws Exception;

    /**
     * 资产转移
     */
    void transferAsset(AssetTransferVO vo) throws Exception;

    /**
     * 资产派发
     */
    void receiveAsset(AssetReceiveVO vo) throws Exception;

    /**
     * 资产账务转移
     */
    JSONObject transferAsset(SapAssetTransferDTO dto) throws Exception;
}
