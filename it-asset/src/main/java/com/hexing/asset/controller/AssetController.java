package com.hexing.asset.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.dto.SapAssetFixDTO;
import com.hexing.asset.domain.dto.SapPurchaseOrder;
import com.hexing.asset.domain.dto.SapValueDTO;
import com.hexing.asset.domain.dto.SimpleOuterDTO;
import com.hexing.asset.domain.vo.AssetFixVO;
import com.hexing.asset.domain.vo.AssetQueryParam;
import com.hexing.asset.domain.vo.AssetTransferVO;
import com.hexing.asset.enums.UIPCodeEnum;
import com.hexing.asset.service.IAssetService;
import com.hexing.asset.service.IUIPService;
import com.hexing.asset.utils.CodeUtil;
import com.hexing.common.annotation.Log;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.domain.model.LoginUser;
import com.hexing.common.core.page.TableDataInfo;
import com.hexing.common.enums.BusinessType;
import com.hexing.common.utils.ServletUtils;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.framework.web.service.TokenService;
import com.taobao.api.internal.util.json.ExceptionErrorListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 资产表Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@Api(tags = "资产信息管理")
@RestController
@RequestMapping("/asset")
public class AssetController extends BaseController {

    @Autowired
    private IAssetService assetService;
    @Autowired
    private TokenService tokenService;


    /**
     * 查询资产列表
     */
    @ApiOperation("查询资产列表")
    @PreAuthorize("@ss.hasPermi('asset:asset:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetQueryParam param) {
        List<Asset> list = assetService.selectAssetList(param);
        return getDataTable(list);
    }

    /**
     * 导出资产表列表
     */
    @ApiOperation("导出资产表列表")
    @PreAuthorize("@ss.hasPermi('asset:asset:export')")
    @Log(title = "导出资产表列表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetQueryParam param) {
        List<Asset> list = assetService.selectAssetList(param);
        ExcelUtil<Asset> util = new ExcelUtil<>(Asset.class);
        return util.exportExcel(list, "固定资产数据");
    }

    /**
     * SAP初始资产信息导入
     */
    @ApiOperation("SAP初始资产信息导入")
//    @PreAuthorize("@ss.hasPermi('asset:asset:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Asset> util = new ExcelUtil<>(Asset.class);
        List<Asset> assetList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = assetService.importAsset(assetList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    /**
     * 获取资产详细信息
     */
    @ApiOperation("获取资产详细信息")
    @PreAuthorize("@ss.hasPermi('asset:asset:query')")
    @GetMapping(value = "/getInfo/{assetCode}")
    public AjaxResult getInfo(@PathVariable String assetCode) {
        Asset asset = assetService.selectAssetByAssetCode(assetCode);
        if (ObjectUtil.isEmpty(asset)) {
            return AjaxResult.error("资产不存在");
        }
        return AjaxResult.success(asset);
    }

    /**
     * 修改资产表
     */
    @ApiOperation("修改资产表")
    @PreAuthorize("@ss.hasPermi('asset:asset:edit')")
    @Log(title = "修改资产表", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody Asset asset) {
        return toAjax(assetService.updateAsset(asset, null));
    }

    /**
     * SAP采购单同步接口
     */
    @ApiOperation("SAP采购单同步接口")
    @PostMapping("/sapAdd")
    @Transactional
    public AjaxResult sapAdd(@RequestBody SimpleOuterDTO<List<SapPurchaseOrder>> param) {
        List<SapPurchaseOrder> orderList = param.getData();
        if (CollectionUtil.isNotEmpty(orderList)) {
            assetService.sapAdd(orderList);
        }
        return AjaxResult.success();
    }

    /**
     * 获取物料号与资产关系对应关系
     */
    @ApiOperation("获取物料号与资产关系对应关系")
    @GetMapping("/getAssetCategoryTree")
    public AjaxResult getAssetCategoryTree() {
        return AjaxResult.success(CodeUtil.getAssetCategoryTree());
    }

    /**
     * SAP价值传输接口
     */
    @ApiOperation("SAP价值传输接口")
    @PostMapping("/sapSyncValue")
    public AjaxResult sapSyncValue(@RequestBody SimpleOuterDTO<List<SapValueDTO>> param) {
        List<SapValueDTO> sapValueList = param.getData();
        return AjaxResult.success(assetService.sapSyncValue(sapValueList));
    }

    /**
     * 资产转固
     */
    @ApiOperation("资产转固")
    @PreAuthorize("@ss.hasPermi('asset:asset:fixAsset')")
    @Log(title = "资产转固", businessType = BusinessType.OTHER)
    @PostMapping("/fixAsset")
    public AjaxResult fixAsset(AssetFixVO vo) {
        try {
            assetService.fixAsset(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("资产转固失败，请联系管理员排查原因\n错误信息：" + e.getMessage());
        }
        return AjaxResult.success("资产转固成功");
    }

    /**
     * 资产转移
     */
    @ApiOperation("资产转移")
    @PreAuthorize("@ss.hasPermi('asset:asset:transferAsset')")
    @Log(title = "资产转移", businessType = BusinessType.OTHER)
    @PostMapping("/transferAsset")
    public AjaxResult transferAsset(AssetTransferVO vo) {
        try {
            assetService.transferAsset(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("资产转移失败，请联系管理员排查原因\n错误信息：" + e.getMessage());
        }
        return AjaxResult.success("资产转移失败");
    }


}
