package com.hexing.asset.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hexing.common.annotation.Log;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.enums.BusinessType;
import com.hexing.asset.domain.AssetProcessCountingTask;
import com.hexing.asset.service.IAssetProcessCountingTaskService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 资产盘点任务流程Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/asset/task")
public class AssetProcessCountingTaskController extends BaseController
{
    @Autowired
    private IAssetProcessCountingTaskService assetProcessCountingTaskService;

    /**
     * 查询资产盘点任务流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessCountingTask assetProcessCountingTask)
    {
        startPage();
        List<AssetProcessCountingTask> list = assetProcessCountingTaskService.selectAssetProcessCountingTaskList(assetProcessCountingTask);
        return getDataTable(list);
    }

    /**
     * 导出资产盘点任务流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:task:export')")
    @Log(title = "资产盘点任务流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessCountingTask assetProcessCountingTask)
    {
        List<AssetProcessCountingTask> list = assetProcessCountingTaskService.selectAssetProcessCountingTaskList(assetProcessCountingTask);
        ExcelUtil<AssetProcessCountingTask> util = new ExcelUtil<AssetProcessCountingTask>(AssetProcessCountingTask.class);
        return util.exportExcel(list, "资产盘点任务流程数据");
    }

    /**
     * 获取资产盘点任务流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetProcessCountingTaskService.selectAssetProcessCountingTaskById(id));
    }

    /**
     * 新增资产盘点任务流程
     */
    @PreAuthorize("@ss.hasPermi('asset:task:add')")
    @Log(title = "资产盘点任务流程", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody AssetProcessCountingTask assetProcessCountingTask)
    {
        System.out.println("assetProcessCountingTask: "+assetProcessCountingTask.toString());
        int i = assetProcessCountingTaskService.insertAssetProcessCountingTask(assetProcessCountingTask);
        return toAjax(i);
    }

    /**
     * 修改资产盘点任务流程
     */
    @PreAuthorize("@ss.hasPermi('asset:task:edit')")
    @Log(title = "资产盘点任务流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcessCountingTask assetProcessCountingTask)
    {
        return toAjax(assetProcessCountingTaskService.updateAssetProcessCountingTask(assetProcessCountingTask));
    }

    /**
     * 删除资产盘点任务流程
     */
    @PreAuthorize("@ss.hasPermi('asset:task:remove')")
    @Log(title = "资产盘点任务流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetProcessCountingTaskService.deleteAssetProcessCountingTaskByIds(ids));
    }
}
