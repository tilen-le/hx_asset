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
import com.hexing.common.exception.ServiceException;
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
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        if (StringUtils.isNotBlank(assetInventoryTask.getTaskName())) {
            wrapper.like(AssetInventoryTask::getTaskName, assetInventoryTask.getTaskName());
        }
        if (StringUtils.isNotBlank(assetInventoryTask.getCreateBy())) {
            wrapper.eq(AssetInventoryTask::getCreateBy, assetInventoryTask.getCreateBy());
        }
        if (StringUtils.isNotBlank(assetInventoryTask.getStatus())) {
            wrapper.eq(AssetInventoryTask::getStatus, assetInventoryTask.getStatus());
        }
        if (StringUtils.isNotBlank(assetInventoryTask.getCreatorName())) {
            wrapper.like(AssetInventoryTask::getCreatorName, assetInventoryTask.getCreatorName());
        }
        if (ObjectUtil.isNotNull(assetInventoryTask.getStartDate())) {
            wrapper.ge(AssetInventoryTask::getStartDate, assetInventoryTask.getStartDate());
        }
        if (ObjectUtil.isNotNull(assetInventoryTask.getEndDate())) {
            wrapper.le(AssetInventoryTask::getEndDate, assetInventoryTask.getEndDate());
        }
        startPage();
        String userName = SecurityUtils.getLoginUser().getUser().getUserName();
        if(!"admin".equals(userName)){
            wrapper.apply("(find_in_set( {0} , inventory_users ) or create_by = {0})", userName);
        }
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
            task.setCreatorName(sysUser.getNickName());
            String inventoryUsers = task.getInventoryUsers();
            if (inventoryUsers.contains(",")){
                String[] split=inventoryUsers.split(",");
                String inventoryUsersName ="";
                for (int i = 0; i < split.length; i++) {
                    SysUser sysUser1 = sysUserService.selectUserByUserName(split[i].trim());
                    inventoryUsersName +=sysUser1.getNickName()+",";
                }
                String substring = inventoryUsersName.substring(0, inventoryUsersName.lastIndexOf(","));
                task.setInventoryUsersName(substring);
            }else {
                SysUser sysUser1 = sysUserService.selectUserByUserName(inventoryUsers.trim());
                task.setInventoryUsersName(sysUser1.getNickName());
            }

            SysDept dept = sysDeptService.selectDeptById(Long.valueOf(task.getInventoryDept()));
            task.setInventoryDeptName(dept.getDeptName());

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

        LambdaQueryWrapper<AssetInventoryTask> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AssetInventoryTask::getTaskName,task.getTaskName());
        List<AssetInventoryTask> assetInventoryTasks = assetInventoryTaskMapper.selectList(lambdaQueryWrapper);
        if (assetInventoryTasks.size()>0){
            throw new ServiceException("盘点任务名称重复");
        }

        Long deptId = Long.valueOf(task.getInventoryDept());
        //查询所以子部门
        List sysDeptList = sysDeptService.selectDeptByParentId(deptId);
        sysDeptList.add(deptId.toString());
        //查询部门所有人员
        List sysUserList = sysUserService.selectUserByDeptId(sysDeptList);
        List<List> userCodeList = new ArrayList();
        while(sysUserList.size()>1000){
            userCodeList.add(sysUserList.subList(0,1000));
            sysUserList = sysUserList.subList(1000,sysUserList.size());
        }
        if (sysUserList.size()>0){
            userCodeList.add(sysUserList);
        }
        //查询人员资产
        List list = new ArrayList();
        for (List userCode:userCodeList){
            List assetList = assetMapper.selectAssetsByUserCodes(userCode);
            for (Object assetCode : assetList) {
                if (ObjectUtil.isNotNull(assetCode)){
                    list.add(assetCode.toString());
                }
            }
        }
        task.setCreateTime(new Date());
        String str = DateUtils.dateTimeNow();
        str += RandomUtil.randomString(4);
        task.setTaskCode(str);

