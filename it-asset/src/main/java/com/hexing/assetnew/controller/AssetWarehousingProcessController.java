package com.hexing.assetnew.controller;

import com.hexing.assetnew.domain.AssetWarehousingProcessDomain;
import com.hexing.assetnew.service.IAssetWarehousingProcessService;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "资产入库流程管理")
@RestController
@RequestMapping("/asset/warehousing")
public class AssetWarehousingProcessController extends BaseController {

    @Autowired
    private IAssetWarehousingProcessService warehousingProcessService;

    /**
     * TODO SAP推送入库信息
     *
     * @param sapRequestBody SAP请求体
     * @return 响应体
     */
    @ApiOperation("SAP推送入库信息")
    @PreAuthorize("@ss.hasPermi('asset:warehousing:sync')")
    @PostMapping("/sapSyncWarehousingOrderInfo")
    public String sapSyncWarehousingOrderInfo(String sapRequestBody) {
        // 1.入库流程记录新增
        AssetWarehousingProcessDomain domain = warehousingProcessService.prepareWarehousingProcessDomain(sapRequestBody);
        warehousingProcessService.add(domain);

        // 2.生成资产条目
        warehousingProcessService.generateAsset(domain);

        // 3.响应SAP同步结果
        return null;
    }


    /**
     * TODO 验收单附件上传
     *
     * @return
     */
    @ApiOperation("验收单附件上传")
    @PreAuthorize("@ss.hasPermi('asset:warehousing:upload')")
    @PostMapping("/uploadAttachment")
    public String uploadAttachment() {
        return null;
    }


    /**
     * TODO 导出入库流程信息
     *
     * @return
     */
    @ApiOperation("导出入库流程信息")
    @PreAuthorize("@ss.hasPermi('asset:warehousing:export')")
    @GetMapping("/export")
    public AjaxResult export() {
        return null;
    }

}
