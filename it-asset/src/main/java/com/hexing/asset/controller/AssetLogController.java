package com.hexing.asset.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetUpdateLog;
import com.hexing.asset.domain.vo.AssetProcessParam;
import com.hexing.asset.domain.vo.AssetProcessReturn;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.core.page.PageDomain;
import com.hexing.common.core.page.TableDataInfo;
import com.hexing.common.core.page.TableSupport;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.ISysDeptService;
import com.hexing.system.service.impl.SysUserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 资产日志Controller
 *
 * @author zxy
 * @date 2023-01-30
 */
@Api(tags = "资产日志")
@RestController
@RequestMapping("/asset/log")
public class AssetLogController extends BaseController
{
    @Autowired
    private IAssetUpdateLogService updateLogService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private IAssetProcessService processService;

    /**
     * 查询保管记录
     */
    @GetMapping("/custodyLogList")
    @PreAuthorize("@ss.hasPermi('asset:log:custodyLogList')")
    @ApiOperation("查询保管记录")
    @ApiOperationSupport(order = 14)
    public TableDataInfo custodyLogList(AssetProcessParam assetProcess)
    {
        List<AssetUpdateLog> list = updateLogService.custodyLogList(assetProcess);
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        List<AssetUpdateLog> collect =new ArrayList<>();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            collect = list.stream().skip((pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        }
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setRows(collect);
        return dataTable;
    }

    /**
     * 查询工单记录
     */
    @GetMapping("/workLogList")
    @PreAuthorize("@ss.hasPermi('asset:log:workLogList')")
    @ApiOperation("查询工单记录")
    @ApiOperationSupport(order = 14)
    public TableDataInfo workLogList(AssetProcessParam assetProcess)
    {
        List<AssetProcessReturn> list = updateLogService.workLogList(assetProcess);
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        List<AssetProcessReturn> collect =new ArrayList<>();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            collect = list.stream().skip((pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        }
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setRows(collect);
        return dataTable;
    }

    /**
     * 查询操作记录
     */
    @GetMapping("/operationLogList")
    @PreAuthorize("@ss.hasPermi('asset:log:operationLogList')")
    @ApiOperation("查询操作记录")
    @ApiOperationSupport(order = 14)
    public TableDataInfo operationLogList(AssetProcessParam assetProcess)
    {
        List<AssetUpdateLog> list = updateLogService.operationLogList(assetProcess);
        return getDataTable(list);
    }

    /**
     * 获取资产操作记录详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取资产操作记录详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        AssetUpdateLog updateLog = updateLogService.getOperationLogById(id);
        if (ObjectUtil.isEmpty(updateLog)) {
            return AjaxResult.error("该资产操作记录不存在");
        }
        return AjaxResult.success(updateLog);
    }

}
