package com.hexing.asset.controller;

import cn.hutool.core.util.ObjectUtil;
import com.hexing.asset.domain.AssetCustodyLog;
import com.hexing.asset.service.IAssetCustodyLogService;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private IAssetCustodyLogService custodyLogService;

    /**
     * 资产保管记录列表
     */
    @GetMapping("/custodyLogList")
    @PreAuthorize("@ss.hasPermi('manage:config:custodyLogList')")
    @ApiOperation("资产保管记录列表")
    public TableDataInfo custodyLogList(AssetCustodyLog searchDTO)
    {
        startPage();
        List<AssetCustodyLog> list = custodyLogService.selectAssetCustodyLogList(searchDTO);

        return getDataTable(list);
    }

    /**
     * 资产保管记录详情
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("资产保管记录详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        AssetCustodyLog custodyLog = custodyLogService.selectAssetCustodyLogById(id);
        if (ObjectUtil.isEmpty(custodyLog)) {
            return AjaxResult.error("资产保管记录不存在");
        }
        return AjaxResult.success(custodyLog);
    }

//    /**
//     * 资产工单记录列表
//     */
//    @GetMapping("/workLogList")
//    @PreAuthorize("@ss.hasPermi('manage:config:custodyLogList')")
//    @ApiOperation("资产工单记录列表")
//    public TableDataInfo workLogList(AssetWorkLog searchDTO)
//    {
//        startPage();
//        List<AssetWorkLog> list = workLogService.selectAssetWorkLogList(searchDTO);
//
//        return getDataTable(list);
//    }
//
//    /**
//     * 资产操作记录列表
//     */
//    @GetMapping("/operationLogList")
//    @PreAuthorize("@ss.hasPermi('manage:config:operationLogList')")
//    @ApiOperation("资产操作记录列表")
//    public TableDataInfo operationLogList(AssetOperationLog searchDTO)
//    {
//        startPage();
//        List<AssetOperationLog> list = operationLogService.selectAssetOperationLogList(searchDTO);
//
//        return getDataTable(list);
//    }

}
