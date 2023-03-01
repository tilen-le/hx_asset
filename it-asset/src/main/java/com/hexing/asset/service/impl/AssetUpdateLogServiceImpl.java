package com.hexing.asset.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.*;
import com.hexing.asset.domain.dto.MaterialCategorySimpleDTO;
import com.hexing.asset.domain.vo.AssetProcessParam;
import com.hexing.asset.domain.vo.AssetProcessReturn;
import com.hexing.asset.mapper.AssetUpdateLogMapper;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.asset.utils.CodeUtil;
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
     * @param asset   资产旧信息
     * @param process 主流程
     * @return
     */
    @Override
    public int saveLog(Asset asset, AssetProcess process) {
        AssetUpdateLog log = new AssetUpdateLog();
        BeanUtils.copyProperties(asset, log);
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String processId = String.valueOf(process.getId());
        if (StringUtils.isNotBlank(processId)) {
            log.setProcessId(processId);
            log.setProcessType(process.getProcessType());
        }
        log.setCreateBy(user.getUserName());
        log.setCreateTime(DateUtils.getNowDate());
        return assetUpdateLogMapper.insert(log);
    }

    //保管记录
    @Override
    public List<AssetUpdateLog> custodyLogList(AssetProcessParam assetProcess) {
        LambdaQueryWrapper<AssetUpdateLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetUpdateLog::getAssetCode, assetProcess.getAssetCode());
        wrapper.orderByDesc(AssetUpdateLog::getCreateTime);
        List<AssetUpdateLog> list = logService.list(wrapper);
        List<AssetUpdateLog> paramsData = new ArrayList<>();
        String personCode = "init";
        String deptCode = "init";
        List<SysDept> depts = sysDeptService.selectDeptList(new SysDept());
        List<SysUser> sysUsers = sysUserService.selectUserList(new SysUser());
        for (AssetUpdateLog log : list) {
            SysUser sysUser = sysUsers.stream().filter(x -> x.getUserName().equals(log.getCreateBy())).findFirst().orElse(new SysUser());
            log.setCreateBy(sysUser.getNickName());
            if (StringUtils.isNotBlank(log.getResponsiblePersonDept())) {
                SysDept dept = depts.stream().filter(x -> x.getDeptId().equals(Long.valueOf(log.getResponsiblePersonDept()))).findFirst().orElse(new SysDept());
                log.setResponsiblePersonDeptName(dept.getDeptName());
            }
            String responsiblePersonCode = log.getResponsiblePersonCode();
            String responsiblePersonDept = log.getResponsiblePersonDept();
            if (StringUtils.isBlank(responsiblePersonCode)) {
                responsiblePersonCode = "";
            }
            if (StringUtils.isBlank(responsiblePersonDept)) {
                responsiblePersonDept = "";
            }

            if (!responsiblePersonCode.equals(personCode) || !responsiblePersonDept.equals(deptCode)) {
                if (paramsData.size() > 0) {
                    AssetUpdateLog previous = paramsData.get(paramsData.size() - 1);
                    if (Objects.nonNull(previous)) {
                        log.setEndTime(previous.getCreateTime());
                    }
                }
                paramsData.add(log);
                personCode = responsiblePersonCode;
                deptCode = responsiblePersonDept;
            }
        }
        return paramsData;
    }

    //工单记录
    @Override
    public List<AssetProcessReturn> workLogList(AssetProcessParam assetProcess) {
        AssetProcess process = new AssetProcess();
        process.setAssetCode(assetProcess.getAssetCode());
        List<AssetProcess> list = processService.list(process);
        List<AssetProcessReturn> domains = new ArrayList<>();
        List<SysDept> depts = sysDeptService.selectDeptList(new SysDept());
        List<SysUser> sysUsers = sysUserService.selectUserList(new SysUser());
        for (AssetProcess ap : list) {
            AssetProcessReturn domain = processService.convertProcess(ap, new AssetProcessReturn());
            SysUser sysUser = sysUsers.stream().filter(x -> x.getUserName().equals(domain.getCreateBy())).findFirst().orElse(new SysUser());
            if (StringUtils.isNotBlank(domain.getResponsiblePersonDept())) {
                SysDept dept = depts.stream().filter(x -> x.getDeptId().equals(Long.valueOf(domain.getResponsiblePersonDept()))).findFirst().orElse(new SysDept());
                domain.setResponsiblePersonDeptName(dept.getDeptName());
            }
            domain.setCreateBy(sysUser.getNickName());
            domains.add(domain);
        }
        return domains;
    }

    //操作记录
    @Override
    public List<AssetUpdateLog> operationLogList(AssetProcessParam assetProcess) {
        LambdaQueryWrapper<AssetUpdateLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetUpdateLog::getAssetCode, assetProcess.getAssetCode());
        wrapper.orderByDesc(AssetUpdateLog::getCreateTime);
        startPage();
        List<AssetUpdateLog> list = logService.list(wrapper);
        List<SysDept> depts = sysDeptService.selectDeptList(new SysDept());
        List<SysUser> sysUsers = sysUserService.selectUserList(new SysUser());
        for (AssetUpdateLog updateLog : list) {
            SysUser sysUser = sysUsers.stream().filter(x -> x.getUserName().equals(updateLog.getCreateBy())).findFirst().orElse(new SysUser());
            if (StringUtils.isNotBlank(updateLog.getResponsiblePersonDept())) {
                SysDept dept = depts.stream().filter(x -> x.getDeptId().equals(Long.valueOf(updateLog.getResponsiblePersonDept()))).findFirst().orElse(new SysDept());
                updateLog.setResponsiblePersonDeptName(dept.getDeptName());
            }
            updateLog.setCreateBy(sysUser.getNickName());
        }

        return list;
    }

    //操作记录详情
    @Override
    public Map getOperationLogById(Long id) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<AssetUpdateLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetUpdateLog::getId, id);
        AssetUpdateLog updateLog = assetUpdateLogMapper.selectOne(wrapper);
        // 保管人和保管部门
        if (StringUtils.isNotEmpty(updateLog.getResponsiblePersonDept())) {
            SysDept dept = sysDeptService.selectDeptById(Long.valueOf(updateLog.getResponsiblePersonDept()));
            if (Objects.nonNull(dept)) {
                updateLog.setResponsiblePersonDeptName(dept.getDeptName());
            }
        }
        if (StringUtils.isNotEmpty(updateLog.getResponsiblePersonCode())) {
            SysUser user = sysUserService.getUserByUserName(updateLog.getResponsiblePersonCode());
            if (Objects.nonNull(user)) {
                updateLog.setResponsiblePersonName(user.getNickName());
            }
        }
        AssetManagementConfig config = new AssetManagementConfig();
        config.setAssetType(updateLog.getAssetType());
        config.setAssetCategory(updateLog.getAssetCategory());
        config.setAssetSubCategory(updateLog.getAssetSubCategory());
        MaterialCategorySimpleDTO dto = CodeUtil.getAssetTypeName(config);
        updateLog.setAssetType(dto.getAssetType());
        updateLog.setAssetCategory(dto.getAssetCategory());
        updateLog.setAssetSubCategory(dto.getAssetSubCategory());
        AssetProcess process = null;
        if (StringUtils.isNotBlank(updateLog.getProcessId())) {
            process = new AssetProcess();
            process.setAssetCode(updateLog.getAssetCode());
            process.setId(Long.valueOf(updateLog.getProcessId()));
            List<AssetProcess> list = processService.list(process);
            process = list.stream().findFirst().orElse(null);
            handleVariable(process.getVariableList());
        }
        result.put("processLog", process);
//        JSONObject domain =new JSONObject();
//        if (Objects.nonNull(process)){
//            domain = processService.convertProcessGetLabel(process, new JSONObject());
//        }
        result.put("updateLog", updateLog);

        return result;
    }

    private void handleVariable(List<AssetProcessVariable> variableList) {
        //处理日期格式
        if (CollectionUtil.isEmpty(variableList)) {
            return;
        }
        for (AssetProcessVariable variable : variableList) {
            String fieldValue = variable.getFieldValue();
            if (StringUtils.isNotBlank(fieldValue)) {
                try {
                    String dateTime = DateUtil.formatDateTime(new Date(fieldValue));
                    variable.setFieldValue(dateTime);
                } catch (Exception e) {
                    continue;
                }
            }
        }


    }
}
