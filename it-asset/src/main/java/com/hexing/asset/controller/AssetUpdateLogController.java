package com.hexing.asset.controller;

import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.common.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资产信息更新日志Controller
 *
 * @author zxy
 * @date 2022-10-19
 */
@RestController
@RequestMapping("/mature/log")
public class AssetUpdateLogController extends BaseController
{
    @Autowired
    private IAssetUpdateLogService assetUpdateLogService;

}
