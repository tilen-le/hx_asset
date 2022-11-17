package com.hexing.assetnew.controller;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.dto.StatisQueryParam;
import com.hexing.asset.domain.vo.AssetLifeCycleVO;
import com.hexing.asset.domain.vo.SimpleStatisticVO;
import com.hexing.asset.enums.AssetStatisticType;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.model.LoginUser;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.ServletUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.framework.web.service.TokenService;
import com.hexing.system.service.ISysDeptService;
import com.hexing.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hexing.common.annotation.Log;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.enums.BusinessType;
import com.hexing.assetnew.domain.Asset;
import com.hexing.assetnew.service.IAssetService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资产表Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@Api(tags = "资产信息管理")
@RestController
@RequestMapping("/asset")
public class AssetController extends BaseController {

    @Autowired
    private IAssetService assetService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IAssetProcessService assetProcessService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询资产列表
     */
    @ApiOperation("查询资产列表")
    @PreAuthorize("@ss.hasPermi('asset:collection:list')")
    @GetMapping("/list")
    public TableDataInfo list(Asset asset) {
        startPage();
        List<Asset> list = assetService.selectAssetList(asset);
        return getDataTable(list);
    }

    /**
     * 导出资产表列表
     */
    @ApiOperation("导出资产表列表")
    @PreAuthorize("@ss.hasPermi('asset:asset:export')")
    @Log(title = "导出资产表列表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Asset asset) {
        List<Asset> list = assetService.selectAssetList(asset);
//        List<Asset> list = assetService.list();
        ExcelUtil<Asset> util = new ExcelUtil<>(Asset.class);
        return util.exportExcel(list, "固定资产数据");
    }

    /**
     * 获取资产详细信息
     */
    @ApiOperation("获取资产详细信息")
    @PreAuthorize("@ss.hasPermi('asset:asset:query')")
    @GetMapping(value = "/getInfo/{assetCode}")
    public AjaxResult getInfo(@PathVariable String assetCode) {
        Asset asset = assetService.selectAssetByAssetCode(assetCode);
        if (ObjectUtil.isEmpty(asset)) {
            return AjaxResult.error("资产不存在");
        }
        return AjaxResult.success(asset);
    }

    /**
     * 新增资产表
     */
    @ApiOperation("新增资产表")
    @PreAuthorize("@ss.hasPermi('asset:asset:add')")
    @Log(title = "新增资产表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Asset asset) {
        return toAjax(assetService.insertAsset(asset));
    }

    /**
     * 修改资产表
     */
    @ApiOperation("修改资产表")
    @PreAuthorize("@ss.hasPermi('asset:asset:edit')")
    @Log(title = "修改资产表", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody Asset asset) {
        return toAjax(assetService.updateAsset(asset, null));
    }

    /**
     * 删除资产表
     *
     * @editor 80015306
     */
    @ApiOperation("删除资产表")
    @PreAuthorize("@ss.hasPermi('asset:asset:remove')")
    @Log(title = "删除资产表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{assetCodes}")
    public AjaxResult remove(@PathVariable List<String> assetCodes) {
        return toAjax(assetService.deleteAssetByAssetCodes(assetCodes));
    }

    /**
     * 资产信息导入
     */
    @ApiOperation("资产信息导入")
    @PreAuthorize("@ss.hasPermi('asset:asset:import')")
    @Log(title = "资产信息导入", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Asset> util = new ExcelUtil<>(Asset.class);
        List<Asset> assetList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = assetService.importAsset(assetList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    /**
     * 资产导入模板下载
     */
    @ApiOperation("资产导入模板下载")
    @PreAuthorize("@ss.hasPermi('asset:asset:template')")
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<Asset> util = new ExcelUtil<>(Asset.class);
        return util.importTemplateExcel("资产信息导入模板");
    }

}
