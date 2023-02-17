package com.hexing.asset.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.*;
import com.hexing.asset.domain.dto.SapAssetTransferDTO;
import com.hexing.asset.domain.vo.AssetFixVO;
import com.hexing.asset.domain.vo.AssetProcessParam;
import com.hexing.asset.domain.vo.AssetProcessReturn;
import com.hexing.asset.domain.vo.AssetReceiveVO;
import com.hexing.asset.enums.AssetProcessType;
import com.hexing.asset.enums.AssetStatus;
import com.hexing.asset.mapper.AssetProcessMapper;
import com.hexing.asset.service.*;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.PageUtil;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.bean.BeanTool;
import com.hexing.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 资产表Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
@Slf4j
public class AssetProcessServiceImpl extends ServiceImpl<AssetProcessMapper, AssetProcess> implements IAssetProcessService {

    @Autowired
    private IAssetService assetService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private IAssetProcessService processService;
    @Autowired
    private IAssetProcessVariableService variableService;
    @Autowired
    private IAssetProcessFieldService fieldService;
    @Autowired
    private ICommonService commonService;

    private int updateAssetAndCreateLog(Asset entity, AssetProcessParam processParam, String type) {
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userName = SecurityUtils.getLoginUser().getUser().getNickName();
//        String userCode = "80010712";
//        String userName = "PFC";
        Date nowDate = DateUtils.getNowDate();
        //总流程
        AssetProcess process = new AssetProcess();
        process.setProcessType(type);
        process.setAssetCode(entity.getAssetCode());
        process.setCreateBy(userCode);
        process.setCreateTime(nowDate);
        processService.save(process);
        processParam.setId(process.getId());
        //推送sap
        if (type.equals(AssetProcessType.PROCESS_FIXED.getCode())) {
            AssetFixVO vo = new AssetFixVO();
            vo.setAssetCode(entity.getAssetCode());
            vo.setCategory(processParam.getAssetType());
            vo.setResponsibilityCostCenterCode(processParam.getAssetType());
            vo.setDEPS(entity.getResponsiblePersonDept());
            vo.setExpirationDate(processParam.getMaturityTime());
            vo.setResponsibilityCostCenterCode(processParam.getDutyCostCenter());
            vo.setBelong(processParam.getProject());
            vo.setProvider(processParam.getSupplierName());
            vo.setUsage(processParam.getComment());
            try {
                JSONObject jsonObject = assetService.fixAsset(vo);
                JSONObject dataJO = jsonObject.getJSONObject("DATA");
                String sapCode = dataJO.getString("ANLN1");
                String costCenter= dataJO.getString("KOSTL");
                String costCenterName = dataJO.getString("LTEXT");
                entity.setSapCode(sapCode);
                entity.setCostCenter(costCenter);
                entity.setCostCenterName(costCenterName);
            } catch (Exception e) {
                throw new ServiceException("资产转固推送sap异常: "+e.getMessage());
            }
        } else if (type.equals(AssetProcessType.PROCESS_RECEIVE.getCode())&&entity.getFixed().equals(AssetStatus.FIXED.getCode())) {
            AssetReceiveVO vo = new AssetReceiveVO();
            vo.setRname(processParam.getResponsiblePersonName() + "-" + processParam.getResponsiblePersonCode());
            vo.setPost(processParam.getResponsiblePersonJob());
            vo.setStage(processParam.getCurrentLocation());
            vo.setAnln1(entity.getSapCode());
            vo.setZnum(processParam.getAssetType());
            vo.setBUKRS(entity.getCompany());
            vo.setBUKRS(processParam.getResponsiblePersonDept());
            try {
                JSONObject jsonObject = assetService.receiveAsset(vo);
                JSONObject dataJO = jsonObject.getJSONObject("DATA");
                String costCenter= dataJO.getString("KOSTL");
                String costCenterName = dataJO.getString("LTEXT");
                entity.setCostCenter(costCenter);
                entity.setCostCenterName(costCenterName);
            } catch (Exception e) {
                throw new ServiceException("资产派发推送sap异常: "+e.getMessage());
            }
        }else if (type.equals(AssetProcessType.PROCESS_UNUSED.getCode())){
            AssetReceiveVO vo = new AssetReceiveVO();
            vo.setBUKRS(entity.getCompany());
            vo.setAnln1(entity.getSapCode());
            vo.setORD41(AssetStatus.UNUSED.getName());
            try {
                assetService.receiveAsset(vo);
            } catch (Exception e) {
                throw new ServiceException("资产闲置推送sap异常: "+e.getMessage());
            }
        } else if (type.equals(AssetProcessType.PROCESS_ACCOUNT_TRANSFORM.getCode())) {
            SapAssetTransferDTO vo = new SapAssetTransferDTO();
            vo.setBUKRS(processParam.getCompany());
            vo.setZBUKRS(entity.getCompany());
            vo.setRname(processParam.getResponsiblePersonName() + "-" + processParam.getResponsiblePersonCode());
            vo.setPost(processParam.getResponsiblePersonJob());
            vo.setStage(processParam.getCurrentLocation());
            vo.setAnln1(entity.getSapCode());
            vo.setDEPS(processParam.getResponsiblePersonDept());
            try {
                JSONObject jsonObject = assetService.transferAsset(vo);
                JSONObject dataJO = jsonObject.getJSONObject("DATA");
                String costCenter= dataJO.getString("KOSTL");
                String costCenterName = dataJO.getString("LTEXT");
                entity.setCostCenter(costCenter);
                entity.setCostCenterName(costCenterName);
            } catch (Exception e) {
                throw new ServiceException("资产账务转移推送sap异常: "+e.getMessage());
            }
        }
        SysUser user = sysUserService.getUserByUserName(processParam.getResponsiblePersonCode());
        if (ObjectUtil.isNotEmpty(user)) {
            processParam.setResponsiblePersonName(user.getNickName());
        }
        //工单号
        String wokeCode = processParam.getWokeCode();
        processParam.setCostCenter(entity.getCostCenter());
        processParam.setCostCenterName(entity.getCostCenterName());
        //流程子表
        processService.saveProcess(processParam, type);
        int i =1;
        if (!process.getProcessType().equals(AssetProcessType.PROCESS_TRANSFORM.getCode())){
            i = assetService.updateAsset(entity, process);
        }
        return i;
    }

