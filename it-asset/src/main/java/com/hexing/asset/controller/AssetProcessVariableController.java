package com.hexing.asset.controller;

import com.hexing.asset.domain.AssetProcessVariable;
import com.hexing.asset.service.IAssetProcessVariableService;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 流程值Controller
 *
 * @author zxy
 * @date 2022-11-03
 */
@RestController
@RequestMapping("/asset/variable")
public class AssetProcessVariableController extends BaseController
{
    @Autowired
    private IAssetProcessVariableService assetProcessVariableService;

    /**
     * 查询流程值列表
     */
    @PreAuthorize("@ss.hasPermi('mature:variable:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcessVariable assetProcessVariable)
    {
        startPage();
        List<AssetProcessVariable> list = assetProcessVariableService.selectAssetProcessVariableList(assetProcessVariable);
        return getDataTable(list);
    }



}
