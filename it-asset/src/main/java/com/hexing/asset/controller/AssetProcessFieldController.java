package com.hexing.asset.controller;

import java.util.List;

import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcessField;
import com.hexing.asset.service.IAssetProcessFieldService;
import com.hexing.common.utils.bean.BeanTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 流程字段 Controller
 *
 * @author zxy
 * @date 2022-11-01
 */
@RestController
@RequestMapping("/asset/process/field")
@Api(tags = "流程字段管理")
public class AssetProcessFieldController extends BaseController {

    @Autowired
    private IAssetProcessFieldService assetProcessFieldService;


    @PreAuthorize("@ss.hasPermi('process:field:list')")
    @GetMapping("/list")
    @ApiOperation("查询流程字段列表")
    public TableDataInfo list(AssetProcessField assetProcessField) {
        startPage();
        List<AssetProcessField> list = assetProcessFieldService.selectAssetProcessFieldList(assetProcessField);
        return getDataTable(list);
    }

//    /**
//     * 导出【请填写功能名称】列表
//     */
//    @PreAuthorize("@ss.hasPermi('mature:field:export')")
//    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(AssetProcessField assetProcessField) {
//        List<AssetProcessField> list = assetProcessFieldService.selectAssetProcessFieldList(assetProcessField);
//        ExcelUtil<AssetProcessField> util = new ExcelUtil<AssetProcessField>(AssetProcessField.class);
//        return util.exportExcel(list, "【请填写功能名称】数据");
//    }

    @PreAuthorize("@ss.hasPermi('process:field:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取流程字段详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(assetProcessFieldService.selectAssetProcessFieldById(id));
    }

    @PreAuthorize("@ss.hasPermi('process:field:add')")
    @Log(title = "新增流程字段", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增流程字段")
    public AjaxResult add(@RequestBody AssetProcessField assetProcessField) {
        return toAjax(assetProcessFieldService.insertAssetProcessField(assetProcessField));
    }

    @PreAuthorize("@ss.hasPermi('process:field:edit')")
    @Log(title = "修改流程字段", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改流程字段")
    public AjaxResult edit(@RequestBody AssetProcessField assetProcessField) {
        return toAjax(assetProcessFieldService.updateAssetProcessField(assetProcessField));
    }

//    /**
//     * 禁用流程字段
//     */
//    @PreAuthorize("@ss.hasPermi('mature:field:remove')")
//    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids) {
//        return toAjax(assetProcessFieldService.deleteAssetProcessFieldByIds(ids));
//    }

}
