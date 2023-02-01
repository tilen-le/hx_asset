package com.hexing.asset.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.dto.MaterialCategorySimpleDTO;
import com.hexing.asset.domain.dto.SapPurchaseOrder;
import com.hexing.asset.domain.vo.AssetQueryParam;
import com.hexing.asset.service.IAssetService;
import com.hexing.asset.utils.CodeUtil;
import com.hexing.common.annotation.Log;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.page.TableDataInfo;
import com.hexing.common.enums.BusinessType;
import com.hexing.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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

    /**
     * 查询资产列表
     */
    @ApiOperation("查询资产列表")
    @PreAuthorize("@ss.hasPermi('asset:collection:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetQueryParam param) {
        startPage();
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
    public AjaxResult export(Asset asset) {
        List<Asset> list = assetService.selectAssetList(asset);
        ExcelUtil<Asset> util = new ExcelUtil<>(Asset.class);
        return util.exportExcel(list, "固定资产数据");
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

//    /**
//     * 新增资产表
//     */
//    @ApiOperation("新增资产表")
//    @PreAuthorize("@ss.hasPermi('asset:asset:add')")
//    @Log(title = "新增资产表", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody Asset asset) {
//        return toAjax(assetService.insertAsset(asset));
//    }

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
    public AjaxResult sapAdd(@RequestBody SapPurchaseOrder order) {
        logger.debug("==== SAP采购单同步接口：开始新建资产信息 ====");
        final String STR_FORMAT = "0000";
        Integer numberOfArrival = order.getNumberOfArrival();
        List<Asset> assetList = new ArrayList<>();
        if (ObjectUtil.isNotNull(numberOfArrival)) {
            LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Asset::getMaterialNum, order.getMaterialNumber())
                    .orderByDesc(Asset::getSerialNum)
                    .last("LIMIT 1");
            Asset theLastOne = assetService.getOne(wrapper);

            int nextNum = ObjectUtil.isNotEmpty(theLastOne) ? theLastOne.getSerialNum() + 1 : 1;
            DecimalFormat df = new DecimalFormat(STR_FORMAT);
            for (int i = 1; i <= numberOfArrival; i++) {
                Asset asset = new Asset();
                String assetCode = order.getMaterialNumber() + df.format(nextNum);
                asset.setMaterialNum(order.getMaterialNumber())
                        .setSerialNum(nextNum)
                        .setAssetName(order.getMaterialText())
                        .setAssetCode(assetCode)
                        .setCompany(order.getCompanyCode())
                        .setCompanyName(order.getCompanyCodeName())
                        .setPurchaseOrderNo(order.getPurchaseOrder())
                        .setProvider(order.getProvider())
                        .setProviderName(order.getProviderDescription())
                        .setOriginalValue(order.getPrice())
                        .setMonetaryUnit(order.getMoneyType())
                        .setCreateBy("SAP")
                        .setCreateTime(new Date());

                MaterialCategorySimpleDTO mcsDto = CodeUtil.parseMaterialNumber(order.getMaterialNumber());
                if (ObjectUtil.isNotEmpty(mcsDto)) {
                    asset.setAssetType(mcsDto.getAssetType())
                            .setAssetCategory(mcsDto.getAssetCategory())
                            .setAssetSubCategory(mcsDto.getAssetSubCategory());
                }

                assetList.add(asset);

                nextNum++;
            }
            assetService.saveBatch(assetList);
        }
        logger.debug("==== SAP采购单同步接口：资产信息新建成功，新增 " + assetList.size() + " 个资产 ====");
        return AjaxResult.success(order);
    }


//    /**
//     * 删除资产表
//     *
//     * @editor 80015306
//     */
//    @ApiOperation("删除资产表")
//    @PreAuthorize("@ss.hasPermi('asset:asset:remove')")
//    @Log(title = "删除资产表", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{assetCodes}")
//    public AjaxResult remove(@PathVariable List<String> assetCodes) {
//        return toAjax(assetService.deleteAssetByAssetCodes(assetCodes));
//    }

}
