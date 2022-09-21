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
import com.hexing.asset.domain.AssetProcessTransfer;
import com.hexing.asset.service.IAssetProcessTransferService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 资产转移流程Controller
 *
 * @author zxy
 * @date 2022-09-20
 */
@RestController
@RequestMapping("/asset/transfer")
public class AssetProcessTransferController extends BaseController
{
    @Autowired
    private IAssetProcessTransferService assetProcessTransferService;

    /**
     * 查询资产转移流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:transfer:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessTransfer assetProcessTransfer)
    {
        startPage();
        List<AssetProcessTransfer> list = assetProcessTransferService.selectAssetProcessTransferList(assetProcessTransfer);
        return getDataTable(list);
    }

    /**
     * 导出资产转移流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:transfer:export')")
    @Log(title = "资产转移流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessTransfer assetProcessTransfer)
    {
        List<AssetProcessTransfer> list = assetProcessTransferService.selectAssetProcessTransferList(assetProcessTransfer);
        ExcelUtil<AssetProcessTransfer> util = new ExcelUtil<AssetProcessTransfer>(AssetProcessTransfer.class);
        return util.exportExcel(list, "资产转移流程数据");
    }

    /**
     * 获取资产转移流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:transfer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetProcessTransferService.selectAssetProcessTransferById(id));
    }

    /**
     * 新增资产转移流程
     */
    @PreAuthorize("@ss.hasPermi('asset:transfer:add')")
    @Log(title = "资产转移流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetProcessTransfer assetProcessTransfer)
    {
        return toAjax(assetProcessTransferService.insertAssetProcessTransfer(assetProcessTransfer));
    }

    /**
     * 修改资产转移流程
     */
    @PreAuthorize("@ss.hasPermi('asset:transfer:edit')")
    @Log(title = "资产转移流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcessTransfer assetProcessTransfer)
    {
        return toAjax(assetProcessTransferService.updateAssetProcessTransfer(assetProcessTransfer));
    }

    /**
     * 删除资产转移流程
     */
    @PreAuthorize("@ss.hasPermi('asset:transfer:remove')")
    @Log(title = "资产转移流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetProcessTransferService.deleteAssetProcessTransferByIds(ids));
    }
}