    //资产操作-派发
    @Override
    @Transactional
    public int receiveAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        if (!entity.getAssetStatus().equals(AssetStatus.IN_STORE.getCode()) &&
                !entity.getAssetStatus().equals(AssetStatus.UNUSED.getCode())) {
            throw new ServiceException("非在库和闲置资产无权操作");
        }
        String responsiblePersonCode = assetProcess.getResponsiblePersonCode();
        if (StringUtils.isBlank(responsiblePersonCode)) {
            throw new ServiceException("请选择领用人");
        }
        if (StringUtils.isBlank(assetProcess.getResponsiblePersonDept())) {
            throw new ServiceException("请选择领用部门");
        }
        if (StringUtils.isBlank(assetProcess.getResponsiblePersonJob())) {
            throw new ServiceException("请输入领用人岗位");
        }
        if (StringUtils.isBlank(assetProcess.getCurrentLocation())) {
            throw new ServiceException("请输入所在位置");
        }
        List<SysUser> sysUsers = sysUserService.selectUserList(new SysUser());
        SysUser sysUser = sysUsers.stream().filter(x -> x.getUserName().equals(responsiblePersonCode)).findFirst().orElse(new SysUser());
        entity.setResponsiblePersonCode(responsiblePersonCode);
        entity.setResponsiblePersonDept(assetProcess.getResponsiblePersonDept());
        entity.setCostCenter(assetProcess.getResponsiblePersonDept());
        entity.setResponsiblePersonName(sysUser.getNickName());
        assetProcess.setResponsiblePersonName(sysUser.getNickName());
        entity.setCurrentLocation(assetProcess.getCurrentLocation());
        if (StringUtils.isNotBlank(entity.getFixed()) && AssetStatus.FIXED.getCode().equals(entity.getFixed())) {
            entity.setAssetStatus(AssetStatus.USING.getCode());
        } else {
            entity.setAssetStatus(AssetStatus.TRIAL.getCode());
            if (StringUtils.isBlank(assetProcess.getAssetName())) {
                throw new ServiceException("请输入资产名称");
            }
            if (StringUtils.isBlank(assetProcess.getStandard())) {
                throw new ServiceException("请输入规格型号");
            }
            if (StringUtils.isBlank(assetProcess.getFactoryNo())) {
                throw new ServiceException("请输入资产出厂编号");
            }
            if (ObjectUtil.isEmpty(assetProcess.getFixedAcceptanceDate())) {
                throw new ServiceException("请输入转固验收日期");
            }
            entity.setAssetName(assetProcess.getAssetName());
            entity.setStandard(assetProcess.getStandard());
            entity.setFactoryNo(assetProcess.getFactoryNo());
        }
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.PROCESS_RECEIVE.getCode());
    }

    //资产操作-转移
    @Override
    @Transactional
    public int transferAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        if (!entity.getAssetStatus().equals(AssetStatus.UNUSED.getCode())) {
            throw new ServiceException("非闲置资产无权操作");
        }
        if (StringUtils.isBlank(assetProcess.getCompany())) {
            throw new ServiceException("请选择接收公司");
        }
        String responsiblePersonCode = assetProcess.getResponsiblePersonCode();
        if (StringUtils.isBlank(responsiblePersonCode)) {
            throw new ServiceException("请选择接收人");
        }
        if (StringUtils.isBlank(assetProcess.getResponsiblePersonDept())) {
            throw new ServiceException("请选择接收人部门");
        }
        if (StringUtils.isBlank(assetProcess.getCurrentLocation())) {
            throw new ServiceException("请输入所在位置");
        }
        entity.setCompany(assetProcess.getCompany());
        entity.setResponsiblePersonCode(responsiblePersonCode);
        entity.setCurrentLocation(assetProcess.getCurrentLocation());
        if (StringUtils.isNotBlank(assetProcess.getPurchaseOrderNo())) {
            entity.setPurchaseOrderNo(assetProcess.getPurchaseOrderNo());
        }
        entity.setAssetStatus(AssetStatus.IN_STORE.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.PROCESS_TRANSFORM.getCode());
    }

    //资产操作-已退货
    @Override
    @Transactional
    public int returnAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        /*
 资产操作【退货】后，弹框确认是否退货，确认后【资产状态】变更为“已退货”页面样式参考图1-3
         * */
        if (!entity.getAssetStatus().equals(AssetStatus.MAINTAIN.getCode())
        ) {
            throw new ServiceException("非维修资产无权操作");
        }
        entity.setAssetStatus(AssetStatus.RETURNED.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.RETURNED.getCode());
    }

    //转固
    @Override
    @Transactional
    public int fixationAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        if (!entity.getAssetStatus().equals(AssetStatus.TRIAL.getCode())) {
            throw new ServiceException("非试用资产无权操作");
        }
        if (StringUtils.isBlank(assetProcess.getAssetType())) {
            throw new ServiceException("请选择资产类型");
        }
        if (ObjectUtil.isEmpty(assetProcess.getMaturityTime())) {
            throw new ServiceException("请设置保质期到期时间");
        }
        entity.setAssetCategory(assetProcess.getAssetType());
        entity.setAssetStatus(AssetStatus.USING.getCode());
        entity.setFixed(AssetStatus.FIXED.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.PROCESS_FIXED.getCode());
    }

    //维修
    @Override
    @Transactional
    public int maintainAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        /*
   资产操作【维修】后，弹框确认后，资产状态变更为【维修】；
         * */
        if (!entity.getAssetStatus().equals(AssetStatus.TRIAL.getCode()) &&
                !entity.getAssetStatus().equals(AssetStatus.USING.getCode())) {
            throw new ServiceException("非试用和在用资产无权操作");
        }
        entity.setAssetStatus(AssetStatus.MAINTAIN.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.PROCESS_MAINTAIN.getCode());
    }

    //闲置
    @Override
    @Transactional
    public int unusedAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        /*
   资产操作【闲置】后
     a.弹框提示“是否清空资产保管人和保管部门？”
         是：资产状态变更为【闲置】，清空字段【资产保管人】，【资产保管部门】，【成本中心】；
         否：资产状态变更为【闲置】
         * */
        if (!entity.getAssetStatus().equals(AssetStatus.USING.getCode())) {
            throw new ServiceException("非在用资产无权操作");
        }
        entity.setAssetStatus(AssetStatus.UNUSED.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);
        if ("1".equals(assetProcess.getClearInfo())) {
            assetProcess.setResponsiblePersonCode("");
            assetProcess.setResponsiblePersonName("");
            assetProcess.setResponsiblePersonDept("");
            assetProcess.setCostCenter("");
            assetProcess.setCostCenterName("");
            entity.setResponsiblePersonCode("");
            entity.setResponsiblePersonName("");
            entity.setResponsiblePersonDept("");
            entity.setCostCenter("");
            entity.setCostCenterName("");
        } else {
            assetProcess.setResponsiblePersonCode(entity.getResponsiblePersonCode());
            assetProcess.setResponsiblePersonName(entity.getResponsiblePersonName());
            assetProcess.setResponsiblePersonDept(entity.getResponsiblePersonDept());
            assetProcess.setCostCenter(entity.getCostCenter());
            assetProcess.setCostCenterName(entity.getCostCenterName());
        }

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.PROCESS_UNUSED.getCode());
    }

    //报废
    @Override
    @Transactional
    public int scrapAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        /*
   资产操作【报废】后，弹框确认后，资产状态变更为【待报废】页面样式参考图1-3
         * */
        if (!entity.getAssetStatus().equals(AssetStatus.USING.getCode())) {
            throw new ServiceException("非在用资产无权操作");
        }
        entity.setAssetStatus(AssetStatus.WAITING_SCRAP.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.SCRAP.getCode());
    }

    //外卖
    @Override
    @Transactional
    public int waiteTakeOutAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        /*
 资产操作【外卖】后，弹框确认是否外卖，确认后，资产状态变更为【待外卖】页面样式参考图1-3
         * */
        if (!entity.getAssetStatus().equals(AssetStatus.USING.getCode())) {
            throw new ServiceException("非在用资产无权操作");
        }
        entity.setAssetStatus(AssetStatus.WAITING_TAKE_OUT.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.WAITING_TAKE_OUT.getCode());
    }

    //盘亏
    @Override
    @Transactional
    public int inventoryLossAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        if (!entity.getAssetStatus().equals(AssetStatus.IN_STORE.getCode()) &&
                !entity.getAssetStatus().equals(AssetStatus.TRIAL.getCode()) &&
                !entity.getAssetStatus().equals(AssetStatus.USING.getCode()) &&
                !entity.getAssetStatus().equals(AssetStatus.UNUSED.getCode()) &&
                !entity.getAssetStatus().equals(AssetStatus.WAITING_TAKE_OUT.getCode()) &&
                !entity.getAssetStatus().equals(AssetStatus.WAITING_SCRAP.getCode())
        ) {
            throw new ServiceException("非在库、试用、在用、闲置、待外卖、待报废资产无权操作");
        }
        entity.setAssetStatus(AssetStatus.INVENTORY_LOSE.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.PROCESS_INVENTORY_LOSE.getCode());
    }

    //已维修
    @Override
    @Transactional
    public int maintainedAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        /*
  【资产状态】为返修或维修都支持操作【已维修】，资产操作【已维修】后弹框展示资产保管人，资产保管部门不支持更改，有值则显示没值则为空，
  支持选择【资产状态】”在库“、“在用”、“试用”：
a.“在用”，资产状态变更为【在用】。 表示转固的资产已经维修完成且在使用；
b."试用"，，资产状态变更为【试用】。 表示未转固的资产已经维修完成且在试用；
c."在库"，清空该条资产“资产保管人，资产保管部门，成本中心”，资产状态变更为【在库】。表示资产已经维修完成且资产保管人不再使用该资产；
页面样式参考图1-4；
         * */
        if (!entity.getAssetStatus().equals(AssetStatus.MAINTAIN.getCode())) {
            throw new ServiceException("非维修资产无权操作");
        }
        if (assetProcess.getAssetStatus().equals(AssetStatus.USING.getCode())) {
            if (AssetStatus.UNFIXED.getCode().equals(entity.getFixed())) {
                throw new ServiceException("该资产未转固");
            }
            entity.setAssetStatus(AssetStatus.USING.getCode());
        }
        if (assetProcess.getAssetStatus().equals(AssetStatus.TRIAL.getCode())) {
            if (AssetStatus.FIXED.getCode().equals(entity.getFixed())) {
                throw new ServiceException("该资产已转固");
            }
            entity.setAssetStatus(AssetStatus.TRIAL.getCode());
        }
        if (assetProcess.getAssetStatus().equals(AssetStatus.IN_STORE.getCode())) {
            entity.setResponsiblePersonCode("");
            entity.setResponsiblePersonName("");
            entity.setCostCenter("");
            entity.setCostCenterName("");
            entity.setAssetStatus(AssetStatus.IN_STORE.getCode());
        }
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);
        assetProcess.setResponsiblePersonCode(entity.getResponsiblePersonCode());
        assetProcess.setResponsiblePersonName(entity.getResponsiblePersonName());
        assetProcess.setResponsiblePersonDept(entity.getResponsiblePersonDept());
        assetProcess.setCostCenter(entity.getCostCenter());
        assetProcess.setAssetStatus(entity.getAssetStatus());

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.PROCESS_MAINTAINED.getCode());
    }

    //已外卖
    @Override
    @Transactional
    public int takeOutAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        /*
 资产操作【已外卖】后，弹框确认是否已外卖，确认后，资产状态变更为【已外卖】页面样式参考图1-5
         * */
        if (!entity.getAssetStatus().equals(AssetStatus.WAITING_TAKE_OUT.getCode())) {
            throw new ServiceException("非待外卖资产无权操作");
        }
        entity.setAssetStatus(AssetStatus.TOKE_OUT.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.TOKE_OUT.getCode());
    }

    //已报废
    @Override
    @Transactional
    public int scrapedAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        /*
 资产操作【已报废】后，弹框确认是否已报废，确认后，资产状态变更为【已报废】页面样式参考图1-5
         * */
        if (!entity.getAssetStatus().equals(AssetStatus.WAITING_SCRAP.getCode())) {
            throw new ServiceException("非待报废资产无权操作");
        }
        entity.setAssetStatus(AssetStatus.SCRAPED.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.SCRAPED.getCode());
    }

    //资产操作-账务转移
    @Override
    @Transactional
    public int accountTransferAsset(AssetProcessParam assetProcess) {
        Asset entity = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetProcess.getAssetCode()));
        if (StringUtils.isBlank(assetProcess.getCompany())) {
            throw new ServiceException("请选择接收公司");
        }
        String responsiblePersonCode =assetProcess.getResponsiblePersonCode();
        if (StringUtils.isBlank(responsiblePersonCode)) {
            throw new ServiceException("请选择接收人");
        }
        if (StringUtils.isBlank(assetProcess.getResponsiblePersonDept())) {
            throw new ServiceException("请选择接收人部门");
        }
        if (StringUtils.isBlank(assetProcess.getCurrentLocation())) {
            throw new ServiceException("请输入所在位置");
        }
        entity.setCompany(assetProcess.getCompany());
        entity.setResponsiblePersonCode(responsiblePersonCode);
        entity.setCurrentLocation(assetProcess.getCurrentLocation());
        if (StringUtils.isNotBlank(assetProcess.getPurchaseOrderNo())) {
            entity.setPurchaseOrderNo(assetProcess.getPurchaseOrderNo());
        }
        entity.setAssetStatus(AssetStatus.IN_STORE.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return updateAssetAndCreateLog(entity, assetProcess, AssetProcessType.PROCESS_ACCOUNT_TRANSFORM.getCode());
    }

    //已报废
    @Override
    @Transactional
    public AssetProcessReturn getTransferInfo(String assetProcess) {
        AssetProcessReturn domain = new AssetProcessReturn();
        AssetProcess process=new AssetProcess();
        process.setAssetCode(assetProcess);
        List<AssetProcess> list = processService.list(process);
        for (AssetProcess process1 : list) {
            if (process1.getProcessType().equals(AssetProcessType.PROCESS_TRANSFORM.getCode())){
                domain = processService.convertProcess(process1, new AssetProcessReturn());
                break;
            }
        }
     return domain;

    }
    /**
     * 查询资产流程
     */
    @Override
    public AssetProcess getOne(AssetProcess process) {
        List<AssetProcess> processList = this.list(process);
        if (CollectionUtil.isNotEmpty(processList)) {
            return processList.get(0);
        }
        return new AssetProcess();
    }

    /**
     * 根据流程id更新值表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByProcessId(AssetProcess process) {

        List<AssetProcessVariable> varList = variableService.selectVariableListByProcessId(process.getId());

        for (AssetProcessVariable var : varList) {
            Object fieldValue = BeanTool.getFieldValue(process, var.getFieldKey());
            if (ObjectUtil.isNotEmpty(fieldValue)) {
                var.setFieldValue(String.valueOf(fieldValue));
            } else {
                var.setFieldValue(null);
            }
        }
        variableService.updateBatchById(varList);

        // 对于新添加的流程字段
        Field[] fields = process.getClass().getDeclaredFields();
        List<Field> newFieldList = Arrays.stream(fields).filter(new Predicate<Field>() {
            @Override
            public boolean test(Field field) {
                return varList.stream().noneMatch(x -> x.getFieldKey().equals(field.getName()));
            }
        }).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(newFieldList)) {
            List<AssetProcessField> processFieldList = fieldService.list(new LambdaQueryWrapper<AssetProcessField>()
                    .eq(AssetProcessField::getProcessType, process.getProcessType())
                    .in(AssetProcessField::getFieldKey, newFieldList.stream().map(Field::getName)));

            Map<String, Long> fieldKeyIdMap = processFieldList
                    .stream().collect(Collectors.toMap(AssetProcessField::getFieldKey, AssetProcessField::getId));

            List<AssetProcessVariable> newVarList = new ArrayList<>();
            for (Field field : newFieldList) {
                AssetProcessVariable newVar = new AssetProcessVariable();
                newVar.setProcessId(process.getId());
                newVar.setFieldId(fieldKeyIdMap.get(field.getName()));
                Object fieldValue = BeanTool.getFieldValue(process, field.getName());
                if (ObjectUtil.isNotEmpty(fieldValue)) {
                    newVar.setFieldValue(String.valueOf(fieldValue));
                } else {
                    newVar.setFieldValue(null);
                }
            }
            variableService.saveBatch(newVarList);
        }

    }

    /**
     * 查询资产流程列表
     */
    @Override
    public List<AssetProcess> listByPage(AssetProcess process) {
        List<AssetProcessField> searchDomain = commonService.getSearchDomain(process);
        PageUtil.startPage();
        return commonService.searchAssetProcess(searchDomain, process);
    }

    @Override
    public List<AssetProcess> list(AssetProcess process) {
        List<AssetProcessField> searchDomain = commonService.getSearchDomain(process);
        return commonService.searchAssetProcess(searchDomain, process);
    }

    /**
     * process 转为 domain
     *
     * @param process
     * @param domain
     */
    @Override
    public <T> T convertProcess(AssetProcess process, T domain) {
        List<AssetProcessVariable> variableList = process.getVariableList();
        if (CollectionUtil.isNotEmpty(variableList)) {
            for (AssetProcessVariable variable : variableList) {
                BeanTool.setFieldValueThrowEx(domain, variable.getFieldKey(), variable.getFieldValue());
            }
        }
        process.setVariableList(null);
        BeanUtil.copyProperties(process, domain);
        return domain;
    }

    @Override
    public void saveOne(AssetProcess process) {
        List<AssetProcessField> processFields = commonService.getProcessFields();
        if (StringUtils.isEmpty(process.getProcessType())) {
            return;
        }
        List<AssetProcessField> fieldList = processFields.stream()
                .filter(assetProcessField -> assetProcessField.getProcessType().equals(process.getProcessType()))
                .collect(Collectors.toList());

        List<AssetProcessVariable> varList = new ArrayList<>();
        for (AssetProcessField field : fieldList) {
            AssetProcessVariable var = new AssetProcessVariable();
            var.setProcessId(process.getId())
                    .setFieldId(field.getId());
            Object fieldValue = BeanTool.getFieldValue(process, field.getFieldKey());
            if (ObjectUtil.isNotEmpty(fieldValue)) {
                var.setFieldValue(String.valueOf(fieldValue));
            } else {
                var.setFieldValue(null);
            }
            varList.add(var);
        }
        variableService.saveBatch(varList);
    }

    @Override
    public void saveBatchProcess(List<? extends AssetProcess> processList) {
        List<AssetProcessField> processFields = commonService.getProcessFields();
        List<AssetProcessField> fieldList = processFields.stream()
                .filter(assetProcessField -> assetProcessField.getProcessType().equals(AssetProcessType.COUNTING_PROCESS.getCode()))
                .collect(Collectors.toList());

        List<AssetProcessVariable> varList = new ArrayList<>();
        for (AssetProcess process : processList) {
            // 字段值存入流程值表
            for (AssetProcessField field : fieldList) {
                AssetProcessVariable var = new AssetProcessVariable();
                var.setProcessId(process.getId())
                        .setFieldId(field.getId());
                Object fieldValue = BeanTool.getFieldValue(process, field.getFieldKey());
                if (ObjectUtil.isNotEmpty(fieldValue)) {
                    var.setFieldValue(String.valueOf(fieldValue));
                } else {
                    var.setFieldValue(null);
                }
                varList.add(var);
            }
        }
        variableService.saveBatch(varList);
    }

    @Override
    public void saveProcess(AssetProcessParam process, String type) {
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userName = SecurityUtils.getLoginUser().getUser().getNickName();
//        String userCode = "80010712";
//        String userName = "PFC";
        process.setCreateTime(DateUtils.getNowDate());
        process.setCreateBy(userCode);
        List<AssetProcessField> processFields = commonService.getProcessFields();
        List<AssetProcessField> fieldList = processFields.stream()
                .filter(assetProcessField -> assetProcessField.getProcessType().equals(type))
                .collect(Collectors.toList());

        List<AssetProcessVariable> varList = new ArrayList<>();
        // 字段值存入流程值表
        for (AssetProcessField field : fieldList) {
            AssetProcessVariable var = new AssetProcessVariable();
            var.setProcessId(process.getId())
                    .setFieldId(field.getId());
            Object fieldValue = BeanTool.getFieldValue(process, field.getFieldKey());
            if (ObjectUtil.isNotEmpty(fieldValue)) {
                var.setFieldValue(String.valueOf(fieldValue));
            } else {
                var.setFieldValue(null);
            }
            varList.add(var);
        }
        variableService.saveBatch(varList);
    }
}
