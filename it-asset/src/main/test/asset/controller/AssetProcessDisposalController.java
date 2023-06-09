package com.hexing.assetNew.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.system.service.ISysDictDataService;
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
import com.hexing.assetNew.domain.AssetProcessDisposal;
import com.hexing.assetNew.service.IAssetProcessDisposalService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 资产处置流程Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/asset/disposal")
public class AssetProcessDisposalController extends BaseController
{
    @Autowired
    private IAssetProcessDisposalService assetProcessDisposalService;
    @Autowired
    private ISysDictDataService sysDictDataService;
    /**
     * 查询资产处置流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:disposal:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessDisposal assetProcessDisposal)
    {
        startPage();
        List<AssetProcessDisposal> list = assetProcessDisposalService.selectAssetProcessDisposalList(assetProcessDisposal);
        return getDataTable(list);
    }

    /**
     * 下载附件
     */
//    @PreAuthorize("@ss.hasPermi('asset:disposal:downLoadFile')")
    @PostMapping("/downLoadFile")
    public AjaxResult downLoadFile(AssetProcessDisposal assetProcessDisposal)
    {
        return toAjax(assetProcessDisposalService.downLoadFile(assetProcessDisposal));
    }

    /**
     * 导出资产处置流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:disposal:export')")
    @Log(title = "资产处置流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessDisposal assetProcessDisposal)
    {
        List<AssetProcessDisposal> list = assetProcessDisposalService.selectAssetProcessDisposalList(assetProcessDisposal);

        List<SysDictData> dictDataStatusList = sysDictDataService.selectDictDataByType("asset_process_status");
        List<SysDictData> dictDataTypeList = sysDictDataService.selectDictDataByType("ding_asset_process_type");
        Map<String,String> dictMap =new HashMap();
        for (SysDictData sysDictData :dictDataStatusList){
            dictMap.put(sysDictData.getDictValue(),sysDictData.getDictLabel());
        }
        for (SysDictData sysDictData :dictDataTypeList){
            dictMap.put(sysDictData.getDictValue(),sysDictData.getDictLabel());
        }
        for (AssetProcessDisposal disposal :list){
            if (dictMap.containsKey(disposal.getStatus())){
                disposal.setStatus(dictMap.get(disposal.getStatus()));
                disposal.setType(dictMap.get(disposal.getType()));
            }
        }
        ExcelUtil<AssetProcessDisposal> util = new ExcelUtil<AssetProcessDisposal>(AssetProcessDisposal.class);
        return util.exportExcel(list, "资产处置流程数据");
    }

    /**
     * 获取资产处置流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:disposal:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetProcessDisposalService.selectAssetProcessDisposalById(id));
    }

    /**
     * 新增资产处置流程
     */
    @PreAuthorize("@ss.hasPermi('asset:disposal:add')")
    @Log(title = "资产处置流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetProcessDisposal assetProcessDisposal)
    {
        return toAjax(assetProcessDisposalService.insertAssetProcessDisposal(assetProcessDisposal));
    }

    /**
     * 修改资产处置流程
     */
    @PreAuthorize("@ss.hasPermi('asset:disposal:edit')")
    @Log(title = "资产处置流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcessDisposal assetProcessDisposal)
    {
        return toAjax(assetProcessDisposalService.updateAssetProcessDisposal(assetProcessDisposal));
    }

    /**
     * 删除资产处置流程
     */
    @PreAuthorize("@ss.hasPermi('asset:disposal:remove')")
    @Log(title = "资产处置流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetProcessDisposalService.deleteAssetProcessDisposalByIds(ids));
    }
}
