package com.hexing.asset.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.enums.AssetStatus;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.asset.service.IAssetService;
import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.common.constant.HttpStatus;
import com.hexing.common.core.domain.Result;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.impl.SysDeptServiceImpl;
import com.hexing.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public int backAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));

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
    public int fixationAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));

        /*
        * 资产操作【转固】后，弹框填写转固信息，确认后【资产状态】变更为“在用”、【转固状态】变更为“已转固”，
        * 推送资产数据到SAP系统自动生成资产卡片（资产卡片的固定资产编码取值资产平台自动生成的【资产编码】）。
        * 生成资产卡片后SAP系统的【资产编号】（SAP系统自动生成的编码）反写到资产平台系统【SAP资产编号】里取消后关闭弹框。
        * 页面样式参考图1-6弹框填写内容说明：1、【SAP卡片资产分类】单选，下拉框数据：
        *   固定资产-租赁资产（Z020）     固定资产-房屋建筑物（Z021）     固定资产-机器设备（Z022）     固定资产-动力及电气（Z023 ）
        *   固定资产-办公设备（Z024）     固定资产-运输设备（Z025）     固定资产-电子设备（Z026）     固定资产-电脑（Z027 ）
        *     固定资产-其他设备（Z029）
        * 固定资产-信息化设备除电脑外（Z030）
        * 2、【SAP卡片资产分类】【成本中心编码】为必填项，【责任成本中心编码】【归属项目】【制造商名称】【用途】为非必填项
        * */
        if (StringUtils.isBlank(asset.getAssetCategory())){
            throw new ServiceException("请选择资产分类");
        }
        if (StringUtils.isBlank(asset.getCostCenter())){
            throw new ServiceException("请输入成本中心编码");
        }
        entity.setCostCenter(asset.getCostCenter());
        entity.setAssetCategory(asset.getAssetCategory());
        entity.setAssetStatus(AssetStatus.USING.getCode());
        entity.setFixed(Boolean.TRUE);
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int transferAsset(Asset asset,String recipient) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));

        /*
   资产操作【资产转移】后，弹框填写转移信息（转移公司，接收人），确认后更新【转移公司】【接收人】，【转出公司】取值资产【所属公司】，
   资产状态变更为【在库】。资产数据推送到SAP系统自动创建资产卡片信息，资产卡片中的【固定资产编号】取值资产平台自动生成的【资产编码】，
   资产卡片创建成功后将SAP系统自动生成的【资产编码】传到资产平台【SAP资产编码】。原资产卡片状态变更为【外卖】
注意：1.一个资产可能会进行多次资产转移，开发创建资产转移子表，记录每次转移的数据，包括自动【转移公司】【转出公司】【接收人】
      2.资产工单tab支持看到资产转移记录。【备注】值为：”${转出公司}转移资产到${转入公司}“
说明：资产转移后SAP会将转移的资产的资产卡片状态改为【外卖】，再新建一个转入公司的资产卡片。但是在资产平台里同属于一个资产，
所以此场景只需要更改【归属公司】记录转移数据，再更改下与资产卡片的关联关系即可。能够区分两个资产卡片不同的字段是【SAP资产编码】
（SAP系统自动生成的编码）。因此SAP不允许创建资产卡片，资产平台转移后自动生成资产卡片并将资产卡片的【资产编码】反写到资产平台才能有关联关系。
（说明：1.SAP中存在多个资产卡片同一个资产编码，比如公司之间资产转移场景会有两个资产卡片；2.SAP老数据【固定资产编码】存在重复的）
页面样式参考图1-2
         * */
        if (StringUtils.isBlank(asset.getCompany())){
            throw new ServiceException("请选择转移公司");
        }
        if (StringUtils.isBlank(recipient)){
            throw new ServiceException("请选择接收人");
        }
        entity.setCompany(asset.getCompany());
        entity.setResponsiblePersonCode(recipient);
        //接收人
        entity.setAssetStatus(AssetStatus.IN_STORE.getCode());
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int waiteTakeOutAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));
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
    public int takeOutAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));
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
    public int repairAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));
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
    public int receiveAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));
        /*
资产操作【领用】后，弹框填写领用信息（领用人，领用部门，领用人岗位，所在位置，资产出厂编码，领用说明），如果该条资产在数据库中有“出厂编码”，则弹框默认带出，
若没有则为空；确认后判断资产【转固状态】：
①【转固状态】=是，【资产状态】变更为“在用”，更新资产领用数据（更新弹框数据到数据库中对应字段：【资产保管人】=【领用人】，
【资产保管部门】=【领用部门】，【成本中心】=【资产保管部门】，【所在位置】=【所在位置】，【出厂编码】=【资产出厂编码】）。资产数据推送到SAP系统更新资产卡片信息；
②【转固状态】=否，【资产状态】变更为“试用”，更新资产领用数据（同上）
    取消后关闭弹框。页面样式参考图1-1
         * */
        if (StringUtils.isBlank(asset.getResponsiblePersonCode())){
            throw new ServiceException("请选择领用人");
        }
        if (StringUtils.isBlank(asset.getResponsiblePersonDept())){
            throw new ServiceException("请选择领用部门");
        }
        if (StringUtils.isBlank(asset.getCurrentLocation())){
            throw new ServiceException("请输入所在位置");
        }
        if (StringUtils.isBlank(asset.getFactoryNo())){
            throw new ServiceException("请输入资产出厂编码");
        }
        if (entity.getFixed()){
            entity.setAssetStatus(AssetStatus.USING.getCode());
        }else {
            entity.setAssetStatus(AssetStatus.TRIAL.getCode());
        }
        entity.setUpdateTime(DateUtils.getNowDate());
//        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        String userCode = "80010712";
        entity.setUpdateBy(userCode);

        return assetMapper.updateById(entity);
    }

    @Override
    public int returnAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));
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
    public int maintainAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));
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
    public int maintainedAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));
        /*
  【资产状态】为返修或维修都支持操作【已维修】，资产操作【已维修】后弹框展示资产保管人，资产保管部门不支持更改，有值则显示没值则为空，
  支持选择【资产状态】”在库“、“在用”、“试用”：
a.“在用”，资产状态变更为【在用】。 表示转固的资产已经维修完成且在使用；
b."试用"，，资产状态变更为【试用】。 表示未转固的资产已经维修完成且在试用；
c."在库"，清空该条资产“资产保管人，资产保管部门，成本中心”，资产状态变更为【在库】。表示资产已经维修完成且资产保管人不再使用该资产；
页面样式参考图1-4；
         * */
        if (asset.getAssetStatus().equals(AssetStatus.USING.getCode())){
            if (!asset.getFixed()){
                throw new ServiceException("该资产未转固");
            }
            entity.setAssetStatus(AssetStatus.USING.getCode());
        }
        if (asset.getAssetStatus().equals(AssetStatus.TRIAL.getCode())){
            if (asset.getFixed()){
                throw new ServiceException("该资产已转固");
            }
            entity.setAssetStatus(AssetStatus.TRIAL.getCode());
        }
        if (asset.getAssetStatus().equals(AssetStatus.IN_STORE.getCode())){
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
    public int scrapAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));
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
    public int scrapedAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));
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
