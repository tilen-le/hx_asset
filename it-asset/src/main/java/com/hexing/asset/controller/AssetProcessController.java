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
    @PutMapping("/back")
//    @PreAuthorize("@ss.hasPermi('asset:process:back')")
    @ApiOperation("归还资产")
    public AjaxResult back(@RequestBody Asset asset)
    {
        return toAjax(processService.backAsset(asset));
    }

    /**
     * 转固资产
     */
    @Log(title = "转固资产", businessType = BusinessType.UPDATE)
    @PutMapping("/fixation")
//    @PreAuthorize("@ss.hasPermi('asset:process:fixation')")
    @ApiOperation("转固资产")
    public AjaxResult fixation(@RequestBody Asset asset)
    {
        return toAjax(processService.fixationAsset(asset));
    }


}
