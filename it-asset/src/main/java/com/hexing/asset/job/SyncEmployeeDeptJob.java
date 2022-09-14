package com.hexing.asset.job;

import com.hexing.system.service.ISysUserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class SyncEmployeeDeptJob {

    @Resource
    private ISysUserService userService;

    @Scheduled(cron = "0 0 8,12,17,21 * * ? ")
    private void autoEmployeeTransfer() {
        userService.syncDepartmentUserList();
    }

}
