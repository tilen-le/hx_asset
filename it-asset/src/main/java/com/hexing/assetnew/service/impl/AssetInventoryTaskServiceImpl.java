package com.hexing.assetnew.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.enums.AssetCountingStatus;
import com.hexing.asset.enums.CountingTaskStatus;
import com.hexing.assetnew.domain.Asset;
import com.hexing.assetnew.domain.AssetInventoryTask;
import com.hexing.assetnew.domain.AssetProcessCountingDomain;
import com.hexing.assetnew.domain.AssetsProcess;
import com.hexing.assetnew.domain.dto.CountingStatusNumDTO;
import com.hexing.assetnew.enums.AssetProcessType;
import com.hexing.assetnew.mapper.AssetInventoryTaskMapper;
import com.hexing.assetnew.service.IAssetInventoryTaskService;
import com.hexing.assetnew.service.IAssetService;
import com.hexing.assetnew.service.IAssetsProcessService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
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
public class AssetInventoryTaskServiceImpl extends ServiceImpl<AssetInventoryTaskMapper, AssetInventoryTask> implements IAssetInventoryTaskService {

    @Autowired
    private AssetInventoryTaskMapper assetInventoryTaskMapper;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysDeptServiceImpl sysDeptService;
    @Autowired
    private IAssetsProcessService assetsProcessService;

    /**
     * 查询盘点任务列表
     */
    @Override
    public List<AssetInventoryTask> selectAssetCountingTaskList(AssetInventoryTask assetInventoryTask) {
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
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if (!user.isAdmin()) {
            String userName = user.getUserName();
            wrapper.apply("(find_in_set( {0} , inventory_users ) or create_by = {0})", userName);
        }
        List<AssetInventoryTask> taskList = assetInventoryTaskMapper.selectList(wrapper);

        List<SysUser> sysUsers = sysUserService.selectUserList(new SysUser());
        List<SysDept> sysDepts = sysDeptService.selectDeptList(new SysDept());

        for (AssetInventoryTask task : taskList) {
            CountingStatusNumDTO numDTO = assetsProcessService.countingStatusCount(task.getTaskCode());
            task.setAssetTotal(numDTO.getTotal());
            task.setAssetNotCounted(numDTO.getNotCounted());
            task.setAssetCounted(numDTO.getCounted());
            task.setAssetAbnormal(numDTO.getAbnormal());
            String userName = sysUsers.stream().filter(sysUser -> sysUser.getUserName().equals(task.getCreateBy())).map(SysUser::getNickName).findFirst().orElse(null);
            task.setCreatorName(userName);
            String inventoryUsers = task.getInventoryUsers();
            if (inventoryUsers.contains(",")) {
                String[] split = inventoryUsers.split(",");
                String inventoryUsersName = "";
                for (int i = 0; i < split.length; i++) {
                    String trim = split[i].trim();
                    userName = sysUsers.stream().filter(sysUser -> sysUser.getUserName().equals(trim)).map(SysUser::getNickName).findFirst().orElse(null);
                    inventoryUsersName += userName + ",";
                }
                String substring = inventoryUsersName.substring(0, inventoryUsersName.lastIndexOf(","));
                task.setInventoryUsersName(substring);
            } else {
                userName = sysUsers.stream().filter(sysUser -> sysUser.getUserName().equals(inventoryUsers)).map(SysUser::getNickName).findFirst().orElse(null);
                task.setInventoryUsersName(userName);
            }
            String deptName = sysDepts.stream().filter(sysDept -> sysDept.getDeptId().equals(Long.valueOf(task.getInventoryDept()))).map(SysDept::getDeptName).findFirst().orElse(null);
            task.setInventoryDeptName(deptName);
        }

        return taskList;
    }

