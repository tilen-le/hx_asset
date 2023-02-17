package com.hexing.asset.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetManagementConfig;
import com.hexing.asset.domain.dto.AssetManagementConfigSearchDTO;
import com.hexing.asset.domain.dto.MaterialCategorySimpleDTO;
import com.hexing.asset.mapper.AssetManagementConfigMapper;
import com.hexing.asset.service.IAssetManagementConfigService;
import com.hexing.asset.utils.CodeUtil;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.impl.SysDeptServiceImpl;
import com.hexing.system.service.impl.SysUserServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资产管理配置Service业务层处理
 *
 * @author zxy
 * @date 2023-01-30
 */
@Service
public class AssetManagementConfigServiceImpl extends ServiceImpl<AssetManagementConfigMapper, AssetManagementConfig> implements IAssetManagementConfigService {
    @Autowired
    private AssetManagementConfigMapper assetManagementConfigMapper;
    @Autowired
    private IAssetManagementConfigService assetManagementConfigService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysDeptServiceImpl sysDeptService;

    /**
     * 查询资产管理配置
     *
     * @param id 资产管理配置主键
     * @return 资产管理配置
     */
    @Override
    public AssetManagementConfig selectAssetManagementConfigById(Long id) {
        return assetManagementConfigMapper.selectById(id);
    }

