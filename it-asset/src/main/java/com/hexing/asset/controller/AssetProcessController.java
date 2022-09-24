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
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 流程总Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/asset/process")
public class AssetProcessController extends BaseController
{
    @Autowired
    private IAssetProcessService assetProcessService;

    /**
     * 查询流程总列表
     */
    @PreAuthorize("@ss.hasPermi('asset:process:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcess assetProcess)
    {
        startPage();
        List<AssetProcess> list = assetProcessService.selectAssetProcessList(assetProcess);
        return getDataTable(list);
    }

    /**
     * 查询调拨流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:process:transferList')")
    @GetMapping("/transferList")
    public TableDataInfo transferList(AssetProcess assetProcess)
    {
        startPage();
        List<AssetProcess> list = assetProcessService.selectAssetProcessList(assetProcess);
        return getDataTable(list);
    }

    /**
     * 导出流程总列表
     */
    @PreAuthorize("@ss.hasPermi('asset:process:export')")
    @Log(title = "流程总", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcess assetProcess)
    {
        List<AssetProcess> list = assetProcessService.selectAssetProcessList(assetProcess);
        ExcelUtil<AssetProcess> util = new ExcelUtil<AssetProcess>(AssetProcess.class);
        return util.exportExcel(list, "流程总数据");
    }

    /**
     * 获取流程总详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:process:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetProcessService.selectAssetProcessById(id));
    }

    /**
     * 新增流程总
     */
    @PreAuthorize("@ss.hasPermi('asset:process:add')")
    @Log(title = "流程总", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetProcess assetProcess)
    {
        return toAjax(assetProcessService.insertAssetProcess(assetProcess));
    }

    /**
     * 修改流程总
     */
    @PreAuthorize("@ss.hasPermi('asset:process:edit')")
    @Log(title = "流程总", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcess assetProcess)
    {
        return toAjax(assetProcessService.updateAssetProcess(assetProcess));
    }

    /**
     * 删除流程总
     */
    @PreAuthorize("@ss.hasPermi('asset:process:remove')")
    @Log(title = "流程总", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetProcessService.deleteAssetProcessByIds(ids));
    }
}
