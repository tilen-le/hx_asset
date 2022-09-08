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
import com.hexing.asset.domain.AssetProcessBack;
import com.hexing.asset.service.IAssetProcessBackService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 资产归还流程Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/asset/back")
public class AssetProcessBackController extends BaseController
{
    @Autowired
    private IAssetProcessBackService assetProcessBackService;

    /**
     * 查询资产归还流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:back:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessBack assetProcessBack)
    {
        startPage();
        List<AssetProcessBack> list = assetProcessBackService.selectAssetProcessBackList(assetProcessBack);
        return getDataTable(list);
    }

    /**
     * 导出资产归还流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:back:export')")
    @Log(title = "资产归还流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessBack assetProcessBack)
    {
        List<AssetProcessBack> list = assetProcessBackService.selectAssetProcessBackList(assetProcessBack);
        ExcelUtil<AssetProcessBack> util = new ExcelUtil<AssetProcessBack>(AssetProcessBack.class);
        return util.exportExcel(list, "资产归还流程数据");
    }

    /**
     * 获取资产归还流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:back:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetProcessBackService.selectAssetProcessBackById(id));
    }

    /**
     * 新增资产归还流程
     */
    @PreAuthorize("@ss.hasPermi('asset:back:add')")
    @Log(title = "资产归还流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetProcessBack assetProcessBack)
    {
        return toAjax(assetProcessBackService.insertAssetProcessBack(assetProcessBack));
    }

    /**
     * 修改资产归还流程
     */
    @PreAuthorize("@ss.hasPermi('asset:back:edit')")
    @Log(title = "资产归还流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcessBack assetProcessBack)
    {
        return toAjax(assetProcessBackService.updateAssetProcessBack(assetProcessBack));
    }

    /**
     * 删除资产归还流程
     */
    @PreAuthorize("@ss.hasPermi('asset:back:remove')")
    @Log(title = "资产归还流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetProcessBackService.deleteAssetProcessBackByIds(ids));
    }
}
