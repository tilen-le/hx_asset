package com.hexing.quartz.task;

import com.hexing.assetnew.service.IAssetInventoryTaskService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("InventoryTaskJob")
public class InventoryTaskJob {

    @Resource
    private IAssetInventoryTaskService assetInventoryTaskService;

    public void updateInventoryTaskStatus() {
        assetInventoryTaskService.updateInventoryTaskStatus();
    }

}
