package com.hexing.asset.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.enums.AssetStatus;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.impl.SysDeptServiceImpl;
import com.hexing.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资产表Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
@Slf4j
public class AssetProcessServiceImpl extends ServiceImpl<AssetMapper, Asset> implements IAssetProcessService {

    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysDeptServiceImpl sysDeptService;

    @Override
    public int backAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));

        // 资产操作【归还】后，资产状态变更为【在库】，清空字段【资产保管人】，【资产保管部门】，【成本中心】；
        entity.setAssetStatus(AssetStatus.IN_STORE.getCode());
        entity.setResponsiblePersonDept("");
        entity.setResponsiblePersonName("");
        entity.setResponsiblePersonDept("");
        entity.setCostCenter("");
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int fixationAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));
        if (StringUtils.isBlank(assetProcess.getAssetCategory())){
            throw new ServiceException("请选择资产分类");
        }
        if (StringUtils.isBlank(assetProcess.getCostCenter())){
            throw new ServiceException("请输入成本中心编码");
        }
        entity.setCostCenter(assetProcess.getCostCenter());
        entity.setAssetCategory(assetProcess.getAssetCategory());
        entity.setAssetStatus(AssetStatus.USING.getCode());
        entity.setFixed("1");
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int transferAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));
        if (StringUtils.isBlank(assetProcess.getCompany())){
            throw new ServiceException("请选择接收公司");
        }
        if (StringUtils.isBlank(assetProcess.getResponsiblePersonCode())){
            throw new ServiceException("请选择接收人");
        }
        if (StringUtils.isBlank(assetProcess.getResponsiblePersonDept())){
            throw new ServiceException("请选择接收人部门");
        }
        if (StringUtils.isBlank(assetProcess.getCostCenter())){
            throw new ServiceException("请输入成本中心");
        }
        if (StringUtils.isBlank(assetProcess.getCurrentLocation())){
            throw new ServiceException("请输入所在位置");
        }
        entity.setCompany(assetProcess.getCompany());
        entity.setResponsiblePersonCode(assetProcess.getResponsiblePersonCode());
        entity.setResponsiblePersonDept(assetProcess.getResponsiblePersonDept());
        entity.setCostCenter(assetProcess.getCostCenter());
        entity.setCurrentLocation(assetProcess.getCurrentLocation());
        if (StringUtils.isNotBlank(assetProcess.getPurchaseOrderNo())){
            entity.setPurchaseOrderNo(assetProcess.getPurchaseOrderNo());
        }
        entity.setAssetStatus(AssetStatus.IN_STORE.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int waiteTakeOutAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));
        /*
 资产操作【外卖】后，弹框确认是否外卖，确认后，资产状态变更为【待外卖】页面样式参考图1-3
         * */
        entity.setAssetStatus(AssetStatus.WAITING_TAKE_OUT.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int takeOutAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));
        /*
 资产操作【已外卖】后，弹框确认是否已外卖，确认后，资产状态变更为【已外卖】页面样式参考图1-5
         * */
        entity.setAssetStatus(AssetStatus.TOKE_OUT.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int repairAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));
        /*
  资产操作【返修】后，弹框确认是否返修，确认后【资产状态】变更为”返修“页面样式参考图1-3
         * */
        entity.setAssetStatus(AssetStatus.REPAIR.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int receiveAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));
        String responsiblePersonCode = assetProcess.getResponsiblePersonCode();
        if (StringUtils.isBlank(responsiblePersonCode)){
            throw new ServiceException("请选择领用人");
        }
        if (StringUtils.isBlank(assetProcess.getResponsiblePersonDept())){
            throw new ServiceException("请选择领用部门");
        }
        //岗位
        List<SysUser> sysUsers = sysUserService.selectUserList(new SysUser());
        SysUser sysUser = sysUsers.stream().filter(x -> x.getUserName().equals(responsiblePersonCode)).findFirst().orElse(new SysUser());

        if (StringUtils.isBlank(assetProcess.getCurrentLocation())){
            throw new ServiceException("请输入所在位置");
        }
        if (StringUtils.isNotBlank(entity.getFixed())&&"1".equals(entity.getFixed())){
            entity.setAssetStatus(AssetStatus.USING.getCode());
        }else {
            entity.setAssetStatus(AssetStatus.TRIAL.getCode());
            if (StringUtils.isBlank(assetProcess.getAssetName())){
                throw new ServiceException("请输入资产名称");
            }
            if (StringUtils.isBlank(assetProcess.getStandard())){
                throw new ServiceException("请输入规格型号");
            }
            if (ObjectUtil.isEmpty(assetProcess.getFixedAcceptanceDate())){
                throw new ServiceException("请输入转固验收日期");
            }
        }
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int returnAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));
        /*
 资产操作【退货】后，弹框确认是否退货，确认后【资产状态】变更为“已退货”页面样式参考图1-3
         * */
        entity.setAssetStatus(AssetStatus.RETURNED.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int maintainAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));
        /*
   资产操作【维修】后，弹框确认后，资产状态变更为【维修】；
         * */
        entity.setAssetStatus(AssetStatus.MAINTAIN.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int maintainedAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));
        /*
  【资产状态】为返修或维修都支持操作【已维修】，资产操作【已维修】后弹框展示资产保管人，资产保管部门不支持更改，有值则显示没值则为空，
  支持选择【资产状态】”在库“、“在用”、“试用”：
a.“在用”，资产状态变更为【在用】。 表示转固的资产已经维修完成且在使用；
b."试用"，，资产状态变更为【试用】。 表示未转固的资产已经维修完成且在试用；
c."在库"，清空该条资产“资产保管人，资产保管部门，成本中心”，资产状态变更为【在库】。表示资产已经维修完成且资产保管人不再使用该资产；
页面样式参考图1-4；
         * */
        if (assetProcess.getAssetStatus().equals(AssetStatus.USING.getCode())){
            if (!assetProcess.getFixed()){
                throw new ServiceException("该资产未转固");
            }
            entity.setAssetStatus(AssetStatus.USING.getCode());
        }
        if (assetProcess.getAssetStatus().equals(AssetStatus.TRIAL.getCode())){
            if (assetProcess.getFixed()){
                throw new ServiceException("该资产已转固");
            }
            entity.setAssetStatus(AssetStatus.TRIAL.getCode());
        }
        if (assetProcess.getAssetStatus().equals(AssetStatus.IN_STORE.getCode())){
            entity.setResponsiblePersonCode("");
            entity.setResponsiblePersonName("");
            entity.setResponsiblePersonDept("");
            entity.setCostCenter("");
            entity.setAssetStatus(AssetStatus.IN_STORE.getCode());
        }
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int scrapAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));
        /*
   资产操作【报废】后，弹框确认后，资产状态变更为【待报废】页面样式参考图1-3
         * */
        entity.setAssetStatus(AssetStatus.WAITING_SCRAP.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int scrapedAsset(AssetProcess assetProcess) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, assetProcess.getAssetId()));
        /*
 资产操作【已报废】后，弹框确认是否已报废，确认后，资产状态变更为【已报废】页面样式参考图1-5
         * */
        entity.setAssetStatus(AssetStatus.SCRAPED.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }


}
