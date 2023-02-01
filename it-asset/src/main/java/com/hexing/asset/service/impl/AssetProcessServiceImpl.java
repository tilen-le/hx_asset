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

    /**
     * 修归还资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    @Override
    public int backAsset(Asset asset) {
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));

        // 资产操作【归还】后，资产状态变更为【在库】，清空字段【资产保管人】，【资产保管部门】，【成本中心】；
        entity.setAssetStatus(AssetStatus.INSTORE.getCode());
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

    /**
     * 转固资产
     *
     * @param asset 资产表
     * @return 结果
     */
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
        entity.setAssetStatus(AssetStatus.INSTORE.getCode());
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


}
