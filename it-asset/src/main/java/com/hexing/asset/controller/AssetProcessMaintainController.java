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
import com.hexing.asset.domain.AssetProcessMaintain;
import com.hexing.asset.service.IAssetProcessMaintainService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 资产维修流程Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/asset/maintain")
public class AssetProcessMaintainController extends BaseController
{
    @Autowired
    private IAssetProcessMaintainService assetProcessMaintainService;

    /**
     * 查询资产维修流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:maintain:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessMaintain assetProcessMaintain)
    {
        startPage();
        List<AssetProcessMaintain> list = assetProcessMaintainService.selectAssetProcessMaintainList(assetProcessMaintain);
        return getDataTable(list);
    }

    /**
     * 导出资产维修流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:maintain:export')")
    @Log(title = "资产维修流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessMaintain assetProcessMaintain)
    {
        List<AssetProcessMaintain> list = assetProcessMaintainService.selectAssetProcessMaintainList(assetProcessMaintain);
        ExcelUtil<AssetProcessMaintain> util = new ExcelUtil<AssetProcessMaintain>(AssetProcessMaintain.class);
        return util.exportExcel(list, "资产维修流程数据");
    }

    /**
     * 获取资产维修流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:maintain:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetProcessMaintainService.selectAssetProcessMaintainById(id));
    }

    /**
     * 新增资产维修流程
     */
    @PreAuthorize("@ss.hasPermi('asset:maintain:add')")
    @Log(title = "资产维修流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetProcessMaintain assetProcessMaintain)
    {
        return toAjax(assetProcessMaintainService.insertAssetProcessMaintain(assetProcessMaintain));
    }

    /**
     * 修改资产维修流程
     */
    @PreAuthorize("@ss.hasPermi('asset:maintain:edit')")
    @Log(title = "资产维修流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcessMaintain assetProcessMaintain)
    {
        return toAjax(assetProcessMaintainService.updateAssetProcessMaintain(assetProcessMaintain));
    }

    /**
     * 删除资产维修流程
     */
    @PreAuthorize("@ss.hasPermi('asset:maintain:remove')")
    @Log(title = "资产维修流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetProcessMaintainService.deleteAssetProcessMaintainByIds(ids));
    }
}
