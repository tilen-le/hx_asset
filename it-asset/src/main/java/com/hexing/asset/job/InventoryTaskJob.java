package com.hexing.asset.job;

import com.hexing.asset.service.IAssetInventoryTaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InventoryTaskJob {

    @Resource
    private IAssetInventoryTaskService assetInventoryTaskService;

    @Scheduled(cron = "0 59 23 * * ? ")
    private void updateInventoryTaskStatus() {
        assetInventoryTaskService.updateInventoryTaskStatus();
    }

}
