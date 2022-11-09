package com.hexing.asset.controller;

import java.util.List;

import com.hexing.asset.domain.AssetHistory;
import com.hexing.asset.service.IAssetHistoryService;
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
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 资产历史记录Controller
 *
 * @author zxy
 * @date 2022-11-01
 */
@RestController
@RequestMapping("/asset/history")
public class AssetHistoryController extends BaseController
{
    @Autowired
    private IAssetHistoryService assetHistoryService;

    /**
     * 查询资产历史记录列表
     */
    @PreAuthorize("@ss.hasPermi('asset:history:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetHistory assetHistory)
    {
        startPage();
        List<AssetHistory> list = assetHistoryService.selectAssetHistoryList(assetHistory);
        return getDataTable(list);
    }

    /**
     * 导出资产历史记录列表
     */
    @PreAuthorize("@ss.hasPermi('asset:history:export')")
    @Log(title = "资产历史记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetHistory assetHistory)
    {
        List<AssetHistory> list = assetHistoryService.selectAssetHistoryList(assetHistory);
        ExcelUtil<AssetHistory> util = new ExcelUtil<AssetHistory>(AssetHistory.class);
        return util.exportExcel(list, "资产历史记录数据");
    }

    /**
     * 获取资产历史记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:history:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetHistoryService.selectAssetHistoryById(id));
    }

    /**
     * 新增资产历史记录
     */
    @PreAuthorize("@ss.hasPermi('asset:history:add')")
    @Log(title = "资产历史记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetHistory assetHistory)
    {
        return toAjax(assetHistoryService.insertAssetHistory(assetHistory));
    }

    /**
     * 修改资产历史记录
     */
    @PreAuthorize("@ss.hasPermi('asset:history:edit')")
    @Log(title = "资产历史记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetHistory assetHistory)
    {
        return toAjax(assetHistoryService.updateAssetHistory(assetHistory));
    }

    /**
     * 删除资产历史记录
     */
    @PreAuthorize("@ss.hasPermi('asset:history:remove')")
    @Log(title = "资产历史记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetHistoryService.deleteAssetHistoryByIds(ids));
    }
}
