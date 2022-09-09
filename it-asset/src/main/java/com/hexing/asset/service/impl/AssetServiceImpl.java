package com.hexing.asset.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.framework.web.domain.server.Sys;
import com.hexing.system.mapper.SysDictDataMapper;
import com.hexing.system.service.ISysDictDataService;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetMapper;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.service.IAssetService;

import javax.swing.text.html.Option;

/**
 * 资产表Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements IAssetService
{

    // 数据字典类型值
    private static final String ASSET_IMPORT_REQUIRED_FIELD = "asset_import_required_field";
    private static final String COMPANY_LIST = "company_list";
    private static final String MANAGE_DEPT = "manage_dept";
    private static final String ASSET_CATEGORY = "asset_category";

    // 数据表字段名
    private static final String FINANCIAL_ASSET_CODE = "financial_asset_code";
    private static final String COMPANY_CODE = "company_code";

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    private List<SysDictData> assetImportRequiredFieldDictDataList = null;

    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    /**
     * 查询资产表
     *
     * @param assetId 资产表主键
     * @return 资产表
     */
    @Override
    public Asset selectAssetByAssetId(Long assetId)
    {
        return assetMapper.selectAssetByAssetId(assetId);
    }

    /**
     * 查询资产表列表
     *
     * @param asset 资产表
     * @return 资产表
     */
    @Override
    public List<Asset> selectAssetList()
    {
        QueryWrapper<Asset> wrapper = new QueryWrapper<>();
        return assetMapper.selectList(wrapper);
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
        return assetMapper.updateAsset(asset);
    }

    /**
     * 批量删除资产表
     *
     * @param assetIds 需要删除的资产表主键
     * @return 结果
     */
    @Override
    public int deleteAssetByAssetIds(Long[] assetIds)
    {
        return assetMapper.deleteAssetByAssetIds(assetIds);
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

    /**
     * TODO 资产信息导入
     *
     * @param assetList 资产信息列表
     * @param isUpdateSupport 是否存在则覆盖
     * @param operName 操作人姓名
     * @return
     */
    @Override
    public String importAsset(List<Asset> assetList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(assetList) || assetList.size() == 0) {
            throw new ServiceException("导入资产数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Asset asset : assetList) {
            try {
                String requiredFieldLeftUnfilled = checkRequiredFields(asset);
                if (requiredFieldLeftUnfilled != null) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、必填字段：" + requiredFieldLeftUnfilled + " 未填写");
                    continue;
                }
                QueryWrapper<Asset> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(FINANCIAL_ASSET_CODE, asset.getFinancialAssetCode())
                        .eq(COMPANY_CODE, asset.getCompanyCode());
                Asset a = assetMapper.selectOne(queryWrapper);
                if (a == null) {
                    String assetCode = generateAssetCode(asset);
                    asset.setAssetCode(assetCode);
                    asset.setCreateBy(operName);
                    asset.setCreateTime(new Date());
                    assetMapper.insert(asset);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、资产： " + asset.getAssetName() + " 导入成功，资产编码为：" + assetCode);
                } else if (isUpdateSupport) {
                    UpdateWrapper<Asset> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq(FINANCIAL_ASSET_CODE, asset.getFinancialAssetCode())
                            .eq(COMPANY_CODE, asset.getCompanyCode());
                    asset.setUpdateBy(operName);
                    asset.setUpdateTime(new Date());
                    assetMapper.update(asset, updateWrapper);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、平台资产编号为： " + asset.getAssetCode() + " 的资产更新成功");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、资产 " + asset.getAssetName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
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
            String value = (String) clazz.getMethod(getMethodName).invoke(asset);
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
            wrapper.apply("asset_code" + "like {0}", "%" + capitalizationDateCode + "____" );
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


}
