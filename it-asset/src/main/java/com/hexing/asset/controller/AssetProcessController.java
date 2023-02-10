package com.hexing.asset.controller;

import com.hexing.asset.domain.vo.AssetProcessParam;
import com.hexing.asset.service.IAssetProcessService;
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
     * 资产派发
     */
    @Log(title = "资产派发", businessType = BusinessType.UPDATE)
    @PutMapping("/receiveAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:receiveAsset')")
    @ApiOperation("资产派发")
    public AjaxResult receiveAsset(@RequestBody AssetProcessParam assetProcess)
    {
        return toAjax(processService.receiveAsset(assetProcess));
    }

    /**
     * 归还资产
     */
    @Log(title = "归还资产", businessType = BusinessType.UPDATE)
    @PutMapping("/backAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:backAsset')")
    @ApiOperation("归还资产")
    public AjaxResult backAsset(@RequestBody AssetProcessParam assetProcess)
    {
        return toAjax(processService.backAsset(assetProcess));
    }

    /**
     * 转固资产
     */
    @Log(title = "转固资产", businessType = BusinessType.UPDATE)
    @PutMapping("/fixationAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:transferAsset')")
    @ApiOperation("转固资产")
    public AjaxResult fixationAsset(@RequestBody AssetProcessParam assetProcess)
    {
        return toAjax(processService.fixationAsset(assetProcess));
    }

    /**
     * 转移资产
     */
    @Log(title = "转移资产", businessType = BusinessType.UPDATE)
    @PutMapping("/transferAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:transferAsset')")
    @ApiOperation("转固资产")
    public AjaxResult transferAsset(@RequestBody AssetProcessParam assetProcess)
    {
        return toAjax(processService.transferAsset(assetProcess));
    }

    /**
     * 资产待外卖
     */
    @Log(title = "资产待外卖", businessType = BusinessType.UPDATE)
    @PutMapping("/waiteTakeOutAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:waiteTakeOutAsset')")
    @ApiOperation("资产待外卖")
    public AjaxResult waiteTakeOutAsset(@RequestBody AssetProcessParam assetProcess)
    {
        return toAjax(processService.waiteTakeOutAsset(assetProcess));
    }

    /**
     * 外卖资产
     */
    @Log(title = "外卖资产", businessType = BusinessType.UPDATE)
    @PutMapping("/takeOutAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:takeOutAsset')")
    @ApiOperation("外卖资产")
    public AjaxResult takeOutAsset(@RequestBody AssetProcessParam assetProcess)
    {
        return toAjax(processService.takeOutAsset(assetProcess));
    }

    /**
     * 外卖资产
     */
    @Log(title = "资产返修", businessType = BusinessType.UPDATE)
    @PutMapping("/repairAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:repairAsset')")
    @ApiOperation("资产返修")
    public AjaxResult repairAsset(@RequestBody AssetProcessParam assetProcess)
    {
        return toAjax(processService.repairAsset(assetProcess));
    }

    /**
     * 资产退货
     */
    @Log(title = "资产退货", businessType = BusinessType.UPDATE)
    @PutMapping("/returnAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:returnAsset')")
    @ApiOperation("资产退货")
    public AjaxResult returnAsset(@RequestBody AssetProcessParam assetProcess)
    {
        return toAjax(processService.returnAsset(assetProcess));
    }

    /**
     * 资产维修
     */
    @Log(title = "资产维修", businessType = BusinessType.UPDATE)
    @PutMapping("/maintainAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:maintainAsset')")
    @ApiOperation("资产维修")
    public AjaxResult maintainAsset(@RequestBody AssetProcessParam assetProcess)
    {
        return toAjax(processService.maintainAsset(assetProcess));
    }

    /**
     * 资产已维修
     */
    @Log(title = "资产已维修", businessType = BusinessType.UPDATE)
    @PutMapping("/maintainedAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:maintainedAsset')")
    @ApiOperation("资产已维修")
    public AjaxResult maintainedAsset(@RequestBody AssetProcessParam assetProcess)
    {
        return toAjax(processService.maintainedAsset(assetProcess));
    }

    /**
     * 资产报废
     */
    @Log(title = "资产报废", businessType = BusinessType.UPDATE)
    @PutMapping("/scrapAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:scrapAsset')")
    @ApiOperation("资产报废")
    public AjaxResult scrapAsset(@RequestBody AssetProcessParam assetProcess)
    {
        return toAjax(processService.scrapAsset(assetProcess));
    }

    /**
     * 资产已报废
     */
    @Log(title = "资产已报废", businessType = BusinessType.UPDATE)
    @PutMapping("/scrapedAsset")
//    @PreAuthorize("@ss.hasPermi('asset:process:scrapedAsset')")
    @ApiOperation("资产已报废")
    public AjaxResult scrapedAsset(@RequestBody AssetProcessParam assetProcess) {
        return toAjax(processService.scrapedAsset(assetProcess));
    }
}
