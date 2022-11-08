package com.hexing.asset.controller;

import com.hexing.asset.domain.AssetProcessVariable;
import com.hexing.asset.service.IAssetProcessVariableService;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 流程值Controller
 *
 * @author zxy
 * @date 2022-11-03
 */
@RestController
@RequestMapping("/asset/variable")
public class AssetProcessVariableController extends BaseController
{
    @Autowired
    private IAssetProcessVariableService assetProcessVariableService;

    /**
     * 查询流程值列表
     */
    @PreAuthorize("@ss.hasPermi('mature:variable:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessVariable assetProcessVariable)
    {
        startPage();
        List<AssetProcessVariable> list = assetProcessVariableService.selectAssetProcessVariableList(assetProcessVariable);
        return getDataTable(list);
    }

//    /**
//     * 导出流程值列表
//     */
//    @PreAuthorize("@ss.hasPermi('mature:variable:export')")
//    @Log(title = "流程值", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(AssetProcessVariable assetProcessVariable)
//    {
//        List<AssetProcessVariable> list = assetProcessVariableService.selectAssetProcessVariableList(assetProcessVariable);
//        ExcelUtil<AssetProcessVariable> util = new ExcelUtil<AssetProcessVariable>(AssetProcessVariable.class);
//        return util.exportExcel(list, "流程值数据");
//    }
//
//    /**
//     * 获取流程值详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('mature:variable:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return AjaxResult.success(assetProcessVariableService.selectAssetProcessVariableById(id));
//    }
//
//    /**
//     * 新增流程值
//     */
//    @PreAuthorize("@ss.hasPermi('mature:variable:add')")
//    @Log(title = "流程值", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody AssetProcessVariable assetProcessVariable)
//    {
//        return toAjax(assetProcessVariableService.insertAssetProcessVariable(assetProcessVariable));
//    }
//
//    /**
//     * 修改流程值
//     */
//    @PreAuthorize("@ss.hasPermi('mature:variable:edit')")
//    @Log(title = "流程值", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody AssetProcessVariable assetProcessVariable)
//    {
//        return toAjax(assetProcessVariableService.updateAssetProcessVariable(assetProcessVariable));
//    }
//
//    /**
//     * 删除流程值
//     */
//    @PreAuthorize("@ss.hasPermi('mature:variable:remove')")
//    @Log(title = "流程值", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(assetProcessVariableService.deleteAssetProcessVariableByIds(ids));
//    }
}
