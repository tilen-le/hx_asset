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
import com.hexing.assetNew.domain.AssetProcessTransform;
import com.hexing.assetNew.service.IAssetProcessTransformService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 资产改造流程Controller
 *
 * @author zxy
 * @date 2022-09-20
 */
@RestController
@RequestMapping("/asset/transform")
public class AssetProcessTransformController extends BaseController
{
    @Autowired
    private IAssetProcessTransformService assetProcessTransformService;
    @Autowired
    private ISysDictDataService sysDictDataService;
    /**
     * 查询资产改造流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:transform:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessTransform assetProcessTransform)
    {
        startPage();
        List<AssetProcessTransform> list = assetProcessTransformService.selectAssetProcessTransformList(assetProcessTransform);
        return getDataTable(list);
    }

    /**
     * 导出资产改造流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:transform:export')")
    @Log(title = "资产改造流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessTransform assetProcessTransform)
    {
        List<AssetProcessTransform> list = assetProcessTransformService.selectAssetProcessTransformList(assetProcessTransform);
        List<SysDictData> dictDataStatusList = sysDictDataService.selectDictDataByType("asset_process_status");
        Map<String,String> dictMap =new HashMap();
        for (SysDictData sysDictData :dictDataStatusList){
            dictMap.put(sysDictData.getDictValue(),sysDictData.getDictLabel());
        }
        for (AssetProcessTransform transform :list){
            if (dictMap.containsKey(transform.getStatus())){
                transform.setStatus(dictMap.get(transform.getStatus()));
            }
        }
        ExcelUtil<AssetProcessTransform> util = new ExcelUtil<AssetProcessTransform>(AssetProcessTransform.class);
        return util.exportExcel(list, "资产改造流程数据");
    }

    /**
     * 获取资产改造流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:transform:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetProcessTransformService.selectAssetProcessTransformById(id));
    }

    /**
     * 新增资产改造流程
     */
    @PreAuthorize("@ss.hasPermi('asset:transform:add')")
    @Log(title = "资产改造流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetProcessTransform assetProcessTransform)
    {
        return toAjax(assetProcessTransformService.insertAssetProcessTransform(assetProcessTransform));
    }

    /**
     * 修改资产改造流程
     */
    @PreAuthorize("@ss.hasPermi('asset:transform:edit')")
    @Log(title = "资产改造流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcessTransform assetProcessTransform)
    {
        return toAjax(assetProcessTransformService.updateAssetProcessTransform(assetProcessTransform));
    }

    /**
     * 删除资产改造流程
     */
    @PreAuthorize("@ss.hasPermi('asset:transform:remove')")
    @Log(title = "资产改造流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetProcessTransformService.deleteAssetProcessTransformByIds(ids));
    }
}