    /**
     * 根据资产类型查询资产配置信息
     *
     * @param asset 资产对象
     * @return
     */
    @Override
    public Asset selectAssetManagementConfigByCategoryInfo(Asset asset) {
        LambdaQueryWrapper<AssetManagementConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetManagementConfig::getAssetType, asset.getAssetType())
                .eq(AssetManagementConfig::getAssetCategory, asset.getAssetCategory())
                .apply("(find_in_set( {0} , asset_sub_category ))", asset.getAssetSubCategory())
                .eq(AssetManagementConfig::getCompany, asset.getCompany());
        List<AssetManagementConfig> assetManagementConfigList = assetManagementConfigMapper.selectList(wrapper);
        if (CollectionUtil.isNotEmpty(assetManagementConfigList)) {
            AssetManagementConfig config = assetManagementConfigList.get(0);
            SysUser user = sysUserService.getUserByUserName(config.getAssetManager());
            SysDept dept = sysDeptService.selectDeptById(user.getDeptId());
            asset.setAssetManager(user.getNickName())
                    .setAssetManagementDept(dept.getDeptName());
        }
        return asset;
    }

    /**
     * 查询资产管理配置列表
     *
     * @param searchDTO 资产管理配置
     * @return 资产管理配置
     */
    @Override
    public List<AssetManagementConfig> selectAssetManagementConfigList(AssetManagementConfigSearchDTO searchDTO) {
        LambdaQueryWrapper<AssetManagementConfig> wrapper = new LambdaQueryWrapper<>();
        List<String> assetSubCategory = searchDTO.getAssetSubCategory();
        List<String> assetManager = searchDTO.getAssetManager();
        List<String> company = searchDTO.getCompany();
        if (StringUtils.isNotBlank(searchDTO.getAssetType())) {
            wrapper.eq(AssetManagementConfig::getAssetType, searchDTO.getAssetType());
        }
        if (StringUtils.isNotBlank(searchDTO.getAssetCategory())) {
            wrapper.eq(AssetManagementConfig::getAssetCategory, searchDTO.getAssetCategory());
        }
        List<AssetManagementConfig> assetManagementConfigs = assetManagementConfigMapper.selectList(wrapper);

        if (CollectionUtil.isNotEmpty(assetSubCategory)) {
            assetManagementConfigs = getList(assetManagementConfigs, assetSubCategory, "assetSubCategory");
        }
        if (CollectionUtil.isNotEmpty(assetManager)) {
            assetManagementConfigs = getList(assetManagementConfigs, assetManager, "assetManager");
        }
        if (CollectionUtil.isNotEmpty(company)) {
            assetManagementConfigs = getList(assetManagementConfigs, company, "");
        }
        List<SysUser> sysUsers = sysUserService.selectUserList(new SysUser());
        List<SysDept> sysDept = sysDeptService.selectDeptList(new SysDept());
        for (AssetManagementConfig assetManagementConfig : assetManagementConfigs) {
            MaterialCategorySimpleDTO categorySimpleDTO = CodeUtil.getAssetTypeName(assetManagementConfig);
            assetManagementConfig.setAssetType(categorySimpleDTO.getAssetType());
            assetManagementConfig.setAssetCategory(categorySimpleDTO.getAssetCategory());
            assetManagementConfig.setAssetSubCategory(categorySimpleDTO.getAssetSubCategory());
            //获取部门id
            SysUser assetManagerUser = getUserByUserCode(sysUsers, assetManagementConfig.getAssetManager());
            SysUser financialManagerUser = getUserByUserCode(sysUsers, assetManagementConfig.getFinancialManager());
            //获取部门名称
            SysDept assetManagerDeptName = getDeptCompleteNameByDeptCode(sysDept, assetManagerUser.getDeptId());
            SysDept financialManagerDeptName = getDeptCompleteNameByDeptCode(sysDept, financialManagerUser.getDeptId());
            assetManagementConfig.setAssetManageDept(assetManagerDeptName.getCompleteName());
            assetManagementConfig.setFinancialManageDept(financialManagerDeptName.getCompleteName());
            //资产管理员
            assetManagementConfig.setAssetManager(assetManagerUser.getNickName());
            //账务管理员
            assetManagementConfig.setFinancialManager(financialManagerUser.getNickName());
        }

        return assetManagementConfigs;
    }

    private List getList(List<AssetManagementConfig> assetManagementConfigs, List<String> searchParam, String whichSearch) {
        List<AssetManagementConfig> list = new ArrayList<>();
        List<String> xlist = new ArrayList<>();
        String param = "";
        for (AssetManagementConfig x : assetManagementConfigs) {
            param = x.getCompany();
            if (whichSearch.equals("assetSubCategory")) {
                xlist = Arrays.asList(x.getAssetSubCategory().split(","));
                param = x.getAssetSubCategory();
            } else if (whichSearch.equals("assetManager")) {
                xlist = Arrays.asList(x.getAssetManager().split(","));
                param = x.getAssetManager();
            }
            Boolean isTureWhich = false;
            for (String s : searchParam) {
                if (xlist.contains(s)) {
                    isTureWhich = Boolean.TRUE;
                    continue;
                }
            }
            if (searchParam.contains(param) || isTureWhich) {
                list.add(x);
            }
        }
        return list;
    }

    private SysUser getUserByUserCode(List<SysUser> sysUsers, String userCode) {
        SysUser sysUser = sysUsers.stream().filter(x -> x.getUserName().equals(userCode)).findFirst().orElse(new SysUser());
        return sysUser;
    }

    private SysDept getDeptCompleteNameByDeptCode(List<SysDept> sysDept, Long deptCode) {
        SysDept dept = sysDept.stream().filter(x -> x.getDeptId().equals(deptCode)).findFirst().orElse(new SysDept());
        return dept;
    }

    private String getUserNameByUserCode(List<SysUser> sysUsers, String userCodes) {
        String userName = "";
        if (userCodes.contains(",")) {
            String[] split = userCodes.split(",");
            String inventoryUsersName = "";
            for (int i = 0; i < split.length; i++) {
                String trim = split[i].trim();
                userName = sysUsers.stream().filter(sysUser -> sysUser.getUserName().equals(trim)).map(SysUser::getNickName).findFirst().orElse(null);
                inventoryUsersName += userName + ",";
            }
            userName = inventoryUsersName.substring(0, inventoryUsersName.lastIndexOf(","));
        } else {
            userName = sysUsers.stream().filter(sysUser -> sysUser.getUserName().equals(userCodes)).map(SysUser::getNickName).findFirst().orElse(null);
        }
        return userName;
    }

    /**
     * 资产、财务管理员资产数据权限查询接口
     *
     * @param user 资产管理配置
     * @return 资产管理配置
     */
    @Override
    public List<AssetManagementConfig> listManagementConfig(String user) {
        LambdaQueryWrapper<AssetManagementConfig> wrapper = new LambdaQueryWrapper<>();
        List<AssetManagementConfig> assetManagementConfigs = new ArrayList<>();

        if (StringUtils.isNotBlank(user)) {
            wrapper.apply("(find_in_set( {0} , asset_manager ))", user).or()
                    .apply("(find_in_set( {0} , financial_manager ))", user);
            assetManagementConfigs = assetManagementConfigMapper.selectList(wrapper);
        }
        return assetManagementConfigs;
    }

    public List<AssetManagementConfig> selectManagementConfigListByAssetManager(String userName) {
        List<AssetManagementConfig> result = new ArrayList<>();

        LambdaQueryWrapper<AssetManagementConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetManagementConfig::getAssetManager, userName);
        List<AssetManagementConfig> assetManagementConfigList = assetManagementConfigMapper.selectList(wrapper);

        for (AssetManagementConfig managementConfig : assetManagementConfigList) {
            String subCategory = managementConfig.getAssetSubCategory();
            if (StringUtils.isNotEmpty(subCategory) && subCategory.contains(",")) {
                String[] subCategoryList = subCategory.split(",");
            }
            result.add(managementConfig);
        }

        return result;
    }

    /**
     * 新增资产管理配置
     *
     * @param assetManagementConfig 资产管理配置
     * @return 结果
     */
    @Override
    public int insertAssetManagementConfig(AssetManagementConfig assetManagementConfig) {
        assetManagementConfig.setCreateTime(DateUtils.getNowDate());
        assetManagementConfig.setUpdateTime(DateUtils.getNowDate());
        //创建人工号
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80010712";
        assetManagementConfig.setCreateBy(userCode);
        assetManagementConfig.setUpdateBy(userCode);

        return assetManagementConfigMapper.insert(assetManagementConfig);
    }

    /**
     * 修改资产管理配置
     *
     * @param assetManagementConfig 资产管理配置
     * @return 结果
     */
    @Override
    public int updateAssetManagementConfig(AssetManagementConfig assetManagementConfig) {
        assetManagementConfig.setUpdateTime(DateUtils.getNowDate());
        String userCode = SecurityUtils.getLoginUser().getUser().getUserName();
//        String userCode = "80015306";
        assetManagementConfig.setUpdateBy(userCode);

        return assetManagementConfigMapper.updateById(assetManagementConfig);
    }

    /**
     * 批量删除资产管理配置
     *
     * @param ids 需要删除的资产管理配置主键
     * @return 结果
     */
    @Override
    public int deleteAssetManagementConfigByIds(Long[] ids) {
        return assetManagementConfigMapper.delete(new LambdaQueryWrapper<AssetManagementConfig>().in(AssetManagementConfig::getId, ids));
    }

}
