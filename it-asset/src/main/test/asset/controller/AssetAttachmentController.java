package com.hexing.assetNew.controller;

import java.util.List;

import com.hexing.assetNew.domain.AssetAttachment;
import com.hexing.assetNew.service.IAssetAttachmentService;
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
 * 资产附件Controller
 *
 * @author zxy
 * @date 2022-11-01
 */
@RestController
@RequestMapping("/asset/attachment")
public class AssetAttachmentController extends BaseController
{
    @Autowired
    private IAssetAttachmentService assetAttachmentService;

    /**
     * 查询资产附件列表
     */
    @PreAuthorize("@ss.hasPermi('asset:attachment:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetAttachment assetAttachment)
    {

        startPage();
        List<AssetAttachment> list = assetAttachmentService.selectAssetAttachmentList(assetAttachment);
        return getDataTable(list);
    }

    /**
     * 导出资产附件列表
     */
    @PreAuthorize("@ss.hasPermi('asset:attachment:export')")
    @Log(title = "资产附件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetAttachment assetAttachment)
    {
        List<AssetAttachment> list = assetAttachmentService.selectAssetAttachmentList(assetAttachment);
        ExcelUtil<AssetAttachment> util = new ExcelUtil<AssetAttachment>(AssetAttachment.class);
        return util.exportExcel(list, "资产附件数据");
    }

    /**
     * 获取资产附件详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:attachment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetAttachmentService.selectAssetAttachmentById(id));
    }

    /**
     * 新增资产附件
     */
    @PreAuthorize("@ss.hasPermi('asset:attachment:add')")
    @Log(title = "资产附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetAttachment assetAttachment)
    {
        return toAjax(assetAttachmentService.insertAssetAttachment(assetAttachment));
    }

    /**
     * 修改资产附件
     */
    @PreAuthorize("@ss.hasPermi('asset:attachment:edit')")
    @Log(title = "资产附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetAttachment assetAttachment)
    {
        return toAjax(assetAttachmentService.updateAssetAttachment(assetAttachment));
    }

    /**
     * 删除资产附件
     */
    @PreAuthorize("@ss.hasPermi('asset:attachment:remove')")
    @Log(title = "资产附件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetAttachmentService.deleteAssetAttachmentByIds(ids));
    }
}
