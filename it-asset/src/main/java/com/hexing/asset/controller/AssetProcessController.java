package com.hexing.asset.controller;

import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetManagementConfig;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.asset.service.IAssetService;
import com.hexing.common.annotation.Log;
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
     * 归还资产
     */
    @Log(title = "归还资产", businessType = BusinessType.UPDATE)
    @PutMapping("/backAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:backAsset')")
    @ApiOperation("归还资产")
    public AjaxResult backAsset(@RequestBody Asset asset)
    {
        return toAjax(processService.backAsset(asset));
    }

    /**
     * 转固资产
     */
    @Log(title = "转固资产", businessType = BusinessType.UPDATE)
    @PutMapping("/fixationAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:transferAsset')")
    @ApiOperation("转固资产")
    public AjaxResult fixationAsset(@RequestBody Asset asset)
    {
        return toAjax(processService.fixationAsset(asset));
    }

    /**
     * 转移资产
     */
    @Log(title = "转移资产", businessType = BusinessType.UPDATE)
    @PutMapping("/transferAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:transferAsset')")
    @ApiOperation("转固资产")
    public AjaxResult transferAsset(@RequestBody Asset asset,String recipient)
    {
        return toAjax(processService.transferAsset(asset,recipient));
    }

    /**
     * 资产待外卖
     */
    @Log(title = "资产待外卖", businessType = BusinessType.UPDATE)
    @PutMapping("/waiteTakeOutAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:waiteTakeOutAsset')")
    @ApiOperation("资产待外卖")
    public AjaxResult waiteTakeOutAsset(@RequestBody Asset asset)
    {
        return toAjax(processService.waiteTakeOutAsset(asset));
    }

    /**
     * 外卖资产
     */
    @Log(title = "外卖资产", businessType = BusinessType.UPDATE)
    @PutMapping("/takeOutAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:takeOutAsset')")
    @ApiOperation("外卖资产")
    public AjaxResult takeOutAsset(@RequestBody Asset asset)
    {
        return toAjax(processService.takeOutAsset(asset));
    }

    /**
     * 外卖资产
     */
    @Log(title = "资产返修", businessType = BusinessType.UPDATE)
    @PutMapping("/repairAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:repairAsset')")
    @ApiOperation("资产返修")
    public AjaxResult repairAsset(@RequestBody Asset asset)
    {
        return toAjax(processService.repairAsset(asset));
    }

    /**
     * 资产领用
     */
    @Log(title = "资产领用", businessType = BusinessType.UPDATE)
    @PutMapping("/receiveAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:receiveAsset')")
    @ApiOperation("资产领用")
    public AjaxResult receiveAsset(@RequestBody Asset asset)
    {
        return toAjax(processService.receiveAsset(asset));
    }

    /**
     * 资产退货
     */
    @Log(title = "资产退货", businessType = BusinessType.UPDATE)
    @PutMapping("/returnAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:returnAsset')")
    @ApiOperation("资产退货")
    public AjaxResult returnAsset(@RequestBody Asset asset)
    {
        return toAjax(processService.returnAsset(asset));
    }

    /**
     * 资产维修
     */
    @Log(title = "资产维修", businessType = BusinessType.UPDATE)
    @PutMapping("/maintainAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:maintainAsset')")
    @ApiOperation("资产维修")
    public AjaxResult maintainAsset(@RequestBody Asset asset)
    {
        return toAjax(processService.maintainAsset(asset));
    }

    /**
     * 资产已维修
     */
    @Log(title = "资产已维修", businessType = BusinessType.UPDATE)
    @PutMapping("/maintainedAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:maintainedAsset')")
    @ApiOperation("资产已维修")
    public AjaxResult maintainedAsset(@RequestBody Asset asset)
    {
        return toAjax(processService.maintainedAsset(asset));
    }

    /**
     * 资产报废
     */
    @Log(title = "资产报废", businessType = BusinessType.UPDATE)
    @PutMapping("/scrapAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:scrapAsset')")
    @ApiOperation("资产报废")
    public AjaxResult scrapAsset(@RequestBody Asset asset)
    {
        return toAjax(processService.scrapAsset(asset));
    }

    /**
     * 资产已报废
     */
    @Log(title = "资产已报废", businessType = BusinessType.UPDATE)
    @PutMapping("/scrapedAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:scrapedAsset')")
    @ApiOperation("资产已报废")
    public AjaxResult scrapedAsset(@RequestBody Asset asset) {
        return toAjax(processService.scrapedAsset(asset));
    }
}
