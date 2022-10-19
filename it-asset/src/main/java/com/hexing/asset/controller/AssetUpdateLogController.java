package com.hexing.asset.controller;

import java.util.List;

import com.hexing.asset.domain.AssetUpdateLog;
import com.hexing.asset.service.IAssetUpdateLogService;
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
 * 资产信息更新日志Controller
 *
 * @author zxy
 * @date 2022-10-19
 */
@RestController
@RequestMapping("/mature/log")
public class AssetUpdateLogController extends BaseController
{
    @Autowired
    private IAssetUpdateLogService assetUpdateLogService;

    /**
     * 查询资产信息更新日志列表
     */
    @PreAuthorize("@ss.hasPermi('mature:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetUpdateLog assetUpdateLog)
    {
        startPage();
        List<AssetUpdateLog> list = assetUpdateLogService.selectAssetUpdateLogList(assetUpdateLog);
        return getDataTable(list);
    }

    /**
     * 导出资产信息更新日志列表
     */
    @PreAuthorize("@ss.hasPermi('mature:log:export')")
    @Log(title = "资产信息更新日志", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetUpdateLog assetUpdateLog)
    {
        List<AssetUpdateLog> list = assetUpdateLogService.selectAssetUpdateLogList(assetUpdateLog);
        ExcelUtil<AssetUpdateLog> util = new ExcelUtil<AssetUpdateLog>(AssetUpdateLog.class);
        return util.exportExcel(list, "资产信息更新日志数据");
    }

    /**
     * 获取资产信息更新日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('mature:log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetUpdateLogService.selectAssetUpdateLogById(id));
    }

    /**
     * 新增资产信息更新日志
     */
    @PreAuthorize("@ss.hasPermi('mature:log:add')")
    @Log(title = "资产信息更新日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetUpdateLog assetUpdateLog)
    {
        return toAjax(assetUpdateLogService.insertAssetUpdateLog(assetUpdateLog));
    }

    /**
     * 修改资产信息更新日志
     */
    @PreAuthorize("@ss.hasPermi('mature:log:edit')")
    @Log(title = "资产信息更新日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetUpdateLog assetUpdateLog)
    {
        return toAjax(assetUpdateLogService.updateAssetUpdateLog(assetUpdateLog));
    }

    /**
     * 删除资产信息更新日志
     */
    @PreAuthorize("@ss.hasPermi('mature:log:remove')")
    @Log(title = "资产信息更新日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetUpdateLogService.deleteAssetUpdateLogByIds(ids));
    }
}
