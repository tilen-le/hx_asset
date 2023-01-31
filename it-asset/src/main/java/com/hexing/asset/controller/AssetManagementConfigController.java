package com.hexing.asset.controller;

import cn.hutool.core.util.ObjectUtil;
import com.hexing.asset.domain.AssetManagementConfig;
import com.hexing.asset.service.IAssetManagementConfigService;
import com.hexing.common.annotation.Log;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.page.TableDataInfo;
import com.hexing.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产管理配置Controller
 *
 * @author zxy
 * @date 2023-01-30
 */
@Api(tags = "资产管理配置")
@RestController
@RequestMapping("/manage/config")
public class AssetManagementConfigController extends BaseController
{
    @Autowired
    private IAssetManagementConfigService assetManagementConfigService;

    /**
     * 查询资产管理配置列表
     */
    @GetMapping("/list")
    @ApiOperation("查询资产管理配置列表")
    public TableDataInfo list(AssetManagementConfig assetManagementConfig)
    {
        startPage();
        List<AssetManagementConfig> list = assetManagementConfigService.selectAssetManagementConfigList(assetManagementConfig);

        return getDataTable(list);
    }

    /**
     * 获取资产管理配置详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取资产管理配置详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        AssetManagementConfig assetManagementConfig = assetManagementConfigService.selectAssetManagementConfigById(id);
        if (ObjectUtil.isEmpty(assetManagementConfig)) {
            return AjaxResult.error("资产管理配置不存在");
        }
        return AjaxResult.success(assetManagementConfig);
    }

    /**
     * 新增资产管理配置
     */
    @Log(title = "资产管理配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ApiOperation("新增资产管理配置")
    public AjaxResult add(@RequestBody AssetManagementConfig assetManagementConfig)
    {
        return toAjax(assetManagementConfigService.insertAssetManagementConfig(assetManagementConfig));
    }

    /**
     * 修改资产管理配置
     */
    @Log(title = "资产管理配置", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    @ApiOperation("修改资产管理配置")
    public AjaxResult edit(@RequestBody AssetManagementConfig assetManagementConfig)
    {
        return toAjax(assetManagementConfigService.updateAssetManagementConfig(assetManagementConfig));
    }

    /**
     * 删除资产管理配置
     */
    @Log(title = "资产管理配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除资产管理配置")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetManagementConfigService.deleteAssetManagementConfigByIds(ids));
    }
}
