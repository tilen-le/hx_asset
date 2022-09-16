package com.hexing.asset.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.enums.UIPcodeEnum;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.service.IAssetService;
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
import org.apache.poi.ss.formula.functions.MatrixFunction;
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

import static com.hexing.common.utils.PageUtil.startPage;

/**
 * 资产表Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
@Slf4j
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements IAssetService
{

    // 数据字典类型值
    private static final String ASSET_IMPORT_REQUIRED_FIELD = "asset_import_required_field";
    private static final String COMPANY_LIST = "company_list";
    private static final String MANAGE_DEPT = "manage_dept";
    private static final String ASSET_CATEGORY = "asset_category";

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    private List<SysDictData> assetImportRequiredFieldDictDataList = null;

    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private SysDictDataMapper sysDictDataMapper;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysDeptServiceImpl sysDeptService;

    @Value("${uip.uipTransfer}")
    private String uipTransfer;

    /**
     * 根据平台资产编号查询资产
     * @param assetCode
     * @return
     */
    @Override
    public Asset selectAssetByAssetCode(String assetCode) {
        QueryWrapper<Asset> wrapper = new QueryWrapper<>();
        wrapper.eq("asset_code", assetCode);
        Asset asset = assetMapper.selectOne(wrapper);
        Map<String, SysUser> usernameNicknameMap = sysUserService.getUsernameUserObjMap();
        Map<String, String> deptIdDeptNameMap = sysDeptService.getDeptIdDeptNameMap();

        SysUser user = usernameNicknameMap.get(asset.getResponsiblePersonCode());
        asset.setResponsiblePersonName(user.getNickName());
        asset.setResponsiblePersonDept(deptIdDeptNameMap.get(user.getDeptId().toString()));

        return asset;
    }


    /**
     * 查询资产表
     *
     * @param assetId 资产表主键
     * @return 资产表
     */
    @Override
    public Asset selectAssetByAssetId(String assetId)
    {
        return assetMapper.selectById(assetId);
    }

    @Override
    public String getAssetsByAssetCodes(JSONObject params)
    {
        QueryWrapper<Asset> wrapper = new QueryWrapper<>();
        Asset asset =new Asset();
        wrapper.setEntity(asset);

        JSONObject R = new JSONObject();
        JSONObject result = new JSONObject();
        try {
            List<JSONObject> assets = new ArrayList<>();
            String assetCode = params.getString("assetCode");
            if (StringUtils.isBlank(assetCode)) {
                result.put("code", "500");
                result.put("msg", "平台资产编号为空");
                return result.toString();
            }
            // 资产编号1;资产编号2;... 的情况
            if (assetCode.contains(";")) {
                String[] assetCodes = assetCode.split(";");
                assets = new ArrayList<>();
                for (String code : assetCodes) {
                    String fdDeptDescription = params.getString("manageDept");
                    if (StringUtils.isNotBlank(fdDeptDescription)) {
                        wrapper.getEntity().setManageDept(fdDeptDescription);
                    }
                    wrapper.getEntity().setAssetCode(code);
                    JSONObject jsonObject = setNewAsset(assetMapper.selectOne(wrapper));
                    assets.add(jsonObject);
                }
                result.put("assets", assets);
                R.put("result", result);
                return R.toString();
            }else{
                String manageDept = params.getString("manageDept");
                if (StringUtils.isNotBlank(manageDept)) {
                    wrapper.getEntity().setManageDept(manageDept);
                }
                wrapper.getEntity().setAssetCode(assetCode);
                JSONObject jsonObject = setNewAsset(assetMapper.selectOne(wrapper));
                assets.add(jsonObject);
                result.put("assets", assets);
                R.put("result", result);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return R.toString();

    }


    /**
     * 根据人员查询资产
     */
    @Override
    public List<Asset> selectAssetByResponsiblePerson(String responsiblePersonCode,String manageDept)
    {
        QueryWrapper<Asset> wrapper = new QueryWrapper<>();
        Asset asset =new Asset();
        wrapper.setEntity(asset);
        wrapper.getEntity().setResponsiblePersonCode(responsiblePersonCode);
        if (StringUtils.isNotBlank(manageDept)){
            wrapper.getEntity().setManageDept(manageDept);
        }
        return assetMapper.selectList(wrapper);
    }

    public JSONObject setNewAsset(Asset asset)
    {
        JSONObject result =new JSONObject();
        result.put("assetCode",asset.getAssetCode());
        result.put("fixedAsset",asset.getAssetName());
        result.put("factoryCode",asset.getFactoryNo());
        result.put("assetStatus",asset.getAssetStatus());
        result.put("location",asset.getLocation());
        result.put("manageDept",asset.getManageDept());
        result.put("brand",asset.getBrand());
        result.put("fdStandard",asset.getStandard());
        result.put("financialAssetCode",asset.getFinancialAssetCode());
        result.put("initialAssetValue",asset.getTotalValue());
        result.put("netAssetValue",asset.getNetWorth());
        result.put("purchaseTime",asset.getBuyDate());
        result.put("companyCode",asset.getCompanyName());
        String estimatedUsefulLife = "";
        if (asset.getCanUseYears() != null && asset.getCanUseYears()>0){
            estimatedUsefulLife =  asset.getCanUseYears().toString()+"年";
        }
        if (asset.getCanUseMonths() != null && asset.getCanUseMonths()>0){
            estimatedUsefulLife = estimatedUsefulLife + asset.getCanUseMonths().toString()+"月";
        }
        result.put("estimatedUsefulLife",estimatedUsefulLife);
        result.put("usageScenario",asset.getUsageScenario());
        result.put("fdResponsiblePersonCode",asset.getResponsiblePersonCode());
        return result;

    }
    @Override
    public JSONObject getAssets(JSONObject params)
    {
        QueryWrapper<Asset> wrapper = new QueryWrapper<>();
        Asset asset =new Asset();
        wrapper.setEntity(asset);

        JSONObject R = new JSONObject();
        JSONObject result = new JSONObject();
        try {
            String userId = params.getString("userId");
            if (StringUtils.isBlank(userId)) {
                result.put("code", "500");
                result.put("msg", "保管人工号为空");
                return result;
            }
            // 查询保管人信息
            SysUser sysUser = sysUserService.selectUserByUserName(userId);
            if (sysUser == null) {
                result.put("code", "500");
                result.put("msg", "未查询到此保管人");
                return result;
            }
            R.put("userId",userId);
            // 查询保管人所属部门
            if (sysUser.getDeptId()==null) {
                result.put("code", "500");
                result.put("msg", "未查询到保管部门");
                return result;
            }
            SysDept sysDept = sysDeptService.selectDeptById(sysUser.getDeptId());
            R.put("department",sysDept.getDeptName());
            String manageDept = params.getString("manageDept");
            List<Asset> assets = selectAssetByResponsiblePerson(userId,manageDept);
            List<JSONObject> assetsList =new ArrayList<>();
            if (assets!=null){
                for (int i = 0; i < assets.size(); i++) {
                    assetsList.add(setNewAsset(assets.get(i)));
                }
            }
            R.put("assets",assetsList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return R;

    }

    /**
     * 查询资产表列表
     *
     * @param
     * @return 资产表
     */
    @Override
    public List<Asset> selectAssetList(Asset asset)
    {
        Map<String, SysUser> usernameNicknameMap = sysUserService.getUsernameUserObjMap();
        Map<String, String> deptIdDeptNameMap = sysDeptService.getDeptIdDeptNameMap();

        startPage();
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
        if (StringUtils.isNotBlank(asset.getCostCenter())) {
            wrapper.eq(Asset::getCostCenter, asset.getCostCenter());
        }
        List<Asset> assetList = assetMapper.selectList(wrapper);

        if (CollectionUtil.isNotEmpty(assetList)) {
            for (Asset a : assetList) {
                SysUser user = usernameNicknameMap.get(a.getResponsiblePersonCode());
                a.setResponsiblePersonName(user.getNickName());
                a.setResponsiblePersonDept(deptIdDeptNameMap.get(user.getDeptId().toString()));
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
    public int insertAsset(Asset asset)
    {
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
    public int updateAsset(Asset asset)
    {
        asset.setUpdateTime(DateUtils.getNowDate());
        return assetMapper.updateById(asset);
    }

    /**
     * 删除资产表信息
     *
     * @param assetId 资产表主键
     * @return 结果
     */
    @Override
    public int deleteAssetByAssetId(Long assetId)
    {
        return assetMapper.deleteAssetByAssetId(assetId);
    }

    @Override
    public int deleteAssetByAssetCodes(List<String> assetCodes) {
        return assetMapper.delete(new QueryWrapper<Asset>().in("asset_code", assetCodes));
    }

    /**
     * 资产信息导入
     *
     * @param assetList 资产信息列表
     * @param isUpdateSupport 是否存在则覆盖
     * @param operName 操作人姓名
     * @return
     */
    @Override
    @Transactional
    public String importAsset(List<Asset> assetList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(assetList) || assetList.size() == 0) {
            throw new ServiceException("导入资产数据不能为空！");
        }
        int totalNum = 0;
        int successNum = 0;
        int failureNum = 0;
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < assetList.size(); i++) {
            Asset asset = assetList.get(i);
            try {
                String requiredFieldLeftUnfilled = checkRequiredFields(asset);
                if (requiredFieldLeftUnfilled != null) {
                    totalNum++;
                    failureNum++;
                    message.append("<br/><font color=\"red\">" + totalNum + "、" + "错误：第 " + (i + 2) + " 行的必填字段：" + requiredFieldLeftUnfilled + " 未填写</font>");
                    continue;
                }
                LambdaQueryWrapper<Asset> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Asset::getFinancialAssetCode, asset.getFinancialAssetCode())
                        .eq(Asset::getCompanyCode, asset.getCompanyCode());
                Asset a = assetMapper.selectOne(queryWrapper);
                if (a == null) {
                    String assetCode = generateAssetCode(asset);
                    asset.setAssetCode(assetCode);
                    asset.setCreateBy(operName);
                    asset.setCreateTime(new Date());
                    assetMapper.insert(asset);
                    totalNum++;
                    successNum++;
//                    message.append("<br/>" + totalNum + "、资产：" + asset.getAssetName() + " 导入成功，生成的资产编码为：" + assetCode);
                } else if (isUpdateSupport) {
                    LambdaUpdateWrapper<Asset> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(Asset::getFinancialAssetCode, asset.getFinancialAssetCode())
                            .eq(Asset::getCompanyCode, asset.getCompanyCode());
                    asset.setUpdateBy(operName);
                    asset.setUpdateTime(new Date());
                    int update = assetMapper.update(asset, updateWrapper);
                    if (update > 0) {
                        totalNum++;
                        successNum++;
//                        message.append("<br/>" + totalNum + "、公司代码： " + asset.getCompanyCode() + "，财务资产编码：" + asset.getFinancialAssetCode() + " 的资产更新成功");
                    } else {
                        totalNum++;
                        failureNum++;
                        message.append("<br/>" + totalNum + "、公司代码： " + asset.getCompanyCode() + "，财务资产编码：" + asset.getFinancialAssetCode() + " 的资产更新失败");
                    }
                }

            } catch (Exception e) {
                totalNum++;
                failureNum++;
                String msg = "<br/>" + totalNum + "、资产 " + asset.getAssetName() + " 导入失败：";
                message.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }

        if (!isUpdateSupport) {
            try {
                JSONObject sapResponse = syncAssetCodeToSAP(assetList);
                System.out.println(sapResponse);
            } catch (Exception e) {
                throw new ServiceException("SAP同步出错");
            }
        }

        if (failureNum > 0 && successNum == 0) {
            message.insert(0, "很抱歉，导入失败！错误如下：");
            throw new ServiceException(message.toString());
        }
        else {
            message.insert(0, "数据导入成功！共 "+totalNum+" 条，成功导入 " + successNum + " 条，出错 "+failureNum +" 条，数据如下：");
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
            assetImportRequiredFieldDictDataList = sysDictDataMapper.selectDictDataByType(ASSET_IMPORT_REQUIRED_FIELD);
        }
        Class<Asset> clazz = Asset.class;
        for (SysDictData dictData : assetImportRequiredFieldDictDataList) {
            String fieldName = dictData.getDictValue();
            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String value = clazz.getMethod(getMethodName).invoke(asset).toString();
            if (StringUtils.isEmpty(value)) {
                return dictData.getDictLabel();
            }
        }
        return null;
    }

    private String generateAssetCode(Asset asset) throws Exception {

        StringBuffer assetCode = new StringBuffer();

        DecimalFormat df = new DecimalFormat("0000");

        List<SysDictData> companyListDictDataList = sysDictDataMapper.selectDictDataByType(COMPANY_LIST);
        List<SysDictData> manageDeptDictDataList = sysDictDataMapper.selectDictDataByType(MANAGE_DEPT);
        List<SysDictData> assetCategoryDictDataList = sysDictDataMapper.selectDictDataByType(ASSET_CATEGORY);

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
            wrapper.apply("asset_code" + " like {0}", "%" + capitalizationDateCode + "____" );
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
            yearAssetCountMap.replace(capitalizationDateCode,yearAssetCountMap.get(capitalizationDateCode) + 1);
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
    public JSONObject syncAssetCodeToSAP(List<Asset> assetList) throws Exception {

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

        return responseBody;
    }

}
