package com.hexing.assetNew.controller;

import com.hexing.assetNew.domain.dto.ReceiveProcessDTO;
import com.hexing.assetNew.domain.dto.ProcessCommonDTO;
import com.hexing.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "资产领用流程管理")
@RestController
@RequestMapping("/asset/receive")
public class AssetReceiveProcessController {

    // TODO 领用流程
    @ApiOperation("资产领用流程")
    @PostMapping(value = "/assetReceive")
    public String assetReceive(@RequestBody ProcessCommonDTO<ReceiveProcessDTO> params) {
        return null;
    }

    /**
     * TODO 导出领用流程信息
     */
    @ApiOperation("导出领用流程信息")
    @PreAuthorize("@ss.hasPermi('asset:receive:export')")
    @GetMapping("/export")
    public AjaxResult export() {
        return null;
    }

}
