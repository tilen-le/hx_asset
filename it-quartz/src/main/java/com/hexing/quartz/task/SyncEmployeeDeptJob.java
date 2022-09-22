package com.hexing.quartz.task;

import com.hexing.system.service.ISysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component("SyncEmployeeDeptJob")
public class SyncEmployeeDeptJob {

    @Resource
    private ISysUserService userService;

    public void autoEmployeeTransfer() {
        userService.syncDepartmentUserList();
    }

}
