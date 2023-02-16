package com.hexing.asset.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hexing.asset.domain.AssetUpdateLog;
import com.hexing.asset.domain.vo.AssetProcessParam;
import com.hexing.asset.domain.vo.AssetProcessReturn;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.common.annotation.Log;
import com.hexing.common.annotation.RepeatSubmit;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Api(tags = "资产流程管理")
@RestController
@RequestMapping("/asset/process")
public class AssetProcessController extends BaseController {

    @Autowired
    private IAssetProcessService processService;

    /**
     * 资产派发
     */
    @Log(title = "资产派发", businessType = BusinessType.UPDATE)
    @PutMapping("/receiveAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:receiveAsset')")
    @ApiOperation("资产派发")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 1)
    public AjaxResult receiveAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.receiveAsset(assetProcess));
    }

    /**
     * 转移资产
     */
    @Log(title = "转移资产", businessType = BusinessType.UPDATE)
    @PutMapping("/transferAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:transferAsset')")
    @ApiOperation("转固资产")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 2)
    public AjaxResult transferAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.transferAsset(assetProcess));
    }

    /**
     * 资产退货
     */
    @Log(title = "资产退货", businessType = BusinessType.UPDATE)
    @PutMapping("/returnAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:returnAsset')")
    @ApiOperation("资产退货")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 3)
    public AjaxResult returnAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.returnAsset(assetProcess));
    }

    /**
     * 转固资产
     */
    @Log(title = "转固资产", businessType = BusinessType.UPDATE)
    @PutMapping("/fixationAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:fixationAsset')")
    @ApiOperation("转固资产")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 4)
    public AjaxResult fixationAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.fixationAsset(assetProcess));
    }

    /**
     * 资产维修
     */
    @Log(title = "资产维修", businessType = BusinessType.UPDATE)
    @PutMapping("/maintainAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:maintainAsset')")
    @ApiOperation("资产维修")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 5)
    public AjaxResult maintainAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.maintainAsset(assetProcess));
    }

    /**
     * 资产闲置
     */
    @Log(title = "资产闲置", businessType = BusinessType.UPDATE)
    @PutMapping("/unusedAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:unusedAsset')")
    @ApiOperation("资产闲置")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 6)
    public AjaxResult unusedAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.unusedAsset(assetProcess));
    }

    /**
     * 资产报废
     */
    @Log(title = "资产报废", businessType = BusinessType.UPDATE)
    @PutMapping("/scrapAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:scrapAsset')")
    @ApiOperation("资产报废")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 7)
    public AjaxResult scrapAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.scrapAsset(assetProcess));
    }

    /**
     * 资产待外卖
     */
    @Log(title = "资产待外卖", businessType = BusinessType.UPDATE)
    @PutMapping("/waiteTakeOutAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:waiteTakeOutAsset')")
    @ApiOperation("资产待外卖")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 8)
    public AjaxResult waiteTakeOutAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.waiteTakeOutAsset(assetProcess));
    }

    /**
     * 资产盘亏
     */
    @Log(title = "资产盘亏", businessType = BusinessType.UPDATE)
    @PutMapping("/inventoryLossAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:inventoryLossAsset')")
    @ApiOperation("资产盘亏")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 9)
    public AjaxResult inventoryLossAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.inventoryLossAsset(assetProcess));
    }

    /**
     * 资产已维修
     */
    @Log(title = "资产已维修", businessType = BusinessType.UPDATE)
    @PutMapping("/maintainedAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:maintainedAsset')")
    @ApiOperation("资产已维修")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 10)
    public AjaxResult maintainedAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.maintainedAsset(assetProcess));
    }

    /**
     * 已外卖资产
     */
    @Log(title = "已外卖资产", businessType = BusinessType.UPDATE)
    @PutMapping("/takeOutAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:takeOutAsset')")
    @ApiOperation("已外卖资产")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 11)
    public AjaxResult takeOutAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.takeOutAsset(assetProcess));
    }

    /**
     * 资产已报废
     */
    @Log(title = "资产已报废", businessType = BusinessType.UPDATE)
    @PutMapping("/scrapedAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:scrapedAsset')")
    @ApiOperation("资产已报废")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 12)
    public AjaxResult scrapedAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.scrapedAsset(assetProcess));
    }

    /**
     * 资产账务转移
     */
    @Log(title = "资产账务转移", businessType = BusinessType.UPDATE)
    @PutMapping("/accountTransferAsset")
    @PreAuthorize("@ss.hasPermi('asset:process:accountTransferAsset')")
    @ApiOperation("资产账务转移")
    @RepeatSubmit(interval = 3000)
    @ApiOperationSupport(order = 12)
    public AjaxResult accountTransferAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.accountTransferAsset(assetProcess));
    }

    /**
     * 获取转移详情
     */
    @GetMapping("/getTransferInfo/{assetCode}")
    @ApiOperation("获取转移详情")
    @ApiOperationSupport(order = 13)
    public AjaxResult getTransferInfo(@PathVariable("assetCode") String assetCode) {
        AssetProcessReturn assetProcessReturn = processService.getTransferInfo(assetCode);
        if (ObjectUtil.isEmpty(assetProcessReturn)) {
            return AjaxResult.error("该资产转移记录不存在");
        }
        return AjaxResult.success(assetProcessReturn);
    }
}