    /**
     * 新增盘点任务
     */
    @Override
    @Transactional
    public boolean insertAssetCountingTask(AssetInventoryTask task) {

        Date now = DateUtils.getNowDate();

        // 盘点任务开始结束时间校验
        if (task.getEndDate().before(now)) {
            throw new ServiceException("盘点结束时间设置错误");
        }
        if (task.getEndDate().before(task.getStartDate())) {
            throw new ServiceException("盘点开始结束时间设置错误");
        }
        // 盘点任务名称重复性校验
        AssetInventoryTask sameNameTask = assetInventoryTaskMapper.selectOne(new LambdaQueryWrapper<AssetInventoryTask>()
                .eq(AssetInventoryTask::getTaskName, task.getTaskName()));
        if (ObjectUtil.isNotNull(sameNameTask)) {
            throw new ServiceException("盘点任务名称重复");
        }

        // 查询部门下所有员工名下的资产

        List<Asset> assetList = assetService.selectAssetByDeptId(Long.valueOf(task.getInventoryDept()));
        List<String> assetCodeList = assetList.stream().map(Asset::getAssetCode).collect(Collectors.toList());

        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userName = SecurityUtils.getLoginUser().getUser().getNickName();

        task.setStatus(CountingTaskStatus.COUNTING.getStatus());
        task.setTaskCode(generateTaskCode());
        if (!CollectionUtils.isEmpty(task.getInventoryUserList())) {
            task.setInventoryUsers(String.join(",", task.getInventoryUserList()));
        }
        task.setCreateBy(userCode);
        task.setCreatorName(userName);
        task.setCreateTime(now);

        if (assetCodeList.size() == 0) {
            task.setStatus(CountingTaskStatus.FINISHED.getStatus());
            this.save(task);
            return true;
        }

        // 创建盘点任务
        this.save(task);

        List<AssetsProcess> processList = new ArrayList<>();
        for (String assetCode : assetCodeList) {
            AssetsProcess assetsProcess = new AssetsProcess();
            assetsProcess.setAssetCode(assetCode)
                    .setCreateBy(userCode)
                    .setProcessType(AssetProcessType.COUNTING_PROCESS.getCode())
//                    .setStatus(CountingTaskStatus.COUNTING.getStatus())
                    .setCreateTime(now);
            processList.add(assetsProcess);
        }

        assetsProcessService.saveBatch(processList);//创建总流程

        // 创建盘点流程
        List<AssetProcessCountingDomain> domainList = new ArrayList<>();
        for (AssetsProcess process : processList) {
            AssetProcessCountingDomain entity = new AssetProcessCountingDomain();
            entity.setId(process.getId());
            entity.setTaskCode(task.getTaskCode());
            entity.setAssetCode(process.getAssetCode());
            entity.setUserCode(task.getCreateBy());
            entity.setCreateTime(now);
            entity.setCountingStatus(AssetCountingStatus.NOT_COUNTED.getStatus());
            domainList.add(entity);
        }
        assetsProcessService.saveBatchProcess(domainList);//创建盘点资产流程

        List<String> inventoryUserList = task.getInventoryUserList();
        String title = "盘点任务名称 :" + task.getTaskName()
                + "   \n盘点开始时间 :" + DateUtils.parseDateToStr("YYYY-MM-dd", task.getStartDate())
                + "   \n盘点结束时间 :" + DateUtils.parseDateToStr("YYYY-MM-dd", task.getEndDate());
        AsyncManager.me().execute(AsyncFactory.sendDingNotice(inventoryUserList, title));

        return true;
    }

    /**
     * 批量删除盘点任务
     */
    @Override
    public int deleteAssetCountingTaskByTaskIds(List<String> taskCode) {
        return assetInventoryTaskMapper.deleteBatchIds(taskCode);
    }

    /**
     * 定时任务：更新盘点任务状态
     */
    @Override
    public void updateInventoryTaskStatus() {
        // 查询所有未完成的盘点任务
        List<AssetInventoryTask> taskList = assetInventoryTaskMapper.selectList(new LambdaQueryWrapper<AssetInventoryTask>()
                .eq(AssetInventoryTask::getStatus, CountingTaskStatus.COUNTING.getStatus()));
        for (AssetInventoryTask task : taskList) {
            // 是否满足完成条件：当前时间大于盘点任务结束时间
            LocalDateTime lastMomentOfDate = DateUtils.getLastMomentOfDate(task.getEndDate());
            if (LocalDateTime.now().isAfter(lastMomentOfDate)) {
                task.setStatus(CountingTaskStatus.FINISHED.getStatus());
            }
        }
    }


    /**
     * 生成盘点任务编码
     */
    private String generateTaskCode() {
        String taskCode = DateUtils.dateTimeNow();
        taskCode += RandomUtil.randomString(4);
        return taskCode;
    }

}
