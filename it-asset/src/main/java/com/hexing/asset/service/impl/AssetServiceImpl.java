package com.hexing.asset.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.service.IAssetService;
import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.common.constant.HttpStatus;
import com.hexing.common.core.domain.Result;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.impl.SysDeptServiceImpl;
import com.hexing.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产表Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
@Slf4j
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements IAssetService {

    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysDeptServiceImpl sysDeptService;
//    @Autowired
    private IAssetUpdateLogService assetUpdateLogService;


    @Value("${uip.uipTransfer}")
    private String uipTransfer;

    /**
     * 根据平台资产编号查询资产
     *
     * @param assetCode
     * @return
     */
    @Override
    public Asset selectAssetByAssetCode(String assetCode) {
        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Asset::getAssetCode, assetCode);
        Asset asset = assetMapper.selectOne(wrapper);
        if (ObjectUtil.isEmpty(asset)) {
            return null;
        }
        Map<String, SysUser> usernameNicknameMap = sysUserService.getUsernameUserObjMap();
        Map<String, String> deptIdDeptNameMap = sysDeptService.getDeptIdDeptNameMap();

        SysUser user = usernameNicknameMap.get(asset.getResponsiblePersonCode());
        asset.setResponsiblePersonName(user.getNickName());
        asset.setResponsiblePersonDept(deptIdDeptNameMap.get(user.getDeptId().toString()));

        return asset;
    }

    /**
     * 根据资产编号查询资产信息
     */
    @Override
    public Result queryAssetCard(Asset asset) {
        try {
            LambdaUpdateWrapper<Asset> wrapper = new LambdaUpdateWrapper<>();
            String assetCode = asset.getAssetCode();
            if (StringUtils.isBlank(assetCode)) {
                return new Result(HttpStatus.ERROR, "平台资产编号为空");
            }
            if (!assetCode.startsWith("[")) {
                wrapper.eq(Asset::getAssetCode, assetCode);
//                if (StringUtils.isNotBlank(asset.getManageDept())) {
//                    wrapper.eq(Asset::getManageDept, asset.getManageDept());
//                }
                asset = assetMapper.selectOne(wrapper);
                return Result.success(asset);
            }
            assetCode = assetCode.substring(assetCode.indexOf("[") + 2, assetCode.lastIndexOf("]") - 1);
            if (assetCode.contains(",")) {
                assetCode = assetCode.replaceAll("\",\"", ";");
            }
            // 资产编号1;资产编号2;... 的情况
            if (assetCode.contains(";")) {
                List<String> assetCodes = Arrays.asList(assetCode.split(";"));
                wrapper.in(Asset::getAssetCode, assetCodes);
            } else {
                wrapper.eq(Asset::getAssetCode, assetCode);
            }
//            if (StringUtils.isNotBlank(asset.getManageDept())) {
//                wrapper.eq(Asset::getManageDept, asset.getManageDept());
//            }
            List<Asset> assets = assetMapper.selectList(wrapper);
            JSONObject data = new JSONObject();
            data.put("assets", assets);
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("出错");
        }
    }


    /**
     * 查询资产表列表
     *
     * @param
     * @return 资产表
     */
    @Override
    public List<Asset> selectAssetList(Asset asset) {
        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(asset.getAssetName())) {
            wrapper.like(Asset::getAssetName, asset.getAssetName());
        }
        if (StringUtils.isNotBlank(asset.getAssetCode())) {
            wrapper.like(Asset::getAssetCode, asset.getAssetCode());
        }
        if (StringUtils.isNotBlank(asset.getResponsiblePersonName())) {
            wrapper.eq(Asset::getResponsiblePersonName, asset.getResponsiblePersonName());
        }

        List<Asset> assetList = assetMapper.selectList(wrapper);

        Map<String, SysUser> responsiblePersonMap = sysUserService
                .getUserByUserNames(assetList.stream().map(Asset::getResponsiblePersonCode).collect(Collectors.toSet()));
        Map<Long, SysDept> deptMap = sysDeptService
                .selectDeptByIds(responsiblePersonMap.values().stream().map(SysUser::getDeptId).collect(Collectors.toList()));

        if (CollectionUtil.isNotEmpty(assetList)) {
            for (Asset a : assetList) {
                SysUser responsiblePerson = responsiblePersonMap.get(a.getResponsiblePersonCode());
                SysDept dept = deptMap.get(responsiblePerson.getDeptId());
                a.setResponsiblePersonDept(dept.getDeptName());
            }
        }
        return assetList;
    }

    /**
     * 新增资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    @Override
    public int insertAsset(Asset asset) {
        asset.setCreateTime(DateUtils.getNowDate());
        return assetMapper.insertAsset(asset);
    }

    /**
     * 修改资产表
     *
     * @param asset 资产表
     * @return 结果
     */
    @Override
    public int updateAsset(Asset asset, String processId) {
        // 资产更新日志记录
        Asset entity = assetMapper.selectOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetId, asset.getAssetId()));
        assetUpdateLogService.saveLog(entity, processId);

        // 更新资产信息
        asset.setUpdateTime(DateUtils.getNowDate());
        int changed = assetMapper.updateById(asset);

        // TODO 若SAP指定的字段发生修改则后同步给SAP

        return changed;
    }

    /**
     * 批量删除资产
     *
     * @param assetCodes 平台资产编号列表
     * @return
     */
    @Override
    public int deleteAssetByAssetCodes(List<String> assetCodes) {
        return assetMapper.delete(new LambdaQueryWrapper<Asset>().in(Asset::getAssetCode, assetCodes));
    }

    /**
     * 根据部门ID查询部门下所有员工保管的资产
     */
    @Override
    public List<Asset> selectAssetListByDeptId(Long deptId) {
        LambdaQueryWrapper<Asset> assetWrapper = new LambdaQueryWrapper<>();
        List<String> deptIdList = sysDeptService.selectDeptByParentId(deptId);
        SysDept sysDept = sysDeptService.selectDeptById(deptId);
        // 若不为公司
        if (sysDept.getParentId() != 0L) {
            deptIdList.add(Long.toString(deptId));
        }
        List<String> userCodeList = sysUserService.selectUserByDeptId(deptIdList);
        if (CollectionUtil.isNotEmpty(userCodeList)) {
            assetWrapper.in(Asset::getResponsiblePersonCode, userCodeList);
            return this.list(assetWrapper);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 根据工号查询员工资产
     */
    @Override
    public List<Asset> selectAssetByUserName(List<String> userCodeList) {
        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Asset::getResponsiblePersonCode, userCodeList);
        return this.list(wrapper);
    }

    /**
     * 查询部门下所有员工名下的资产
     */
    @Override
    public List<Asset> selectAssetByDeptId(Long deptId) {
        //查询所有子部门
        List<String> sysDeptIdList = sysDeptService.selectDeptByParentId(deptId);
        sysDeptIdList.add(deptId.toString());

        //查询部门所有人员
        List<String> sysUserList = sysUserService.selectUserByDeptId(sysDeptIdList);

        List<List<String>> userCodeListCollection = new ArrayList<>();
        while (sysUserList.size() > 1000) {
            userCodeListCollection.add(sysUserList.subList(0, 1000));
            sysUserList = sysUserList.subList(1000, sysUserList.size());
        }
        if (sysUserList.size() > 0) {
            userCodeListCollection.add(sysUserList);
        }

        List<Asset> assetList = new ArrayList<>();
        for (List<String> userCodeList : userCodeListCollection) {
            assetList.addAll(this.selectAssetByUserName(userCodeList));
        }

        return assetList;
    }

}
