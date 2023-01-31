package com.hexing.assetNew.controller;

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
import com.hexing.assetNew.domain.AssetProcessReceive;
import com.hexing.assetNew.service.IAssetProcessReceiveService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 资产领用流程Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/asset/receive")
public class AssetProcessReceiveController extends BaseController
{
    @Autowired
    private IAssetProcessReceiveService assetProcessReceiveService;

    /**
     * 查询资产领用流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:receive:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessReceive assetProcessReceive)
    {
        startPage();
        List<AssetProcessReceive> list = assetProcessReceiveService.selectAssetProcessReceiveList(assetProcessReceive);
        return getDataTable(list);
    }

    /**
     * 导出资产领用流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:receive:export')")
    @Log(title = "资产领用流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessReceive assetProcessReceive)
    {
        List<AssetProcessReceive> list = assetProcessReceiveService.selectAssetProcessReceiveList(assetProcessReceive);
        ExcelUtil<AssetProcessReceive> util = new ExcelUtil<AssetProcessReceive>(AssetProcessReceive.class);
        return util.exportExcel(list, "资产领用流程数据");
    }

    /**
     * 获取资产领用流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:receive:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetProcessReceiveService.selectAssetProcessReceiveById(id));
    }

    /**
     * 新增资产领用流程
     */
    @PreAuthorize("@ss.hasPermi('asset:receive:add')")
    @Log(title = "资产领用流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetProcessReceive assetProcessReceive)
    {
        return toAjax(assetProcessReceiveService.insertAssetProcessReceive(assetProcessReceive));
    }

    /**
     * 修改资产领用流程
     */
    @PreAuthorize("@ss.hasPermi('asset:receive:edit')")
    @Log(title = "资产领用流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcessReceive assetProcessReceive)
    {
        return toAjax(assetProcessReceiveService.updateAssetProcessReceive(assetProcessReceive));
    }

    /**
     * 删除资产领用流程
     */
    @PreAuthorize("@ss.hasPermi('asset:receive:remove')")
    @Log(title = "资产领用流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetProcessReceiveService.deleteAssetProcessReceiveByIds(ids));
    }
}
