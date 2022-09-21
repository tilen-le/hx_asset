package com.hexing.asset.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetInventoryTask;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.enums.AssetCountingStatus;
import com.hexing.asset.enums.CountingTaskStatus;
import com.hexing.asset.mapper.AssetInventoryTaskMapper;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.mapper.AssetProcessMapper;
import com.hexing.asset.service.IAssetInventoryTaskService;
import com.hexing.asset.service.IAssetProcessCountingService;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.framework.manager.AsyncManager;
import com.hexing.framework.manager.factory.AsyncFactory;
import com.hexing.system.service.impl.SysDeptServiceImpl;
import com.hexing.system.service.impl.SysUserServiceImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hexing.common.utils.PageUtil.startPage;


/**
 * 盘点任务Service业务层处理
 *
 * @author zxy
 * @date 2022-09-13
 */
@Service
public class AssetInventoryTaskServiceImpl extends ServiceImpl<AssetInventoryTaskMapper, AssetInventoryTask> implements IAssetInventoryTaskService
{



    @Autowired
    private AssetInventoryTaskMapper assetInventoryTaskMapper;
    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private AssetProcessMapper assetProcessMapper;

    @Autowired
    private IAssetProcessCountingService assetProcessCountingService;
    @Autowired
    private IAssetProcessService assetProcessService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysDeptServiceImpl sysDeptService;

    /**
     * 查询盘点任务
     *
     * @param taskId 盘点任务主键
     * @return 盘点任务
     */
    @Override
    public AssetInventoryTask selectAssetCountingTaskByTaskId(String taskId)
    {
        return assetInventoryTaskMapper.selectAssetCountingTaskByTaskId(taskId);
    }

    /**
     * 查询盘点任务列表
     *
     * @param assetInventoryTask 盘点任务
     * @return 盘点任务
     */
    @Override
    public List<AssetInventoryTask> selectAssetCountingTaskList(AssetInventoryTask assetInventoryTask)
    {
        LambdaQueryWrapper<AssetInventoryTask> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(assetInventoryTask.getTaskCode())) {
            wrapper.like(AssetInventoryTask::getTaskCode, assetInventoryTask.getTaskCode());
        }
        if (StringUtils.isNotBlank(assetInventoryTask.getCreateBy())) {
            wrapper.eq(AssetInventoryTask::getCreateBy, assetInventoryTask.getCreateBy());
        }
        if (ObjectUtil.isNotNull(assetInventoryTask.getStartDate())) {
            wrapper.ge(AssetInventoryTask::getStartDate, assetInventoryTask.getStartDate());
        }
        if (ObjectUtil.isNotNull(assetInventoryTask.getEndDate())) {
            wrapper.le(AssetInventoryTask::getEndDate, assetInventoryTask.getEndDate());
        }
        startPage();
        List<AssetInventoryTask> taskList = assetInventoryTaskMapper.selectList(wrapper);
        for (AssetInventoryTask task : taskList) {
            JSONObject countResult = assetProcessCountingService.countingStatusCount(task.getTaskCode());
            int total = countResult.getIntValue("total");
            int notCounted = countResult.getIntValue("notCounted");
            int counted = countResult.getIntValue("counted");
            int abnormal = countResult.getIntValue("abnormal");
            task.setAssetTotal(total);
            task.setAssetNotCounted(notCounted);
            task.setAssetCounted(counted);
            task.setAssetAbnormal(abnormal);
            SysUser sysUser = sysUserService.selectUserByUserName(task.getCreateBy());
            task.setCreateByName(sysUser.getNickName());
            String inventoryUsers = task.getInventoryUsers();
            if (inventoryUsers.contains(",")){
                String[] split=inventoryUsers.substring(1,inventoryUsers.lastIndexOf("]")).split(",");
                String inventoryUsersName ="";
                for (int i = 0; i < split.length; i++) {
                    SysUser sysUser1 = sysUserService.selectUserByUserName(split[i].trim());
                    inventoryUsersName +=sysUser1.getNickName()+",";
                }
                String substring = inventoryUsersName.substring(0, inventoryUsersName.lastIndexOf(","));
                task.setInventoryUsersName(substring);
            }else {
                String s=inventoryUsers.substring(1,inventoryUsers.lastIndexOf("]"));
                SysUser sysUser1 = sysUserService.selectUserByUserName(s.trim());
                task.setInventoryUsersName(sysUser1.getNickName());
            }

            SysDept dept = sysDeptService.selectDeptById(Long.valueOf(task.getInventoryDept()));
            task.setInventoryDeptName(dept.getDeptName());
            if (CountingTaskStatus.COUNTING.getStatus().equals(task.getStatus())) {
                if (new Date().compareTo(task.getEndDate()) > 0 || notCounted == 0) {
                    task.setStatus(CountingTaskStatus.FINISHED.getStatus());
//                    assetInventoryTaskMapper.updateById(task);
                }
            } else {
                task.setStatus(CountingTaskStatus.COUNTING.getStatus());
            }
        }

