package com.hexing.asset.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hexing.asset.domain.AssetManagementConfig;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetUpdateLog;
import com.hexing.asset.domain.vo.AssetProcessParam;
import com.hexing.asset.domain.vo.AssetProcessReturn;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产日志Controller
 *
 * @author zxy
 * @date 2023-01-30
 */
@Api(tags = "资产日志")
@RestController
@RequestMapping("/asset/log")
public class AssetLogController extends BaseController
{
    @Autowired
    private IAssetUpdateLogService updateLogService;

    /**
     * 查询保管记录
     */
    @GetMapping("/custodyLogList")
    @PreAuthorize("@ss.hasPermi('asset:log:custodyLogList')")
    @ApiOperation("查询保管记录")
    @ApiOperationSupport(order = 14)
    public TableDataInfo custodyLogList(AssetProcessParam assetProcess)
    {
        startPage();
        List<AssetUpdateLog> list = updateLogService.custodyLogList(assetProcess);
        return getDataTable(list);
    }

    /**
     * 查询工单记录
     */
    @GetMapping("/workLogList")
    @PreAuthorize("@ss.hasPermi('asset:log:workLogList')")
    @ApiOperation("查询工单记录")
    @ApiOperationSupport(order = 14)
    public TableDataInfo workLogList(AssetProcessParam assetProcess)
    {
        startPage();
        List<AssetProcessReturn> list = updateLogService.workLogList(assetProcess);
        return getDataTable(list);
    }

    /**
     * 查询操作记录
     */
    @GetMapping("/operationLogList")
    @PreAuthorize("@ss.hasPermi('asset:log:operationLogList')")
    @ApiOperation("查询操作记录")
    @ApiOperationSupport(order = 14)
    public TableDataInfo operationLogList(AssetProcessParam assetProcess)
    {
        startPage();
        List<AssetUpdateLog> list = updateLogService.operationLogList(assetProcess);
        return getDataTable(list);
    }

    /**
     * 获取资产操作记录详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取资产操作记录详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        AssetUpdateLog updateLog = updateLogService.getOperationLogById(id);
        if (ObjectUtil.isEmpty(updateLog)) {
            return AjaxResult.error("该资产操作记录不存在");
        }
        return AjaxResult.success(updateLog);
    }

}
