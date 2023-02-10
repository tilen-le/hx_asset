package com.hexing.asset.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastjsonSockJsMessageCodec;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetManagementConfig;
import com.hexing.asset.domain.dto.*;
import com.hexing.asset.domain.vo.AssetQueryParam;
import com.hexing.asset.enums.AssetStatus;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.service.IAssetManagementConfigService;
import com.hexing.asset.service.IAssetService;
import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.asset.utils.CodeUtil;
import com.hexing.common.constant.HttpStatus;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.domain.Result;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.PageUtil;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.impl.SysDeptServiceImpl;
import com.hexing.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.DecimalFormat;
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
    @Autowired
    private IAssetManagementConfigService assetManagementConfigService;


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
        if (ObjectUtil.isNotEmpty(asset)) {
            // 资产管理员
            asset = assetManagementConfigService.selectAssetManagementConfigByCategoryInfo(asset);
            // 解析物料号返回资产大中小类
            JSONObject assetCategoryTree = CodeUtil.getAssetCategoryTree().getJSONObject(0);
            MaterialCategorySimpleDTO dto = CodeUtil.parseMaterialNumber(asset.getMaterialNum(), assetCategoryTree);
            asset.setAssetType(dto.getAssetType());
            asset.setAssetCategory(dto.getAssetCategory());
            asset.setAssetSubCategory(dto.getAssetSubCategory());
            // 保管人和保管部门
            if (StringUtils.isNotEmpty(asset.getResponsiblePersonCode())) {
                SysUser user = sysUserService.getUserByUserName(asset.getResponsiblePersonCode());
                SysDept dept = sysDeptService.selectDeptById(user.getDeptId());
                asset.setResponsiblePersonDept(dept.getDeptName());
            }

        }
        return asset;
    }

    /**
     * 资产信息导入
     *
     * @param assetList       资产信息列表
     * @param isUpdateSupport 是否存在则覆盖
     * @param operName        操作人姓名
     * @return
     */
    @Override
    @Transactional
    public String importAsset(List<Asset> assetList, Boolean isUpdateSupport, String operName) {
        if (CollectionUtil.isEmpty(assetList)) {
            throw new ServiceException("导入资产数据不能为空！");
        }
        int successNum = 0;         /* 导入成功条数 */
        int failureNum = 0;         /* 导入失败条数 */
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < assetList.size(); i++) {
            try {
                JSONObject assetCategoryTree = CodeUtil.getAssetCategoryTree().getJSONObject(0);
                for (Asset asset : assetList) {
                    MaterialCategorySimpleDTO dto = CodeUtil.parseMaterialNumber(asset.getMaterialNum(), assetCategoryTree);
                    asset.setAssetType(dto.getAssetType());
                    asset.setAssetCategory(dto.getAssetCategory());
                    asset.setAssetSubCategory(dto.getAssetSubCategory());
                    save(asset);
                    successNum++;
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + "错误：第 " + (i + 2) + "行出错";
                message.append(msg + e.getMessage());
                log.error(msg, e);
                throw new ServiceException("导入出错");
            }
        }
        message.insert(0, "数据导入完成，共 " + assetList.size() + " 条，成功导入 " + successNum + " 条，出错 " + failureNum + " 条，详情如下：");
        return message.toString();
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
    public List<Asset> selectAssetList(AssetQueryParam param) {
        List<Asset> assetList = new ArrayList<>();
        String username = SecurityUtils.getUsername();
        // 用户数据查看权限判断
//        List<AssetManagementConfig> managementConfigList = assetManagementConfigService.listManagementConfig(username);
//        if (CollectionUtil.isEmpty(managementConfigList)) {
//            return assetList;
//        }

//        boolean isAssetManager = false;
//        boolean isFinancialManager = false;
//        for (AssetManagementConfig managementConfig : managementConfigList) {
//            if (StringUtils.isNotEmpty(managementConfig.getAssetManager()) && managementConfig.getAssetManager().contains(username)) {
//                isAssetManager = true;
//            }
//            if (StringUtils.isNotEmpty(managementConfig.getFinancialManager()) && managementConfig.getFinancialManager().contains(username)) {
//                isFinancialManager = true;
//            }
//        }

        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(param.getAssetCode())) {
            wrapper.like(Asset::getAssetCode, param.getAssetCode());
        }
        if (StringUtils.isNotEmpty(param.getAssetType())) {
            wrapper.eq(Asset::getAssetType, param.getAssetType());
        }
        if (StringUtils.isNotEmpty(param.getAssetCategory())) {
            wrapper.eq(Asset::getAssetCategory, param.getAssetCategory());
        }
        if (CollectionUtil.isNotEmpty(param.getAssetSubCategory())) {
            wrapper.in(Asset::getAssetSubCategory, param.getAssetSubCategory());
        }
        if (StringUtils.isNotEmpty(param.getAssetName())) {
            wrapper.like(Asset::getAssetName, param.getAssetName());
        }
        if (CollectionUtil.isNotEmpty(param.getAssetStatus())) {
            wrapper.in(Asset::getAssetStatus, param.getAssetStatus());
        }
        if (StringUtils.isNotEmpty(param.getCompany())) {
            wrapper.eq(Asset::getCompany, param.getCompany());
        }
        if (ObjectUtil.isNotEmpty(param.getFixed())) {
            wrapper.eq(Asset::getFixed, param.getFixed());
        }
        if (ObjectUtil.isNotEmpty(param.getCapitalizationStartDate())) {
            wrapper.ge(Asset::getCapitalizationDate, param.getCapitalizationStartDate());
        }
        if (ObjectUtil.isNotEmpty(param.getCapitalizationEndDate())) {
            wrapper.le(Asset::getCapitalizationDate, param.getCapitalizationEndDate());
        }
        if (StringUtils.isNotEmpty(param.getResponsiblePersonDept())) {
            // 查询指定部门下所有部门的id
            List<String> childDeptIdList = sysDeptService.selectDeptByParentId(Long.valueOf(param.getResponsiblePersonDept()));
            childDeptIdList.add(param.getResponsiblePersonDept());
            // 查询这些部门下所有人的工号
            List<String> usernameList = sysUserService.selectUserByDeptId(childDeptIdList);
            wrapper.in(Asset::getResponsiblePersonCode, usernameList);
        }

        PageUtil.startPage();
        assetList = assetMapper.selectList(wrapper);

        if (CollectionUtil.isNotEmpty(assetList)) {
            // 解析物料号返回资产大中小类
            JSONObject assetCategoryTree = CodeUtil.getAssetCategoryTree().getJSONObject(0);
            for (Asset asset : assetList) {
                MaterialCategorySimpleDTO dto = CodeUtil.parseMaterialNumber(asset.getMaterialNum(), assetCategoryTree);
                asset.setAssetType(dto.getAssetType());
                asset.setAssetCategory(dto.getAssetCategory());
                asset.setAssetSubCategory(dto.getAssetSubCategory());
            }

            Map<String, SysUser> responsiblePersonMap = sysUserService
                    .getUserByUserNames(assetList.stream().map(Asset::getResponsiblePersonCode).collect(Collectors.toSet()));
            Map<Long, SysDept> deptMap = sysDeptService
                    .selectDeptByIds(responsiblePersonMap.values().stream().map(SysUser::getDeptId).collect(Collectors.toList()));
            for (Asset a : assetList) {
                if (StringUtils.isNotEmpty(a.getResponsiblePersonCode())) {
                    SysUser responsiblePerson = responsiblePersonMap.get(a.getResponsiblePersonCode());
                    SysDept dept = deptMap.get(responsiblePerson.getDeptId());
                    a.setResponsiblePersonDept(dept.getDeptName());
                }
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

    /**
     * SAP采购单同步接口
     */
    @Override
    public void sapAdd(List<SapPurchaseOrder> orderList) {
        log.debug("==== SAP采购单同步接口：开始新建资产信息 ====");
        int totalNum = 0;
        for (SapPurchaseOrder order : orderList) {
            int numberOfArrival = order.getNumberOfArrival().intValue();
            List<Asset> assetList = new ArrayList<>();
            if (ObjectUtil.isNotNull(numberOfArrival)) {
                LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Asset::getMaterialNum, order.getMaterialNumber())
                        .orderByDesc(Asset::getSerialNum)
                        .last("LIMIT 1");
                Asset theLastOne = this.getOne(wrapper);

                int nextNum = ObjectUtil.isNotEmpty(theLastOne) ? theLastOne.getSerialNum() + 1 : 1;
                DecimalFormat df = new DecimalFormat("0000");
                for (int i = 1; i <= numberOfArrival; i++) {
                    Asset asset = new Asset();
                    String assetCode = order.getMaterialNumber() + df.format(nextNum);
                    asset.setMaterialNum(order.getMaterialNumber())
                            .setSerialNum(nextNum)
                            .setAssetName(order.getMaterialText())
                            .setAssetCode(assetCode)
                            .setCompany(order.getCompanyCode())
                            .setPurchaseOrderNo(order.getPurchaseOrder())
                            .setProvider(order.getProvider())
                            .setProviderName(order.getProviderDescription())
                            .setOriginalValue(order.getPrice())
                            .setMonetaryUnit(order.getMoneyType())
                            .setAssetStatus(AssetStatus.IN_STORE.getCode())
                            .setProofOfMaterial(order.getProofOfMaterial())
                            .setCreateBy("SAP")
                            .setCreateTime(new Date())
                            .setAssetType(order.getMaterialNumber().substring(0, 1))
                            .setAssetCategory(order.getMaterialNumber().substring(1, 3))
                            .setAssetSubCategory(order.getMaterialNumber().substring(3, 5))
                            .setFixed("0");
                    assetList.add(asset);
                    nextNum++;
                }
                this.saveBatch(assetList);
                totalNum += assetList.size();
            }
        }
        log.debug("==== SAP采购单同步接口：资产信息新建成功，新增 " + totalNum + " 个资产 ====");
    }

    /**
     * SAP价值传输接口
     */
    @Override
    public List<SapValueDTO> sapSyncValue(List<SapValueDTO> sapValueList) {
        Set<String> assetCodeSet = sapValueList.stream().map(SapValueDTO::getAssetCode).collect(Collectors.toSet());
        List<Asset> assetList = this.list(new LambdaQueryWrapper<Asset>().in(Asset::getAssetCode, assetCodeSet));
        if (CollectionUtil.isNotEmpty(assetList)) {
            Map<String, Asset> assetMap = assetList.stream().collect(Collectors.toMap(Asset::getAssetCode, o -> o));
            for (SapValueDTO sapValueDTO : sapValueList) {
                Asset asset = assetMap.get(sapValueDTO.getAssetCode());
                if (ObjectUtil.isNotEmpty(asset)) {
                    asset.setOriginalValue(sapValueDTO.getOriginalValue())
                            .setNetValue(sapValueDTO.getNetValue())
                            .setCanUseYears(sapValueDTO.getCanUseYears())
                            .setCanUseMonths(sapValueDTO.getCanUseMonths());
                }
                try {
                    this.updateById(asset);
                    sapValueDTO.setSuccess(true);
                } catch (Exception e) {
                    sapValueDTO.setSuccess(false);
                    sapValueDTO.setReason(e.getMessage());
                }
            }
        }
        return sapValueList;
    }
}
