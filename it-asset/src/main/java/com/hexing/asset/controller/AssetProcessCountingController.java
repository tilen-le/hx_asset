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
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.service.IAssetProcessCountingService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 资产盘点流程Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/asset/counting")
public class AssetProcessCountingController extends BaseController
{
    @Autowired
    private IAssetProcessCountingService assetProcessCountingService;

    /**
     * 查询资产盘点流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessCounting assetProcessCounting)
    {
        startPage();
        List<AssetProcessCounting> list = assetProcessCountingService.selectAssetProcessCountingList(assetProcessCounting);
        return getDataTable(list);
    }

    /**
     * 导出资产盘点流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:export')")
    @Log(title = "资产盘点流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessCounting assetProcessCounting)
    {
        List<AssetProcessCounting> list = assetProcessCountingService.selectAssetProcessCountingList(assetProcessCounting);
        ExcelUtil<AssetProcessCounting> util = new ExcelUtil<AssetProcessCounting>(AssetProcessCounting.class);
        return util.exportExcel(list, "资产盘点流程数据");
    }

    /**
     * 获取资产盘点流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetProcessCountingService.selectAssetProcessCountingById(id));
    }

    /**
     * 新增资产盘点流程
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:add')")
    @Log(title = "资产盘点流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetProcessCounting assetProcessCounting)
    {
        return toAjax(assetProcessCountingService.insertAssetProcessCounting(assetProcessCounting));
    }

    /**
     * 修改资产盘点流程
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:edit')")
    @Log(title = "资产盘点流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcessCounting assetProcessCounting)
    {
        return toAjax(assetProcessCountingService.updateAssetProcessCounting(assetProcessCounting));
    }

    /**
     * 删除资产盘点流程
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:remove')")
    @Log(title = "资产盘点流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetProcessCountingService.deleteAssetProcessCountingByIds(ids));
    }
}