//        String beginDateTime = DateFormatUtils.format(task.getStartDate(), "yyyy-MM-dd 00:00:00");
//        String endDateTime = DateFormatUtils.format(task.getEndDate(), "yyyy-MM-dd 23:59:59");
//
//        try {
//            task.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beginDateTime));
//            task.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateTime));
//        } catch (Exception e) {
//            log.error("盘点任务始末时间设置出错", e);
//        }
        String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        try {
            Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            if (task.getEndDate().getTime() < parse.getTime()) {
                throw new ServiceException("盘点结束时间设置错误");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (task.getEndDate().getTime() < task.getStartDate().getTime()) {
            throw new ServiceException("盘点开始结束时间设置错误");
        }
        String userName = SecurityUtils.getLoginUser().getUser().getUserName();
        String user = SecurityUtils.getLoginUser().getUser().getNickName();
        task.setCreateBy(userName);
        task.setCreatorName(user);
        if (!CollectionUtils.isEmpty(task.getInventoryUserList())) {
            task.setInventoryUsers(task.getInventoryUserList().stream().collect(Collectors.joining(",")));
        }
        if (list.size()>0){
            task.setStatus(CountingTaskStatus.COUNTING.getStatus());
        }else {
            task.setStatus(CountingTaskStatus.FINISHED.getStatus());
        }
        assetInventoryTaskMapper.insert(task);//创建盘点任务

        List<AssetProcess> assetProcessList =new ArrayList<>();
        for(Object assetCode:list){
            AssetProcess assetProcess = new AssetProcess();
            assetProcess.setAssetCode(assetCode.toString());
            assetProcess.setUserCode(userName);
            assetProcess.setProcessType("1000");
            assetProcess.setCreateTime(new Date());
            assetProcessList.add(assetProcess);
        }
        if (assetProcessList.size()>0){
            assetProcessService.saveBatch(assetProcessList);//创建总流程
        }

        List<AssetProcessCounting> assetProcessCountingList =new ArrayList<>();
        for(AssetProcess assetProcess:assetProcessList){
            AssetProcessCounting entity = new AssetProcessCounting();
            entity.setTaskCode(task.getTaskCode());
            entity.setAssetCode(assetProcess.getAssetCode());
            entity.setProcessId(assetProcess.getId());
            entity.setCreateTime(new Date());
            entity.setCountingStatus(AssetCountingStatus.NOT_COUNTED.getStatus());
            assetProcessCountingList.add(entity);
        }
        if (assetProcessCountingList.size()>0){
            assetProcessCountingService.saveBatch(assetProcessCountingList);//创建盘点资产流程
        }

        List<String> inventoryUserList = task.getInventoryUserList();
        String title = "盘点任务名称 :" + task.getTaskName()
                + "   \n盘点开始时间 :" + DateUtils.parseDateToStr("YYYY-MM-dd", task.getStartDate())
                + "   \n盘点结束时间 :" + DateUtils.parseDateToStr("YYYY-MM-dd", task.getEndDate());
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

    /**
     * 更新盘点任务状态
     */
    @Override
    public void updateInventoryTaskStatus() {
        // 查询所有未完成的盘点任务
        List<AssetInventoryTask> taskList = assetInventoryTaskMapper.selectList(new LambdaQueryWrapper<AssetInventoryTask>()
                .eq(AssetInventoryTask::getStatus, CountingTaskStatus.COUNTING.getStatus()));
        for (AssetInventoryTask task : taskList) {
            // 是否满足完成条件：当前时间大于盘点任务结束时间
            LocalDateTime localDateTime = LocalDateTime
                    .ofInstant(Instant.ofEpochMilli(task.getEndDate().getTime()), ZoneId.systemDefault());
            LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
            Date endMomentOfEndDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
            if (new Date().compareTo(endMomentOfEndDate) > 0) {
                task.setStatus(CountingTaskStatus.FINISHED.getStatus());
            }
        }
    }

}
