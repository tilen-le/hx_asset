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
import com.hexing.asset.domain.Asset;
import com.hexing.asset.service.IAssetService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 资产表Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/asset/asset")
public class AssetController extends BaseController
{
    @Autowired
    private IAssetService assetService;

    /**
     * 查询资产表列表
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:list')")
    @GetMapping("/list")
    public TableDataInfo list(Asset asset)
    {
        startPage();
        List<Asset> list = assetService.selectAssetList(asset);
        return getDataTable(list);
    }

    /**
     * 导出资产表列表
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:export')")
    @Log(title = "资产表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Asset asset)
    {
        List<Asset> list = assetService.selectAssetList(asset);
        ExcelUtil<Asset> util = new ExcelUtil<Asset>(Asset.class);
        return util.exportExcel(list, "资产表数据");
    }

    /**
     * 获取资产表详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:query')")
    @GetMapping(value = "/{assetId}")
    public AjaxResult getInfo(@PathVariable("assetId") Long assetId)
    {
        return AjaxResult.success(assetService.selectAssetByAssetId(assetId));
    }

    /**
     * 新增资产表
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:add')")
    @Log(title = "资产表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Asset asset)
    {
        return toAjax(assetService.insertAsset(asset));
    }

    /**
     * 修改资产表
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:edit')")
    @Log(title = "资产表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Asset asset)
    {
        return toAjax(assetService.updateAsset(asset));
    }

    /**
     * 删除资产表
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:remove')")
    @Log(title = "资产表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{assetIds}")
    public AjaxResult remove(@PathVariable Long[] assetIds)
    {
        return toAjax(assetService.deleteAssetByAssetIds(assetIds));
    }
}
