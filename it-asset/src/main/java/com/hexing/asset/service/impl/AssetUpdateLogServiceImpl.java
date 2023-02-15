package com.hexing.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetUpdateLog;
import com.hexing.asset.domain.vo.AssetProcessParam;
import com.hexing.asset.mapper.AssetUpdateLogMapper;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.ISysDeptService;
import com.hexing.system.service.impl.SysUserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.hexing.common.utils.PageUtil.startPage;

/**
 * 资产信息更新日志Service业务层处理
 *
 * @author zxy
 * @date 2022-10-19
 */
@Service
public class AssetUpdateLogServiceImpl extends ServiceImpl<AssetUpdateLogMapper, AssetUpdateLog> implements IAssetUpdateLogService {

    @Autowired
    private AssetUpdateLogMapper assetUpdateLogMapper;
    @Autowired
    private IAssetUpdateLogService logService;
    @Autowired
    private IAssetProcessService processService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private SysUserServiceImpl sysUserService;

    /**
     * 创建资产信息更新日志
     *
     * @param asset 资产旧信息
     * @param process 主流程
     * @return
     */
    @Override
    public int saveLog(Asset asset, AssetProcess process) {
        AssetUpdateLog log = new AssetUpdateLog();
        BeanUtils.copyProperties(asset, log);
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userName = SecurityUtils.getLoginUser().getUser().getNickName();
//        String userCode = "80010712";
//        String userName = "PFC";
        if (StringUtils.isNotBlank(process.getId().toString())) {
            log.setProcessId(process.getId().toString());
            log.setProcessType(process.getProcessType());
        }
        log.setCreateBy(userCode);
        log.setCreateTime(DateUtils.getNowDate());

        return  assetUpdateLogMapper.insert(log);
    }

    //保管记录
    @Override
    public List<AssetUpdateLog> custodyLogList(AssetProcessParam assetProcess) {
        LambdaQueryWrapper<AssetUpdateLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetUpdateLog::getAssetCode,assetProcess.getAssetCode());
        wrapper.orderByDesc(AssetUpdateLog::getCreateTime);
        List<AssetUpdateLog> list = logService.list(wrapper);
        return list;
    }

    //工单记录
    @Override
    public List<AssetProcess> workLogList(AssetProcessParam assetProcess) {
        AssetProcess process=new AssetProcess();
        process.setAssetCode(assetProcess.getAssetCode());
        List<AssetProcess> list = processService.listByPage(process);
        return list;
    }

    //操作记录
    @Override
    public List<AssetUpdateLog> operationLogList(AssetProcessParam assetProcess) {
        LambdaQueryWrapper<AssetUpdateLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetUpdateLog::getAssetCode,assetProcess.getAssetCode());
        wrapper.orderByDesc(AssetUpdateLog::getCreateTime);
        startPage();
        List<AssetUpdateLog> list = logService.list(wrapper);
        List<SysDept> depts = sysDeptService.selectDeptList(new SysDept());
        List<SysUser> sysUsers = sysUserService.selectUserList(new SysUser());
        for (AssetUpdateLog updateLog : list) {
            SysUser sysUser = sysUsers.stream().filter(x -> x.getUserName().equals(updateLog.getCreateBy())).findFirst().orElse(new SysUser());
           if (StringUtils.isNotBlank(updateLog.getResponsiblePersonDept())){
               SysDept dept = depts.stream().filter(x -> x.getDeptId().equals(Long.valueOf(updateLog.getResponsiblePersonDept()))).findFirst().orElse(new SysDept());
               updateLog.setResponsiblePersonDeptName(dept.getDeptName());
           }
            updateLog.setCreateBy(sysUser.getNickName());
        }

        return list;
    }

    //操作记录详情
    @Override
    public AssetUpdateLog getOperationLogById(Long id) {
        LambdaQueryWrapper<AssetUpdateLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetUpdateLog::getId,id);
        return assetUpdateLogMapper.selectOne(wrapper);
    }
}
