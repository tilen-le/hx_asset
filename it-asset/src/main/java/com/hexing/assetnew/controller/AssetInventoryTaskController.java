package com.hexing.assetnew.controller;

import com.hexing.assetnew.domain.AssetInventoryTask;
import com.hexing.asset.domain.dto.AssetInventoryTaskDTO;
import com.hexing.assetnew.service.IAssetInventoryTaskService;
import com.hexing.common.annotation.Log;
import com.hexing.common.annotation.RepeatSubmit;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.page.TableDataInfo;
import com.hexing.common.enums.BusinessType;
import com.hexing.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 盘点任务Controller
 *
 * @author zxy
 * @date 2022-09-13
 */
@RestController
@RequestMapping("/asset/task")
public class AssetInventoryTaskController extends BaseController {

    @Autowired
    private IAssetInventoryTaskService assetProcessCountingTaskService;
    @Autowired
    private IAssetInventoryTaskService assetInventoryTaskService;

    /**
     * 查询盘点任务列表
     */
    @PreAuthorize("@ss.hasPermi('asset:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetInventoryTask assetInventoryTask) {
        List<AssetInventoryTask> list = assetProcessCountingTaskService.selectAssetCountingTaskList(assetInventoryTask);
        return getDataTable(list);
    }

    /**
     * 导出盘点任务列表
     */
    @PreAuthorize("@ss.hasPermi('asset:task:export')")
    @Log(title = "盘点任务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetInventoryTask assetInventoryTask) {
        List<AssetInventoryTask> list = assetProcessCountingTaskService.selectAssetCountingTaskList(assetInventoryTask);
        ExcelUtil<AssetInventoryTask> util = new ExcelUtil<AssetInventoryTask>(AssetInventoryTask.class);
        return util.exportExcel(list, "盘点任务数据");
    }

    /**
     * 新增盘点任务
     */
    @PreAuthorize("@ss.hasPermi('asset:task:add')")
    @Log(title = "盘点任务表", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit(interval = 10000)
    public AjaxResult add(@RequestBody AssetInventoryTaskDTO asset) {
        return toAjax(assetInventoryTaskService.insertAssetCountingTask(asset));
    }

    /**
     * 删除盘点任务
     *
     * @editor 80015306
     */
    @PreAuthorize("@ss.hasPermi('asset:task:remove')")
    @Log(title = "盘点任务表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{taskCodes}")
    public AjaxResult remove(@PathVariable List<String> taskCodes) {
        return toAjax(assetInventoryTaskService.deleteAssetCountingTaskByTaskIds(taskCodes));
    }

}
