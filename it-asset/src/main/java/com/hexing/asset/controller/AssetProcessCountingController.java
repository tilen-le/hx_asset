package com.hexing.asset.controller;

import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hexing.asset.domain.vo.AssetProcessCountingVO;
import com.hexing.asset.mapper.AssetProcessCountingMapper;
import com.hexing.common.core.page.PageDomain;
import com.hexing.common.core.page.TableSupport;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.poi.ExcelUtil;
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
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.service.IAssetProcessCountingService;
import com.hexing.common.core.page.TableDataInfo;


/**
 * 资产盘点流程Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@Api(tags = "资产盘点记录管理")
@RestController
@RequestMapping("/asset/counting")
public class AssetProcessCountingController extends BaseController
{
    @Autowired
    private IAssetProcessCountingService assetProcessCountingService;
    @Autowired
    private AssetProcessCountingMapper assetProcessCountingMapper;

    /**
     * 查询资产盘点流程列表
     */
    @ApiOperation("获取盘点记录列表")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessCounting assetProcessCounting)
    {
//        startPage();
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<Object> page = PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize());
        List<AssetProcessCounting> list = assetProcessCountingService
                .selectAssetProcessCountingList(assetProcessCounting);
        List<AssetProcessCountingVO> voList = assetProcessCountingService.toAssetProcessCountingVOList(list);
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setRows(voList);
        dataTable.setTotal(page.getTotal());
        return dataTable;
    }

    /**
     * 盘点统计
     */
    @ApiOperation("盘点统计")
    @GetMapping("/inventoryCount")
    public AjaxResult inventoryCount(String type,String startDate, String endDate) {
        if (StringUtils.isEmpty(startDate)||StringUtils.isEmpty(endDate)){
            return AjaxResult.error("起止日期未设置");
        }
        JSONObject jsonObject = assetProcessCountingService.inventoryCount(type, startDate, endDate);
        return AjaxResult.success(jsonObject);
    }

    /**
     * 盘点统计列表
     */
    @ApiOperation("盘点统计列表")
    @GetMapping("/inventoryCountList")
    public TableDataInfo inventoryCountList(String startDate,String endDate)
    {
        startPage();
        if (StringUtils.isEmpty(startDate)||StringUtils.isEmpty(endDate)){
            return new TableDataInfo();
        }
        List<AssetProcessCounting> list = assetProcessCountingService.inventoryCountList(startDate, endDate);
        TableDataInfo dataTable = getDataTable(list);
        return dataTable;
    }

    /**
     * 盘点状态统计
     */
    @ApiOperation("盘点状态统计")
    @GetMapping("/countingStatusCount")
    public AjaxResult countingStatusCount(String taskCode) {
        JSONObject result = assetProcessCountingService.countingStatusCount(taskCode);
        return AjaxResult.success(result);
    }

    /**
     * 导出资产盘点流程列表
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:export')")
    @Log(title = "资产盘点流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcessCounting assetProcessCounting)
    {
        List<AssetProcessCounting> list = assetProcessCountingService.selectAssetProcessCountingList(assetProcessCounting);
        List<AssetProcessCountingVO> voList = assetProcessCountingService.toAssetProcessCountingVOList(list);
        ExcelUtil<AssetProcessCountingVO> util = new ExcelUtil<>(AssetProcessCountingVO.class);
        return util.exportExcel(voList, "资产盘点记录");
    }

    /**
     * 获取资产盘点流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(assetProcessCountingService.selectAssetProcessCountingById(id));
    }

    /**
     * 新增资产盘点流程
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:add')")
    @Log(title = "资产盘点流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetProcessCounting assetProcessCounting)
    {
        return toAjax(assetProcessCountingService.insertAssetProcessCounting(assetProcessCounting));
    }

    /**
     * 修改资产盘点流程
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:edit')")
    @Log(title = "资产盘点流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcessCounting assetProcessCounting)
    {
        return toAjax(assetProcessCountingService.updateAssetProcessCounting(assetProcessCounting));
    }

    /**
     * 删除资产盘点流程
     */
    @PreAuthorize("@ss.hasPermi('asset:counting:remove')")
    @Log(title = "资产盘点流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetProcessCountingService.deleteAssetProcessCountingByIds(ids));
    }
}