        return taskList;
    }

    /**
     * 新增盘点任务
     */
    @Override
    @Transactional
    public int insertAssetCountingTask(AssetInventoryTask task) {
        QueryWrapper<Asset> wrapper = new QueryWrapper<>();
        wrapper.setEntity(new Asset());

        Long deptId = Long.valueOf(task.getInventoryDept());
        List<SysDept> sysDeptList = sysDeptService.selectDeptByParentId(deptId);
        SysDept sysDept = new SysDept();
        sysDept.setDeptId(deptId);
        sysDeptList.add(sysDept);
        List<Asset> list = new ArrayList();
        for (SysDept sd : sysDeptList) {
            List<SysUser> sysUserList = sysUserService.selectUserByDeptId(sd.getDeptId());
            for (SysUser s : sysUserList) {
                wrapper.getEntity().setResponsiblePersonCode(s.getUserName());
                List<Asset> assetList = assetMapper.selectList(wrapper);
                if (assetList.size() > 0) {
                    for (Asset asset : assetList) {
                        list.add(asset);
                    }
                }
            }
        }
        task.setCreateTime(new Date());
        String str = DateUtils.dateTimeNow();
        str += RandomUtil.randomString(4);
        task.setTaskCode(str);

        String beginDateTime = DateFormatUtils.format(task.getStartDate(), "yyyy-MM-dd 00:00:00");
        String endDateTime = DateFormatUtils.format(task.getEndDate(), "yyyy-MM-dd 23:59:59");

        try {
            task.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beginDateTime));
            task.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateTime));
        } catch (Exception e) {
            log.error("盘点任务始末时间设置出错", e);
        }

        if (task.getEndDate().getTime() < task.getStartDate().getTime()) {
            return 0;
        }
        String userName = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userName = "1";
        task.setCreateBy(userName);
        if (task.getInventoryUserList() != null) {
            task.setInventoryUsers(task.getInventoryUserList().toString());
        }
        task.setStatus(CountingTaskStatus.COUNTING.getStatus());
        assetInventoryTaskMapper.insert(task);
        for (Asset asset : list) {
            AssetProcess assetProcess = new AssetProcess();
            assetProcess.setAssetCode(asset.getAssetCode());
            assetProcess.setUserCode(userName);
            assetProcess.setProcessType("1000");
            assetProcess.setCreateTime(new Date());
            assetProcessMapper.insert(assetProcess);

            AssetProcessCounting entity = new AssetProcessCounting();
            entity.setTaskCode(task.getTaskCode());
            entity.setAssetCode(asset.getAssetCode());
            entity.setProcessId(assetProcess.getId());
            entity.setCreateTime(new Date());
            entity.setCountingStatus(AssetCountingStatus.NOT_COUNTED.getStatus());
            assetProcessCountingService.insertAssetProcessCounting(entity);
        }
        List<String> inventoryUserList = task.getInventoryUserList();
//        List<String> inventoryUserList = new ArrayList<>();
//        inventoryUserList.add("80010712");
        String title = "盘点任务编码 :" + task.getTaskCode()
                + "\n盘点开始时间 :" + DateUtils.parseDateToStr("YYYY-MM-dd", task.getStartDate())
                + "\n盘点结束时间 :" + DateUtils.parseDateToStr("YYYY-MM-dd", task.getEndDate());
        AsyncManager.me().execute(AsyncFactory.sendDingNotice(inventoryUserList, title));
        return 1;
    }

    /**
     * 修改盘点任务
     *
     * @param assetInventoryTask 盘点任务
     * @return 结果
     */
    @Override
    public int updateAssetCountingTask(AssetInventoryTask assetInventoryTask)
    {
        return assetInventoryTaskMapper.updateAssetCountingTask(assetInventoryTask);
    }

    /**
     * 批量删除盘点任务
     *
     * @param taskIds 需要删除的盘点任务主键
     * @return 结果
     */
    @Override
    public int deleteAssetCountingTaskByTaskIds(List<String> taskCode)
    {
        return assetInventoryTaskMapper.deleteBatchIds(taskCode);
    }

    /**
     * 删除盘点任务信息
     *
     * @param taskId 盘点任务主键
     * @return 结果
     */
    @Override
    public int deleteAssetCountingTaskByTaskId(String taskId)
    {
        return assetInventoryTaskMapper.deleteAssetCountingTaskByTaskId(taskId);
    }
}
