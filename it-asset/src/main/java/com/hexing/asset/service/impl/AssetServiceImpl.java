package com.hexing.asset.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastjsonSockJsMessageCodec;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.*;
import com.hexing.asset.domain.dto.*;
import com.hexing.asset.domain.vo.*;
import com.hexing.asset.enums.AssetProcessType;
import com.hexing.asset.enums.AssetStatus;
import com.hexing.asset.enums.ManagerType;
import com.hexing.asset.enums.UIPCodeEnum;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.service.*;
import com.hexing.asset.utils.CodeUtil;
import com.hexing.common.constant.HttpStatus;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.domain.Result;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysRole;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.enums.UserType;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.PageUtil;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.system.service.ISysRoleService;
import com.hexing.system.service.impl.SysDeptServiceImpl;
import com.hexing.system.service.impl.SysUserServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.text.StyledEditorKit;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Consumer;
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
    @Autowired
    private IAssetUpdateLogService assetUpdateLogService;
    @Autowired
    private IAssetManagementConfigService assetManagementConfigService;
    @Autowired
    private IAssetProcessService assetProcessService;
    @Autowired
    private IUIPService uipService;
    @Autowired
    private IAssetProcessService processService;
    @Autowired
    private ISysRoleService sysRoleService;


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
            asset.setTransfer("0");
            LambdaQueryWrapper<AssetProcess> w = new LambdaQueryWrapper<>();
            w.eq(AssetProcess::getAssetCode, asset.getAssetCode());
            w.eq(AssetProcess::getProcessType, AssetProcessType.PROCESS_TRANSFORM.getCode()).
                    or().eq(AssetProcess::getProcessType, AssetProcessType.PROCESS_ACCOUNT_TRANSFORM.getCode());
            w.orderByDesc(AssetProcess::getCreateTime);
            AssetProcess process = assetProcessService.list(w).stream().findFirst().orElse(null);
            if (ObjectUtil.isNotEmpty(process) && process.getProcessType().equals(AssetProcessType.PROCESS_TRANSFORM.getCode())) {
                asset.setTransfer("1");
            }
            // 资产管理员
            asset = assetManagementConfigService.selectAssetManagementConfigByCategoryInfo(asset);
            // 解析物料号返回资产大中小类
            JSONObject assetCategoryTree = CodeUtil.getAssetCategoryTree().getJSONObject(0);
            MaterialCategorySimpleDTO dto = CodeUtil.parseMaterialNumber(asset.getMaterialNum(), assetCategoryTree);
            asset.setAssetType(dto.getAssetType());
            asset.setAssetCategory(dto.getAssetCategory());
            asset.setAssetSubCategory(dto.getAssetSubCategory());
            // 保管人和保管部门
            if (StringUtils.isNotEmpty(asset.getResponsiblePersonDept())) {
//                SysUser user = sysUserService.getUserByUserName(asset.getResponsiblePersonCode());
                SysDept dept = sysDeptService.selectDeptById(Long.valueOf(asset.getResponsiblePersonDept()));
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
            throw new ServiceException("导入失败：导入资产数据不能为空！");
        }
        int successNum = 0;         /* 导入成功条数 */
        int failureNum = 0;         /* 导入失败条数 */
        StringBuilder message = new StringBuilder();
        AssetUpdateLog updateLog = new AssetUpdateLog();
        String type = AssetProcessType.PROCESS_ASSET_CREATE.getCode();
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
        for (int i = 0; i < assetList.size(); i++) {
            try {
                Asset asset = assetList.get(i);
                if (StringUtils.isBlank(asset.getCurrentMaterialNum())) {
                    throw new ServiceException("导入失败：正确的物料号字段缺失！导入失败");
                }

                LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Asset::getMaterialNum, asset.getCurrentMaterialNum())
                        .orderByDesc(Asset::getSerialNum)
                        .last("LIMIT 1");
                Asset theLastOne = this.getOne(wrapper);
                int nextNum = ObjectUtil.isNotEmpty(theLastOne) ? theLastOne.getSerialNum() + 1 : 1;
                DecimalFormat df = new DecimalFormat("0000");
                String assetCode = asset.getCurrentMaterialNum() + df.format(nextNum);
                asset.setAssetCode(assetCode);

                asset.setSerialNum(nextNum);

                asset.setAssetType(asset.getCurrentMaterialNum().substring(0, 1));
                asset.setAssetCategory(asset.getCurrentMaterialNum().substring(1, 3));
                asset.setAssetSubCategory(asset.getCurrentMaterialNum().substring(3, 5));

                // 资产管理员
                asset = assetManagementConfigService.selectAssetManagementConfigByCategoryInfo(asset);

                asset.setFixed("1");

                asset.setCreateBy("system");
                asset.setCreateTime(DateUtils.getNowDate());
                save(asset);
                BeanUtils.copyProperties(asset, updateLog);
                updateLog.setProcessType(type);
                updateLog.setCreateBy(userCode);
                updateLog.setCreateTime(DateUtils.getNowDate());
                assetUpdateLogService.save(updateLog);
                successNum++;

            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + "错误：第 " + (i + 2) + "行出错";
                message.append(msg + e.getMessage());
                log.error(msg, e);
                throw new ServiceException("导入出错：" + e);
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
        List<Asset> assetList;

        String username = SecurityUtils.getUsername();
        SysUser user = sysUserService.getUserByUserName(username);
        List<SysRole> roleList = sysRoleService.selectUserRoleListByUserId(user.getUserId());

        boolean isAdmin = false;
        for (SysRole sysRole : roleList) {
            if ("超级管理员".equals(sysRole.getRoleName())
                    || "总务管理员".equals(sysRole.getRoleName())
                    || "财务管理员".equals(sysRole.getRoleName())) {
                isAdmin = true;
                break;
            }
        }
        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();

        // 超级管理员可查看到所有数据
        if (!isAdmin) {
            boolean isAssetManager = false;
            boolean isFinancialManager = false;

            // 若为资产管理员
            List<AssetManagementConfig> assetManagementConfigList = assetManagementConfigService
                    .selectManagementConfigListByAssetManager(username, ManagerType.ASSET_MANAGER.getType());
            if (CollectionUtil.isNotEmpty(assetManagementConfigList)) {
                isAssetManager = true;
                wrapper.nested(i -> {
                    for (AssetManagementConfig managementConfig : assetManagementConfigList) {
                        i.or(ii -> {
                            ii.eq(Asset::getAssetType, managementConfig.getAssetType())
                                    .eq(Asset::getAssetCategory, managementConfig.getAssetCategory());
                            if (StringUtils.isNotEmpty(managementConfig.getAssetSubCategory())) {
                                // 子类拆分为列表
                                List<String> subCategoryList = Arrays.asList(managementConfig.getAssetSubCategory().split(","));
                                ii.in(Asset::getAssetSubCategory, subCategoryList);
                            }
                        });
                    }
                });
            }

            // 若为账务管理员
            List<AssetManagementConfig> financialManagementConfigList = assetManagementConfigService
                    .selectManagementConfigListByAssetManager(username, ManagerType.FINANCIAL_MANAGER.getType());
            if (CollectionUtil.isNotEmpty(financialManagementConfigList)) {
                isFinancialManager = true;
                List<String> companyList = financialManagementConfigList.stream().map(AssetManagementConfig::getCompany).collect(Collectors.toList());
                wrapper.in(Asset::getCompany, companyList);
            }

            if (!isAssetManager && !isFinancialManager) {
                return new ArrayList<>();
            }
        }

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
            wrapper.in(Asset::getCompany, param.getCompany());
        }
        if (ObjectUtil.isNotEmpty(param.getFixed())) {
            wrapper.in(Asset::getFixed, param.getFixed());
        }
        if (ObjectUtil.isNotEmpty(param.getCapitalizationStartDate())) {
            wrapper.ge(Asset::getCapitalizationDate, param.getCapitalizationStartDate());
        }
        if (ObjectUtil.isNotEmpty(param.getCapitalizationEndDate())) {
            wrapper.le(Asset::getCapitalizationDate, param.getCapitalizationEndDate());
        }
        if (ObjectUtil.isNotEmpty(param.getCreateTimeBegin())) {
            wrapper.ge(Asset::getCreateTime, param.getCreateTimeBegin());
        }
        if (ObjectUtil.isNotEmpty(param.getCreateTimeEnd())) {
            wrapper.le(Asset::getCreateTime, param.getCreateTimeEnd());
        }
        if (StringUtils.isNotEmpty(param.getResponsiblePersonDept())) {
            // 查询指定部门下所有部门的id
            List<String> childDeptIdList = sysDeptService.selectDeptByParentId(Long.valueOf(param.getResponsiblePersonDept()));
            childDeptIdList.add(param.getResponsiblePersonDept());
            // 查询这些部门下所有人的工号
            List<String> usernameList = sysUserService.selectUserByDeptId(childDeptIdList);
            wrapper.in(Asset::getResponsiblePersonCode, usernameList);
        }

        wrapper.orderByDesc(Asset::getUpdateTime);

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

            List<Long> deptIdList = new ArrayList<>();
            for (Asset asset : assetList) {
                if (StringUtils.isNotEmpty(asset.getResponsiblePersonDept())) {
                    deptIdList.add(Long.valueOf(asset.getResponsiblePersonDept()));
                }
            }
            Map<Long, SysDept> deptMap = sysDeptService.selectDeptByIds(deptIdList);
            for (Asset a : assetList) {
                if (StringUtils.isNotEmpty(a.getResponsiblePersonDept())) {
                    SysDept dept = deptMap.get(Long.valueOf(a.getResponsiblePersonDept()));
                    a.setResponsiblePersonDept(dept.getDeptName());
                }
            }
        }

        return assetList;
    }

    /**
     * 查询资产表列表
     *
     * @param
     * @return 资产表
     */
    @Override
    public List<Asset> selectAllAsset(AssetQueryParam param) {
        List<Asset> assetList;

        String username = SecurityUtils.getUsername();
        SysUser user = sysUserService.getUserByUserName(username);
        List<SysRole> roleList = sysRoleService.selectUserRoleListByUserId(user.getUserId());

        boolean isAdmin = false;
        for (SysRole sysRole : roleList) {
            if ("超级管理员".equals(sysRole.getRoleName())
                    || "总务管理员".equals(sysRole.getRoleName())
                    || "财务管理员".equals(sysRole.getRoleName())) {
                isAdmin = true;
                break;
            }
        }
        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();

        // 超级管理员可查看到所有数据
        if (!isAdmin) {
            boolean isAssetManager = false;
            boolean isFinancialManager = false;

            // 若为资产管理员
            List<AssetManagementConfig> assetManagementConfigList = assetManagementConfigService
                    .selectManagementConfigListByAssetManager(username, ManagerType.ASSET_MANAGER.getType());
            if (CollectionUtil.isNotEmpty(assetManagementConfigList)) {
                isAssetManager = true;
                wrapper.nested(i -> {
                    for (AssetManagementConfig managementConfig : assetManagementConfigList) {
                        i.or(ii -> {
                            ii.eq(Asset::getAssetType, managementConfig.getAssetType())
                                    .eq(Asset::getAssetCategory, managementConfig.getAssetCategory());
                            if (StringUtils.isNotEmpty(managementConfig.getAssetSubCategory())) {
                                // 子类拆分为列表
                                List<String> subCategoryList = Arrays.asList(managementConfig.getAssetSubCategory().split(","));
                                ii.in(Asset::getAssetSubCategory, subCategoryList);
                            }
                        });
                    }
                });
            }

            // 若为账务管理员
            List<AssetManagementConfig> financialManagementConfigList = assetManagementConfigService
                    .selectManagementConfigListByAssetManager(username, ManagerType.FINANCIAL_MANAGER.getType());
            if (CollectionUtil.isNotEmpty(financialManagementConfigList)) {
                isFinancialManager = true;
                List<String> companyList = financialManagementConfigList.stream().map(AssetManagementConfig::getCompany).collect(Collectors.toList());
                wrapper.in(Asset::getCompany, companyList);
            }

            if (!isAssetManager && !isFinancialManager) {
                return new ArrayList<>();
            }
        }

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
            wrapper.in(Asset::getCompany, param.getCompany());
        }
        if (ObjectUtil.isNotEmpty(param.getFixed())) {
            wrapper.in(Asset::getFixed, param.getFixed());
        }
        if (ObjectUtil.isNotEmpty(param.getCapitalizationStartDate())) {
            wrapper.ge(Asset::getCapitalizationDate, param.getCapitalizationStartDate());
        }
        if (ObjectUtil.isNotEmpty(param.getCapitalizationEndDate())) {
            wrapper.le(Asset::getCapitalizationDate, param.getCapitalizationEndDate());
        }
        if (ObjectUtil.isNotEmpty(param.getCreateTimeBegin())) {
            wrapper.ge(Asset::getCreateTime, param.getCreateTimeBegin());
        }
        if (ObjectUtil.isNotEmpty(param.getCreateTimeEnd())) {
            wrapper.le(Asset::getCreateTime, param.getCreateTimeEnd());
        }
        if (StringUtils.isNotEmpty(param.getResponsiblePersonDept())) {
            // 查询指定部门下所有部门的id
            List<String> childDeptIdList = sysDeptService.selectDeptByParentId(Long.valueOf(param.getResponsiblePersonDept()));
            childDeptIdList.add(param.getResponsiblePersonDept());
            // 查询这些部门下所有人的工号
            List<String> usernameList = sysUserService.selectUserByDeptId(childDeptIdList);
            wrapper.in(Asset::getResponsiblePersonCode, usernameList);
        }

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

            List<Long> deptIdList = new ArrayList<>();
            for (Asset asset : assetList) {
                if (StringUtils.isNotEmpty(asset.getResponsiblePersonDept())) {
                    deptIdList.add(Long.valueOf(asset.getResponsiblePersonDept()));
                }
            }
            Map<Long, SysDept> deptMap = sysDeptService.selectDeptByIds(deptIdList);
            for (Asset a : assetList) {
                if (StringUtils.isNotEmpty(a.getResponsiblePersonDept())) {
                    SysDept dept = deptMap.get(Long.valueOf(a.getResponsiblePersonDept()));
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
    public int updateAsset(Asset asset, AssetProcess process) {
        // 资产更新日志记录
        assetUpdateLogService.saveLog(asset, process);
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
    @Transactional(rollbackFor = Exception.class)
    public void sapAdd(List<SapPurchaseOrder> orderList) {
        log.debug("==== SAP采购单同步接口：开始新建资产信息 ====");
        int totalNum = 0;
        String type = AssetProcessType.PROCESS_ASSET_CREATE.getCode();
        String userCode = "sap";
        for (SapPurchaseOrder order : orderList) {
            int numberOfArrival = order.getNumberOfArrival().intValue();
            List<Asset> assetList = new ArrayList<>();
            LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Asset::getMaterialNum, order.getMaterialNumber())
                    .orderByDesc(Asset::getSerialNum)
                    .last("LIMIT 1");
            Asset theLastOne = this.getOne(wrapper);

            int nextNum = ObjectUtil.isNotEmpty(theLastOne) ? theLastOne.getSerialNum() + 1 : 1;
            DecimalFormat df = new DecimalFormat("0000");
            List<AssetUpdateLog> logList = new ArrayList<>();
            for (int i = 1; i <= numberOfArrival; i++) {
                Asset asset = new Asset();
                String assetCode = order.getMaterialNumber() + df.format(nextNum);
                asset.setMaterialNum(order.getMaterialNumber())
                        .setSerialNum(nextNum)
                        .setAssetName(order.getMaterialText())
                        .setAssetCode(assetCode)
                        .setSapAssetCode(assetCode)
                        .setCompany(order.getCompanyCode())
                        .setPurchaseOrderNo(order.getPurchaseOrder())
                        .setProvider(order.getProvider())
                        .setProviderName(order.getProviderDescription())
                        .setOriginalValue(order.getOriginalValue())
                        .setMonetaryUnit(order.getMoneyType())
                        .setAssetStatus(AssetStatus.IN_STORE.getCode())
                        .setProofOfMaterial(order.getProofOfMaterial())
                        .setCreateBy("SAP")
                        .setCreateTime(new Date())
                        .setUpdateBy("SAP")
                        .setUpdateTime(new Date())
                        .setAssetType(order.getMaterialNumber().substring(0, 1))
                        .setAssetCategory(order.getMaterialNumber().substring(1, 3))
                        .setAssetSubCategory(order.getMaterialNumber().substring(3, 5))
                        .setFixed("0")
                        .setUnit(order.getUnit())
                        .setStorageDate(order.getOrderDate());
                assetList.add(asset);
                AssetUpdateLog updateLog = new AssetUpdateLog();
                BeanUtils.copyProperties(asset, updateLog);
                updateLog.setCreateBy(userCode);
                updateLog.setCreateTime(DateUtils.getNowDate());
                updateLog.setProcessType(type);
                logList.add(updateLog);
                nextNum++;
            }
            this.saveBatch(assetList);
            assetUpdateLogService.saveBatch(logList);
            totalNum += assetList.size();
        }
        log.debug("==== SAP采购单同步接口：资产信息新建成功，新增 " + totalNum + " 个资产 ====");
    }

    /**
     * SAP价值传输接口
     */
    @Override
    public List<SapValueDTO> sapSyncValue(List<SapValueDTO> sapValueList) {
        Set<String> sapCodeSet = sapValueList.stream().map(SapValueDTO::getSapCode).collect(Collectors.toSet());
        List<Asset> assetList = this.list(new LambdaQueryWrapper<Asset>().in(Asset::getSapCode, sapCodeSet));
        if (CollectionUtil.isNotEmpty(assetList)) {
            log.debug("==== SAP价值传输接口：开始同步价值信息 ====");
            Map<String, Asset> assetMap = assetList.stream().collect(Collectors.toMap(Asset::getSapCode, o -> o));
            for (SapValueDTO sapValueDTO : sapValueList) {
                Asset asset = assetMap.get(sapValueDTO.getSapCode());
                if (ObjectUtil.isNotEmpty(asset)) {
                    asset.setOriginalValue(sapValueDTO.getOriginalValue())
                            .setNetValue(sapValueDTO.getNetValue())
                            .setCanUseYears(sapValueDTO.getCanUseYears())
                            .setCanUseMonths(sapValueDTO.getCanUseMonths())
                            .setDepreciation(sapValueDTO.getDepreciation());
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
        log.debug("==== SAP价值传输接口：价值信息同步完成 ====");
        return sapValueList;
    }

    /**
     * 资产转固
     */
    @Override
    public JSONObject fixAsset(AssetFixVO vo) throws Exception {
        SapAssetFixDTO sapAssetFixDTO = new SapAssetFixDTO();
        Asset asset = this.selectAssetByAssetCode(vo.getAssetCode());
        if (ObjectUtil.isNotEmpty(asset)) {
            // 推送SAP
            sapAssetFixDTO.setBUKRS(asset.getCompany())
                    .setANLKL(vo.getCategory())
                    .setTXT50(asset.getAssetName())
                    .setTXA50(asset.getStandard())
                    .setANLHTXT(vo.getBelong())
                    .setSERNR(asset.getMaterialNum())
                    .setINVNR(asset.getAssetCode())
                    .setMENGE(1.0)
                    .setMEINS(asset.getUnit())
                    .setINVZU(asset.getCurrentLocation())
                    .setZYONGTU(vo.getUsage())
                    .setZZANLU003("")
                    .setZZANLU004(asset.getResponsiblePersonCode())
                    .setDEPS(vo.getDEPS())
                    .setKOSTLV(vo.getResponsibilityCostCenterCode())
                    .setRAUMN(asset.getResponsiblePersonName())
                    .setLIFNR(asset.getProvider())
                    .setNAME1(asset.getProviderName())
                    .setNAME2(vo.getProvider());

            JSONArray data = new JSONArray();
            data.add(sapAssetFixDTO);
            String responseBody = uipService.sendToSAP(data, UIPCodeEnum.FIX_ASSET_INTERFACE.getCode(), "资产转固");

            JSONObject responseBodyJO = JSONObject.parseObject(responseBody);
            String sapResponseCode = responseBodyJO.getString("CODE");
            if ("E".equals(sapResponseCode)) {
                throw new Exception("SAP报错：" + responseBodyJO);
            } else if ("S".equals(sapResponseCode)) {
                return responseBodyJO;
            }
        }
        return null;
    }

    /**
     * 资产派发
     */
    @Override
    public JSONObject receiveAsset(AssetReceiveVO vo) throws Exception {
        JSONArray data = new JSONArray();
        data.add(vo);
        String responseBody = uipService.sendToSAP(data, UIPCodeEnum.RECEIVE_ASSET_INTERFACE.getCode(), "资产派发");
        JSONObject responseBodyJO = JSONObject.parseObject(responseBody);
        String sapResponseCode = responseBodyJO.getString("CODE");
        if ("E".equals(sapResponseCode)) {
            throw new Exception("SAP报错：" + responseBodyJO);
        } else if ("S".equals(sapResponseCode)) {
            log.debug("SAP资产派发成功：" + responseBodyJO);
            return responseBodyJO;
        }
        return responseBodyJO;
    }

    /**
     * 资产账务转移
     */
    @Override
    public JSONObject transferAsset(SapAssetTransferDTO dto) throws Exception {
        JSONArray data = new JSONArray();
        data.add(dto);
        String responseBody = uipService.sendToSAP(data, UIPCodeEnum.TRANSFER_ASSET_INTERFACE.getCode(), "资产转移");
        JSONObject responseBodyJO = JSONObject.parseObject(responseBody);
        String sapResponseCode = responseBodyJO.getString("CODE");
        if ("E".equals(sapResponseCode)) {
            throw new Exception("SAP报错：" + responseBodyJO);
        } else if ("S".equals(sapResponseCode)) {
            log.debug("SAP资产转移成功：" + responseBodyJO);
            return responseBodyJO;
        }
        return responseBodyJO;
    }

}
