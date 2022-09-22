package com.hexing.quartz.task;

import com.hexing.asset.service.IAssetInventoryTaskService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("InventoryTaskJob")
public class InventoryTaskJob {

    @Resource
    private IAssetInventoryTaskService assetInventoryTaskService;

    private void updateInventoryTaskStatus() {
        assetInventoryTaskService.updateInventoryTaskStatus();
    }

}
