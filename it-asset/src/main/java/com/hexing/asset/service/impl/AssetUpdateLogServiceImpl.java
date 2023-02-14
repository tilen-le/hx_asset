package com.hexing.asset.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetUpdateLog;
import com.hexing.asset.domain.vo.AssetProcessParam;
import com.hexing.asset.domain.vo.AssetProcessReturn;
import com.hexing.asset.mapper.AssetUpdateLogMapper;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        LambdaQueryWrapper<AssetUpdateLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetUpdateLog::getAssetCode,log.getAssetCode());
        wrapper.orderByDesc(AssetUpdateLog::getCreateTime);
        AssetUpdateLog updateLog = logService.list(wrapper).stream().findFirst().orElse(null);
        if (ObjectUtil.isNotEmpty(updateLog)&&!updateLog.getResponsiblePersonCode().equals(log.getResponsiblePersonCode())){
            updateLog.setUpdateTime(DateUtils.getNowDate());
            updateLog.setUpdateBy(userCode);
            assetUpdateLogMapper.updateById(updateLog);
        }
        log.setCreateBy(userCode);
        log.setCreateTime(DateUtils.getNowDate());
        log.setUpdateTime(null);
        log.setUpdateBy("");

        return  assetUpdateLogMapper.insert(log);
    }

    //保管记录
    @Override
    public List<AssetUpdateLog> custodyLogList(AssetProcessParam assetProcess) {
        LambdaQueryWrapper<AssetUpdateLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetUpdateLog::getAssetCode,assetProcess.getAssetCode());
        wrapper.orderByDesc(AssetUpdateLog::getCreateTime);
        AssetUpdateLog updateLog = logService.list(wrapper).stream().findFirst().orElse(null);
        wrapper.isNotNull(AssetUpdateLog::getAssetCode);
        List<AssetUpdateLog> list = logService.list(wrapper);
        list.add(updateLog);
        return list;
    }

    //工单记录
    @Override
    public List<AssetProcessReturn> workLogList(AssetProcessParam assetProcess) {
        List<AssetProcessReturn> domains = new ArrayList<>();
        AssetProcess process=new AssetProcess();
        process.setAssetCode(assetProcess.getAssetCode());
        List<AssetProcess> list = processService.list(process);
        for (AssetProcess ap : list) {
            AssetProcessReturn domain = processService.convertProcess(ap, new AssetProcessReturn());
            domains.add(domain);
        }
        return domains;
    }

    //操作记录
    @Override
    public List<AssetUpdateLog> operationLogList(AssetProcessParam assetProcess) {
        LambdaQueryWrapper<AssetUpdateLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetUpdateLog::getAssetCode,assetProcess.getAssetCode());
        wrapper.orderByDesc(AssetUpdateLog::getCreateTime);
        return logService.list(wrapper);
    }

    //操作记录详情
    @Override
    public AssetUpdateLog getOperationLogById(Long id) {
        LambdaQueryWrapper<AssetUpdateLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetUpdateLog::getId,id);
        return assetUpdateLogMapper.selectOne(wrapper);
    }
}
