package com.hexing.asset.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.domain.AssetProcessField;
import com.hexing.asset.domain.AssetProcessVariable;
import com.hexing.asset.domain.AssetsProcess;
import com.hexing.asset.enums.AssetProcessType;
import com.hexing.asset.service.IAssetProcessFieldService;
import com.hexing.asset.service.IAssetProcessVariableService;
import com.hexing.asset.service.IAssetsProcessService;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.ISysDictDataService;
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
 * 资产流程Controller
 *
 * @author zxy
 * @date 2022-11-04
 */
@RestController
@RequestMapping("/assets/process")
@Api(tags="流程管理")
public class AssetsProcessController extends BaseController
{
    @Autowired
    private IAssetsProcessService assetsProcessService;
    @Autowired
    private IAssetProcessFieldService fieldService;
    @Autowired
    private IAssetProcessVariableService variableService;
    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询资产流程列表
     */
    @PreAuthorize("@ss.hasPermi('assets:process:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetsProcess assetsProcess)
    {
        startPage();
        List<AssetsProcess> list = assetsProcessService.selectAssetsProcessList(assetsProcess);
        return getDataTable(list);
    }

    @GetMapping("/getOne")
    @ApiOperation("查询资产流程")
    public AjaxResult getOne(String taskCode, String assetCode) {
        String processType = AssetProcessType.ASSET_COUNTING.getCode();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("taskCode", taskCode);
        paramMap.put("assetCode", assetCode);
        return AjaxResult.success((AssetProcessCounting) assetsProcessService.getOne(processType, paramMap));
    }

    /**
     * 导出资产流程列表
     */
    @PreAuthorize("@ss.hasPermi('assets:process:export')")
    @Log(title = "资产流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetsProcess assetsProcess)
    {
        List<AssetsProcess> list = assetsProcessService.selectAssetsProcessList(assetsProcess);
        ExcelUtil<AssetsProcess> util = new ExcelUtil<AssetsProcess>(AssetsProcess.class);
        return util.exportExcel(list, "资产流程数据");
    }


    /**
     * 获取资产流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('assets:process:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetsProcessService.selectAssetsProcessById(id));
    }

    /**
     * 新增资产流程
     */
    //    @PreAuthorize("@ss.hasPermi('assets:process:add')")
    @Log(title = "资产流程", businessType = BusinessType.INSERT) //TODO 日志切面修改
    @PostMapping("/{processType}/save")
    public AjaxResult save(JSONObject params, @PathVariable String processType) {
        if (StringUtils.isEmpty(processType)) {
            return AjaxResult.error("流程类型为空");
        }

        List<AssetProcessField> fieldList = fieldService.list(new LambdaQueryWrapper<AssetProcessField>()
                .eq(AssetProcessField::getProcessType, processType));

        if (CollectionUtil.isEmpty(fieldList)) {
            return AjaxResult.error("流程类型不存在");
        }

        AssetsProcess process = new AssetsProcess();
        // 根据流程类型查询字典数据表，获取流程类型编号

        assetsProcessService.insertAssetsProcess(process);

        Map<String, Long> fieldMap = fieldList
                .stream().collect(Collectors.toMap(AssetProcessField::getFieldKey, AssetProcessField::getId));
        // 将字段值存库
        List<AssetProcessVariable> varList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Long fieldId = fieldMap.get(entry.getKey());
            AssetProcessVariable var = new AssetProcessVariable();
            var.setFieldId(String.valueOf(fieldId))
                    .setFieldValue(String.valueOf(entry.getValue()));
            varList.add(var);
        }

        return toAjax(variableService.saveBatch(varList));
    }

    /**
     * 新增资产流程
     */
//    @PreAuthorize("@ss.hasPermi('assets:process:add')")
//    @Log(title = "资产流程", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody AssetsProcess assetsProcess)
//    {
//        return toAjax(assetsProcessService.insertAssetsProcess(assetsProcess));
//    }

    /**
     * 修改资产流程
     */
    @PreAuthorize("@ss.hasPermi('assets:process:edit')")
    @Log(title = "资产流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetsProcess assetsProcess)
    {
        return toAjax(assetsProcessService.updateAssetsProcess(assetsProcess));
    }

    /**
     * 删除资产流程
     */
    @PreAuthorize("@ss.hasPermi('assets:process:remove')")
    @Log(title = "资产流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetsProcessService.deleteAssetsProcessByIds(ids));
    }
}
