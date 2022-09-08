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
import com.hexing.asset.domain.AssetProcessExchange;
import com.hexing.asset.service.IAssetProcessExchangeService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 资产更换流程Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/asset/exchange")
public class AssetProcessExchangeController extends BaseController
{
    @Autowired
    private IAssetProcessExchangeService assetProcessExchangeService;

    /**
     * 查询资产更换流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:exchange:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessExchange assetProcessExchange)
    {
        startPage();
        List<AssetProcessExchange> list = assetProcessExchangeService.selectAssetProcessExchangeList(assetProcessExchange);
        return getDataTable(list);
    }

    /**
     * 导出资产更换流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:exchange:export')")
    @Log(title = "资产更换流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessExchange assetProcessExchange)
    {
        List<AssetProcessExchange> list = assetProcessExchangeService.selectAssetProcessExchangeList(assetProcessExchange);
        ExcelUtil<AssetProcessExchange> util = new ExcelUtil<AssetProcessExchange>(AssetProcessExchange.class);
        return util.exportExcel(list, "资产更换流程数据");
    }

    /**
     * 获取资产更换流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:exchange:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetProcessExchangeService.selectAssetProcessExchangeById(id));
    }

    /**
     * 新增资产更换流程
     */
    @PreAuthorize("@ss.hasPermi('asset:exchange:add')")
    @Log(title = "资产更换流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetProcessExchange assetProcessExchange)
    {
        return toAjax(assetProcessExchangeService.insertAssetProcessExchange(assetProcessExchange));
    }

    /**
     * 修改资产更换流程
     */
    @PreAuthorize("@ss.hasPermi('asset:exchange:edit')")
    @Log(title = "资产更换流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcessExchange assetProcessExchange)
    {
        return toAjax(assetProcessExchangeService.updateAssetProcessExchange(assetProcessExchange));
    }

    /**
     * 删除资产更换流程
     */
    @PreAuthorize("@ss.hasPermi('asset:exchange:remove')")
    @Log(title = "资产更换流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetProcessExchangeService.deleteAssetProcessExchangeByIds(ids));
    }
}
