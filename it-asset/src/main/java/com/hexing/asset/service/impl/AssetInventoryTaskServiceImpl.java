package com.hexing.asset.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.domain.AssetInventoryTask;
import com.hexing.asset.domain.dto.AssetInventoryTaskDTO;
import com.hexing.asset.enums.AssetCountingStatus;
import com.hexing.asset.mapper.AssetInventoryTaskMapper;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.mapper.AssetProcessMapper;
import com.hexing.asset.service.IAssetProcessCountingService;
import com.hexing.asset.service.IAssetInventoryTaskService;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.system.service.impl.SysDeptServiceImpl;
import com.hexing.system.service.impl.SysUserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
        QueryWrapper<AssetInventoryTask> wrapper = new QueryWrapper<>();
        return assetInventoryTaskMapper.selectList(wrapper);
    }

    /**
     * 新增盘点任务
     */
    @Override
    public int insertAssetCountingTask(AssetInventoryTask task)
    {
        QueryWrapper<Asset> wrapper = new QueryWrapper<>();
        wrapper.setEntity(new Asset());

        Long deptId =  Long.valueOf(task.getInventoryDept());
        List<SysDept> sysDeptList = sysDeptService.selectDeptByParentId(deptId);
        SysDept sysDept =new SysDept();
        sysDept.setDeptId(deptId);
        sysDeptList.add(sysDept);
        List<Asset>  list =new ArrayList();
        for (SysDept sd : sysDeptList) {
            List<SysUser> sysUserList = sysUserService.selectUserByDeptId(sd.getDeptId());
            for (SysUser s : sysUserList) {
                wrapper.getEntity().setResponsiblePersonCode(s.getUserName());
                List<Asset> assetList = assetMapper.selectList(wrapper);
                if (assetList.size()>0){
                    for (Asset asset : assetList) {
                        list.add(asset);
                    }
                }
            }
        }
        task.setCreateTime(new Date());
        String str = DateUtils.dateTimeNow();
        str+=RandomUtil.randomString(15);
        task.setTaskCode(str);
//        String userName = SecurityUtils.getLoginUser().getUser().getUserName();
        String userName = "1";
        task.setCreateBy(userName);
        if (task.getInventoryUserList()!=null){
            task.setInventoryUsers(task.getInventoryUserList().toString());
        }
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
            entity.setAssetCode(asset.getCreateBy());
            entity.setProcessId(assetProcess.getId());
            entity.setCreateTime(new Date());
            entity.setCountingStatus(AssetCountingStatus.NOT_COUNTED.getStatus());
            assetProcessCountingService.insertAssetProcessCounting(entity);
        }

        return assetInventoryTaskMapper.insert(task);
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
    public int deleteAssetCountingTaskByTaskIds(String[] taskIds)
    {
        return assetInventoryTaskMapper.deleteAssetCountingTaskByTaskIds(taskIds);
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
