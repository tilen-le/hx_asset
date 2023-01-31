package com.hexing.assetNew.controller;

import java.util.List;

import com.hexing.assetNew.domain.AssetInfo;
import com.hexing.assetNew.service.IAssetInfoService;
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
 * 资产Controller
 *
 * @author zxy
 * @date 2022-11-01
 */
@RestController
@RequestMapping("/asset/info")
public class AssetInfoController extends BaseController
{
    @Autowired
    private IAssetInfoService assetInfoService;

    /**
     * 查询资产列表
     */
    @PreAuthorize("@ss.hasPermi('asset:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetInfo assetInfo)
    {
        startPage();
        List<AssetInfo> list = assetInfoService.selectAssetInfoList(assetInfo);
        return getDataTable(list);
    }

    /**
     * 导出资产列表
     */
    @PreAuthorize("@ss.hasPermi('asset:info:export')")
    @Log(title = "资产", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetInfo assetInfo)
    {
        List<AssetInfo> list = assetInfoService.selectAssetInfoList(assetInfo);
        ExcelUtil<AssetInfo> util = new ExcelUtil<AssetInfo>(AssetInfo.class);
        return util.exportExcel(list, "资产数据");
    }

    /**
     * 获取资产详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:info:query')")
    @GetMapping(value = "/{assetId}")
    public AjaxResult getInfo(@PathVariable("assetId") Long assetId)
    {
        return AjaxResult.success(assetInfoService.selectAssetInfoByAssetId(assetId));
    }

    /**
     * 新增资产
     */
    @PreAuthorize("@ss.hasPermi('asset:info:add')")
    @Log(title = "资产", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetInfo assetInfo)
    {
        return toAjax(assetInfoService.insertAssetInfo(assetInfo));
    }

    /**
     * 修改资产
     */
    @PreAuthorize("@ss.hasPermi('asset:info:edit')")
    @Log(title = "资产", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetInfo assetInfo)
    {
        return toAjax(assetInfoService.updateAssetInfo(assetInfo));
    }

    /**
     * 删除资产
     */
    @PreAuthorize("@ss.hasPermi('asset:info:remove')")
    @Log(title = "资产", businessType = BusinessType.DELETE)
	@DeleteMapping("/{assetIds}")
    public AjaxResult remove(@PathVariable Long[] assetIds)
    {
        return toAjax(assetInfoService.deleteAssetInfoByAssetIds(assetIds));
    }
}
