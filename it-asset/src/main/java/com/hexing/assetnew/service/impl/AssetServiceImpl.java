package com.hexing.assetnew.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.assetnew.domain.Asset;
import com.hexing.asset.domain.dto.ExchangeProcessDTO;
import com.hexing.asset.domain.dto.OldProcessCommonDTO;
import com.hexing.asset.domain.dto.UserAssetInfoDTO;
import com.hexing.asset.enums.AssetStatus;
import com.hexing.asset.enums.SAPRespType;
import com.hexing.asset.enums.UIPcodeEnum;
import com.hexing.assetnew.enums.SysDictTypeEnum;
import com.hexing.assetnew.mapper.AssetMapper;
import com.hexing.asset.service.IAssetProcessExchangeService;
import com.hexing.asset.service.IAssetProcessTransferService;
import com.hexing.assetnew.service.IAssetService;
import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.common.constant.HttpStatus;
import com.hexing.common.core.domain.Result;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.mapper.SysDictDataMapper;
import com.hexing.system.service.impl.SysDeptServiceImpl;
import com.hexing.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    private List<SysDictData> assetImportRequiredFieldDictDataList = null;

    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private SysDictDataMapper sysDictDataMapper;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysDeptServiceImpl sysDeptService;
    @Autowired
    private IAssetProcessExchangeService assetProcessExchangeService;
    @Autowired
    private IAssetProcessTransferService assetProcessTransferService;
    @Autowired
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
                if (StringUtils.isNotBlank(asset.getManageDept())) {
                    wrapper.eq(Asset::getManageDept, asset.getManageDept());
                }
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
            if (StringUtils.isNotBlank(asset.getManageDept())) {
                wrapper.eq(Asset::getManageDept, asset.getManageDept());
            }
            List<Asset> assets = assetMapper.selectList(wrapper);
            JSONObject data = new JSONObject();
            data.put("assets", assets);
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("出错");
        }
    }

    @Override
    public Result queryPersonInfoAndAssetsByUserCode(UserAssetInfoDTO params) {
        try {
            // 查询保管人信息
            SysUser user = sysUserService.selectUserByUserName(params.getUserCode());
            if (user == null) {
                return new Result(HttpStatus.ERROR, "未查询到此保管人");
            }
            // 查询保管人所属部门
            if (user.getDeptId() == null) {
                return new Result(HttpStatus.ERROR, "未查询到保管部门");
            }
            SysDept dept = sysDeptService.selectDeptById(user.getDeptId());

            LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Asset::getResponsiblePersonCode, params.getUserCode())
                    .eq(StringUtils.isNotBlank(params.getManageDept()), Asset::getManageDept, params.getManageDept());
            List<Asset> assets = assetMapper.selectList(wrapper);
            if (CollectionUtil.isNotEmpty(assets)) {
                assets.forEach(asset -> asset.setResponsiblePersonDept(dept.getDeptName()));
            }

            params.setAssets(assets);

            return Result.success(params);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(HttpStatus.ERROR, "出错");
        }
    }

    @Override
    @Transactional
    public Result updateAssetExchange(OldProcessCommonDTO<ExchangeProcessDTO> params) {
        try {
            ExchangeProcessDTO data = params.getData();

            String adminId = data.getAdminCode(); // 资产管理员工号
            String userCode = data.getUserCode(); // 流程申请人工号

            String locationOfOldAsset = data.getBefore().getLocation();;
            String codeOfOldAsset = data.getBefore().getAssetCode();

            String locationOfNewAsset = data.getAfter().getLocation();
            String codeOfNewAsset = data.getAfter().getAssetCode();

            String instanceId = data.getInstanceId();

            // 老资产
            int oldAssetProcessId = assetProcessExchangeService.saveProcess(instanceId, userCode, codeOfOldAsset);

            SysUser adminUser = sysUserService.getUserByUserName(adminId);

            Asset oldAsset = assetMapper.selectOne(new LambdaQueryWrapper<Asset>()
                    .eq(Asset::getAssetCode, codeOfOldAsset));

            oldAsset.setLocation(locationOfNewAsset)
                    .setResponsiblePersonCode(adminId)
                    .setResponsiblePersonName(adminUser.getNickName())
                    .setAssetStatus(AssetStatus.UNUSED.getName())
                    .setUpdateBy(userCode)
                    .setUpdateTime(new Date());

            int updateOfOld = this.updateAsset(oldAsset, Integer.toString(oldAssetProcessId));
            if (updateOfOld == 0) {
                log.error("老资产更新失败");
                throw new ServiceException("老资产更新失败");
            }

            // 新资产
            int newAssetProcessId = assetProcessExchangeService.saveProcess(instanceId, userCode, codeOfNewAsset);

            SysUser user = sysUserService.getUserByUserName(userCode);
            Asset newAsset = assetMapper.selectOne(new LambdaQueryWrapper<Asset>()
                    .eq(Asset::getAssetCode, codeOfNewAsset));
            newAsset.setLocation(locationOfOldAsset)
                    .setResponsiblePersonCode(userCode)
                    .setResponsiblePersonName(user.getNickName())
                    .setAssetStatus(AssetStatus.USING.getName())
                    .setUpdateBy(userCode)
                    .setUpdateTime(new Date());
            int updateOfNew = this.updateAsset(newAsset, Integer.toString(newAssetProcessId));
            if (updateOfNew == 0) {
                log.error("新资产更新失败");
                throw new ServiceException("新资产更新失败");
            }

            log.warn("资产更换流程，目标资产更成功");

            return Result.success("更换成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(HttpStatus.ERROR, "出错");
        }
    }

    @Override
    @Transactional
    public Result updateAssetTransfer(JSONObject params) {
        try {
            params = params.getJSONObject("data");

            JSONArray assets = params.getJSONArray("assets");
            String recipient = params.getString("recipientCode");

            String userCode = params.getString("userCode");       /* 流程发起人工号 */
            String instanceId = params.getString("instanceId");

            if (CollectionUtil.isNotEmpty(assets)) {
                for (int i = 0; i < assets.size(); i++) {
                    JSONObject updateInfo = assets.getObject(i, JSONObject.class);
                    if (StringUtils.isNull(recipient)) {
                        return new Result(HttpStatus.ERROR, "接收者工号不能为空");
                    }
                    SysUser recipientInfo = sysUserService.selectUserByUserName(recipient);
                    if (recipientInfo == null) {
                        return new Result(HttpStatus.ERROR, "后台无该接收者信息");
                    }
                    int processId = assetProcessTransferService.saveProcess(instanceId, userCode, updateInfo.getString("assetCode"));
                    String location = updateInfo.getString("location");
                    Asset asset = assetMapper.selectOne(new LambdaQueryWrapper<Asset>()
                            .eq(Asset::getAssetCode, updateInfo.getString("assetCode")));
                    asset.setLocation(location)
                            .setResponsiblePersonCode(recipient)
                            .setResponsiblePersonName(recipientInfo.getNickName());

                    int update = this.updateAsset(asset, Integer.toString(processId));

                    if (update == 0) {
                        throw new ServiceException("更新未成功");
                    }
                }
            }
            return Result.success("资产转移成功");
        } catch (Exception e) {
            log.error("", e);
            return new Result(HttpStatus.ERROR, "出错");
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
        if (StringUtils.isNotBlank(asset.getLocation())) {
            wrapper.eq(Asset::getLocation, asset.getLocation());
        }
        if (StringUtils.isNotBlank(asset.getResponsiblePersonName())) {
            wrapper.eq(Asset::getResponsiblePersonName, asset.getResponsiblePersonName());
        }
        if (StringUtils.isNotBlank(asset.getCostCenter())) {
            wrapper.eq(Asset::getCostCenter, asset.getCostCenter());
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
        if (StringUtils.isNull(assetList) || assetList.size() == 0) {
            throw new ServiceException("导入资产数据不能为空！");
        }
        int successNum = 0;         /* 导入成功条数 */
        int failureNum = 0;         /* 导入失败条数 */
        Map<String, SysUser> userMap = sysUserService
                .getUserByUserNames(assetList.stream().map(Asset::getResponsiblePersonCode).collect(Collectors.toSet()));
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < assetList.size(); i++) {
            Asset asset = assetList.get(i);
            try {
                // 检查必填字段是否存在未填
                String requiredFieldLeftUnfilled = checkRequiredFields(asset);
                if (requiredFieldLeftUnfilled != null) {
                    failureNum++;
                    message.append("<br/><font color=\"red\">" + "第 " + (i + 2) + " 行导入失败，原因：" + "必填字段：" + requiredFieldLeftUnfilled + " 未填写或格式存在错误</font>");
                    continue;
                }
                // 检查保管人是否存在于系统中
                SysUser user = userMap.get(asset.getResponsiblePersonCode());
                if (ObjectUtil.isNull(user)) {
                    failureNum++;
                    message.append("<br/><font color=\"red\">" + "第 " + (i + 2) + " 行导入失败，原因：" + "该保管人在系统中不存在");
                    continue;
                }
                if (!isUpdateSupport) {
                    LambdaQueryWrapper<Asset> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Asset::getFinancialAssetCode, asset.getFinancialAssetCode())
                            .eq(Asset::getCompanyCode, asset.getCompanyCode());
                    Asset a = assetMapper.selectOne(queryWrapper);
                    if (a != null) {
                        message.append("<br/>" + "第 " + (i + 2) + " 行导入失败，原因：" + "资产已存在，平台资产编号为：" + a.getAssetCode());
                        failureNum++;
                    } else {
                        String assetCode = generateAssetCode(asset);
                        asset.setAssetCode(assetCode)
                                .setResponsiblePersonName(user.getNickName())
                                .setCreateBy(operName)
                                .setCreateTime(new Date());
                        assetMapper.insert(asset);
                        successNum++;
                    }
                } else {
                    asset.setResponsiblePersonName(user.getNickName()); // 更新资产保管人信息
                    int update = this.updateAsset(asset, "资产导入");
                    if (update > 0) {
                        successNum++;
                    } else {
                        failureNum++;
                        message.append("<br/>" + "错误：第 " + (i + 2) + "公司代码： " + asset.getCompanyCode() + "，财务资产编码：" + asset.getFinancialAssetCode() + " 的资产更新失败");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + "错误：第 " + (i + 2) + "行出错";
                message.append(msg + e.getMessage());
                log.error(msg, e);
                throw new ServiceException("导入出错");
            }
        }

        if (!isUpdateSupport) {
            try {
                // 过滤已导入过的资产信息
                List<Asset> assets = assetList.stream()
                        .filter(x -> StringUtils.isNotEmpty(x.getAssetCode()))
                        .collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(assets)) {
                    syncAssetCodeToSAP(assets);
                }
            } catch (Exception e) {
                log.error("", e);
                throw new ServiceException("SAP同步出错：" + e.getMessage());
            }
        }
        if (!isUpdateSupport) {
            message.insert(0, "数据导入完成，共 " + assetList.size() + " 条，成功导入 " + successNum + " 条，出错 " + failureNum + " 条，详情如下：");
        } else {
            message.insert(0, "数据更新完成，共 " + assetList.size() + " 条，成功更新 " + successNum + " 条，出错 " + failureNum + " 条，详情如下：");
        }

        return message.toString();
    }

    /**
     * 检查Excel表格中必填字段是否有没填的，
     * 若有必填字段未填，则返回未填字段的字段中文名，否则返回null
     *
     * @param asset 资产对象
     * @return
     * @throws Exception
     * @author 80015306
     * @date 2022-09-09
     */
    private String checkRequiredFields(Asset asset) throws Exception {
        if (assetImportRequiredFieldDictDataList == null) {
            assetImportRequiredFieldDictDataList = sysDictDataMapper.selectDictDataByType(SysDictTypeEnum.ASSET_IMPORT_REQUIRED_FIELD.getValue());
        }
        Class<Asset> clazz = Asset.class;
        for (SysDictData dictData : assetImportRequiredFieldDictDataList) {
            String fieldName = dictData.getDictValue();
            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Object value = clazz.getMethod(getMethodName).invoke(asset);
            if (ObjectUtil.isEmpty(value)) {
                return dictData.getDictLabel();
            }
        }
        return null;
    }

    private String generateAssetCode(Asset asset) throws Exception {

        StringBuilder assetCode = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        DecimalFormat df = new DecimalFormat("0000");

        List<SysDictData> companyListDictDataList = sysDictDataMapper.selectDictDataByType(SysDictTypeEnum.COMPANY_LIST.getValue());
        List<SysDictData> manageDeptDictDataList = sysDictDataMapper.selectDictDataByType(SysDictTypeEnum.MANAGE_DEPT.getValue());
        List<SysDictData> assetCategoryDictDataList = sysDictDataMapper.selectDictDataByType(SysDictTypeEnum.ASSET_CATEGORY.getValue());

        String capitalizationDate = sdf.format(asset.getCapitalizationDate());
        String capitalizationDateCode = capitalizationDate.split("/")[0].substring(1);       /* 资本化日期对应编码 */

        String companyCode = companyListDictDataList.stream()                       /* 所属公司对应编码 */
                .filter(dataDict -> dataDict.getDictLabel().equals(asset.getCompanyName()))
                .map(SysDictData::getDictValue)
                .findFirst()
                .orElseThrow(() -> new Exception("找不到公司对应编码"));

        String manageDeptCode = manageDeptDictDataList.stream()                     /* 管理部门对应编码 */
                .filter(dataDict -> dataDict.getDictLabel().equals(asset.getManageDept()))
                .map(SysDictData::getDictValue)
                .findFirst()
                .orElseThrow(() -> new Exception("找不到管理部门对应编码"));

        String categoryCode = assetCategoryDictDataList.stream()                     /* 管理部门对应编码 */
                .filter(dataDict -> dataDict.getDictLabel().equals(asset.getCategory()))
                .map(SysDictData::getDictValue)
                .findFirst()
                .orElseThrow(() -> new Exception("找不到资产类别对应编码"));

        Map<String, Integer> yearAssetCountMap = new HashMap<>();

        if (!yearAssetCountMap.containsKey(capitalizationDateCode)) {
            QueryWrapper<Asset> wrapper = new QueryWrapper<>();
            wrapper.apply("asset_code" + " like {0}", "%" + capitalizationDateCode + "____");
            List<Asset> assetList = assetMapper.selectList(wrapper);
            List<String> assetCodeList = assetList.stream().map(Asset::getAssetCode).collect(Collectors.toList());
            if (assetCodeList == null || assetCodeList.size() == 0) {
                yearAssetCountMap.put(capitalizationDateCode, 1);
            } else {
                // 找到后四位最大的编号
                for (int i = 0; i < assetCodeList.size(); i++) {
                    String code = assetCodeList.get(i);
                    assetCodeList.set(i, code.substring(code.length() - 4));
                }
                Integer max = assetCodeList.stream().mapToInt(Integer::parseInt).max().orElse(0);
                yearAssetCountMap.put(capitalizationDateCode, max + 1);
            }
        } else {
            yearAssetCountMap.replace(capitalizationDateCode, yearAssetCountMap.get(capitalizationDateCode) + 1);
        }
        String serialNumber = df.format(yearAssetCountMap.get(capitalizationDateCode));

        assetCode.append(companyCode)
                .append(manageDeptCode)
                .append(categoryCode)
                .append(capitalizationDateCode)
                .append(serialNumber);

        return assetCode.toString();
    }

    /**
     * 将资产信息和保管人关联关系通过UIP同步到SAP
     */
    public void syncAssetCodeToSAP(List<Asset> assetList) throws Exception {

        JSONArray data = new JSONArray();
        for (Asset asset : assetList) {
            JSONObject obj = new JSONObject();
            obj.put("BUKRS", asset.getCompanyCode());
            obj.put("ANLN1", asset.getFinancialAssetCode());
            obj.put("ZNUM", asset.getAssetCode());
            data.add(obj);
        }

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        params.add("INBOUND", data);
        params.add("interfaceCode", UIPcodeEnum.pushSingleFdCodeToSAP.getCode());

        log.info("===导入如资产数据-新资产平台资产编号推送SAP：" + params);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(uipTransfer, params, String.class);

        String body = responseEntity.getBody();
        JSONObject responseBody = JSONObject.parseObject(body);
        log.info("===导入资产数据--新资产的平台资产编码同步到SAP，SAP响应：" + responseBody);

        StringBuilder message = new StringBuilder();
        JSONArray inbound = responseBody.getJSONArray("INBOUND");
        for (Object o : inbound) {
            JSONObject next = (JSONObject) o;
            String type = next.getString("TYPE");
            if (SAPRespType.ERROR.getType().equals(type)) {
                String msg = next.getString("MSG");
                message.append("<br/>").append(msg);
            }
        }
        if (message.length() > 0) {
            throw new ServiceException(message.toString());
        }

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
