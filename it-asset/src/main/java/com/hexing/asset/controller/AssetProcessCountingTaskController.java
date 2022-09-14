package com.hexing.asset.controller;

import java.util.List;

import com.hexing.asset.domain.AssetProcessCountingTask;
import com.hexing.asset.service.IAssetProcessCountingTaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
public class AssetProcessCountingTaskController extends BaseController
{
    @Autowired
    private IAssetProcessCountingTaskService assetProcessCountingTaskService;

    /**
     * 查询盘点任务列表
     */
    @PreAuthorize("@ss.hasPermi('asset:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessCountingTask assetProcessCountingTask)
    {
        startPage();
        List<AssetProcessCountingTask> list = assetProcessCountingTaskService.selectAssetCountingTaskList(assetProcessCountingTask);
        return getDataTable(list);
    }

    /**
     * 导出盘点任务列表
     */
    @PreAuthorize("@ss.hasPermi('asset:task:export')")
    @Log(title = "盘点任务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessCountingTask assetProcessCountingTask)
    {
        List<AssetProcessCountingTask> list = assetProcessCountingTaskService.selectAssetCountingTaskList(assetProcessCountingTask);
        ExcelUtil<AssetProcessCountingTask> util = new ExcelUtil<AssetProcessCountingTask>(AssetProcessCountingTask.class);
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




}
