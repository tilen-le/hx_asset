package com.hexing.asset.controller;

import java.util.List;

import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetInventoryTask;
import com.hexing.asset.domain.dto.AssetInventoryTaskDTO;
import com.hexing.asset.service.IAssetInventoryTaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hexing.common.annotation.Log;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.enums.BusinessType;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 盘点任务Controller
 *
 * @author zxy
 * @date 2022-09-13
 */
@RestController
@RequestMapping("/asset/task")
public class AssetInventoryTaskController extends BaseController
{
    @Autowired
    private IAssetInventoryTaskService assetProcessCountingTaskService;

    /**
     * 查询盘点任务列表
     */
    @PreAuthorize("@ss.hasPermi('asset:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetInventoryTask assetInventoryTask)
    {
        List<AssetInventoryTask> list = assetProcessCountingTaskService.selectAssetCountingTaskList(assetInventoryTask);
        return getDataTable(list);
    }

    /**
     * 导出盘点任务列表
     */
    @PreAuthorize("@ss.hasPermi('asset:task:export')")
    @Log(title = "盘点任务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetInventoryTask assetInventoryTask)
    {
        List<AssetInventoryTask> list = assetProcessCountingTaskService.selectAssetCountingTaskList(assetInventoryTask);
        ExcelUtil<AssetInventoryTask> util = new ExcelUtil<AssetInventoryTask>(AssetInventoryTask.class);
        return util.exportExcel(list, "盘点任务数据");
    }

    /**
     * 获取盘点任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('mature:task:query')")
    @GetMapping(value = "/{taskId}")
    public AjaxResult getInfo(@PathVariable("taskId") String taskId)
    {
        return AjaxResult.success(assetProcessCountingTaskService.selectAssetCountingTaskByTaskId(taskId));
    }

    /**
     * 新增盘点任务
     */
//    @PreAuthorize("@ss.hasPermi('mature:task:add')")
    @Log(title = "盘点任务表", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody AssetInventoryTaskDTO asset) {
        return toAjax(assetProcessCountingTaskService.insertAssetCountingTask(asset));
    }



}
