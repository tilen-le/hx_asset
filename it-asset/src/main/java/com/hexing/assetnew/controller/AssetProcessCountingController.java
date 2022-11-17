package com.hexing.assetnew.controller;

import com.alibaba.fastjson.JSONObject;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.service.IAssetProcessCountingService;
import com.hexing.assetnew.domain.AssetProcessCountingDomain;
import com.hexing.assetnew.domain.AssetsProcess;
import com.hexing.assetnew.service.IAssetsProcessService;
import com.hexing.common.annotation.Log;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.page.TableDataInfo;
import com.hexing.common.enums.BusinessType;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 资产盘点流程Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@Api(tags = "资产盘点记录管理")
@RestController
@RequestMapping("/asset/counting")
public class AssetProcessCountingController extends BaseController {

    @Autowired
    private IAssetProcessCountingService assetProcessCountingService;
    @Autowired
    private IAssetsProcessService processService;

    /**
     * 查询资产盘点流程列表
     */
    @ApiOperation("获取盘点记录列表")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessCountingDomain assetProcessCountingDomain) {
        List<AssetsProcess> list = processService.listByPage(assetProcessCountingDomain);
        List<AssetProcessCountingDomain> domains = new ArrayList<>();
        for (AssetsProcess assetsProcess : list) {
            AssetProcessCountingDomain domain = processService.convertProcess(assetsProcess, new AssetProcessCountingDomain());
            domains.add(domain);
        }
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setRows(domains);
        return dataTable;
    }

    /**
     * 盘点统计
     */
    @ApiOperation("盘点统计")
    @GetMapping("/inventoryCount")
    public AjaxResult inventoryCount(String type, String startDate, String endDate) {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return AjaxResult.error("起止日期未设置");
        }
        JSONObject jsonObject = assetProcessCountingService.inventoryCount(type, startDate, endDate);
        return AjaxResult.success(jsonObject);
    }

    /**
     * 盘点统计列表
     */
    @ApiOperation("盘点统计列表")
    @GetMapping("/inventoryCountList")
    public TableDataInfo inventoryCountList(String startDate, String endDate) {
        startPage();
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return new TableDataInfo();
        }
        List<AssetProcessCounting> list = assetProcessCountingService.inventoryCountList(startDate, endDate);
        TableDataInfo dataTable = getDataTable(list);
        return dataTable;
    }

    /**
     * 盘点状态统计
     */
    @ApiOperation("盘点状态统计")
    @GetMapping("/countingStatusCount")
    public AjaxResult countingStatusCount(String taskCode) {
        return AjaxResult.success(processService.countingStatusCount(taskCode));
    }

    /**
     * 导出资产盘点流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:export')")
    @Log(title = "资产盘点流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessCountingDomain assetProcessCountingDomain) {
        List<AssetsProcess> list = processService.list(assetProcessCountingDomain);
        List<AssetProcessCountingDomain> domains = new ArrayList<>();
        for (AssetsProcess assetsProcess : list) {
            AssetProcessCountingDomain domain = processService.convertProcess(assetsProcess, new AssetProcessCountingDomain());
            domains.add(domain);
        }
        ExcelUtil<AssetProcessCountingDomain> util = new ExcelUtil<>(AssetProcessCountingDomain.class);
        return util.exportExcel(domains, "资产盘点记录");
    }

}
