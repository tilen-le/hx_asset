package com.hexing.assetnew.controller;

import com.hexing.assetnew.service.IAssetsProcessService;
import com.hexing.common.core.controller.BaseController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资产流程Controller
 *
 * @author zxy
 * @date 2022-11-04
 */
@RestController
@RequestMapping("/assets/process")
@Api(tags = "流程管理")
public class AssetsProcessController extends BaseController {
    @Autowired
    private IAssetsProcessService assetsProcessService;

}
