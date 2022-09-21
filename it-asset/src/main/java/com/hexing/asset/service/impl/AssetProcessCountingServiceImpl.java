package com.hexing.asset.service.impl;

import java.util.*;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.vo.AssetProcessCountingVO;
import com.hexing.asset.enums.AssetCountingStatus;
import com.hexing.asset.service.IAssetService;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.core.page.PageDomain;
import com.hexing.common.core.page.TableSupport;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.mapper.SysUserMapper;
import com.hexing.system.service.ISysDeptService;
import com.hexing.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessCountingMapper;
import com.hexing.asset.domain.AssetProcessCounting;
import com.hexing.asset.service.IAssetProcessCountingService;
import springfox.documentation.spring.web.json.Json;

import static com.hexing.common.utils.PageUtil.startPage;

/**
 * 资产盘点流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessCountingServiceImpl extends ServiceImpl<AssetProcessCountingMapper, AssetProcessCounting> implements IAssetProcessCountingService
{
    @Autowired
    private AssetProcessCountingMapper assetProcessCountingMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private ISysDeptService deptService;

    /**
     * 查询资产盘点流程
     *
     * @param id 资产盘点流程主键
     * @return 资产盘点流程
     */
    @Override
    public AssetProcessCounting selectAssetProcessCountingById(Long id)
    {
        return assetProcessCountingMapper.selectAssetProcessCountingById(id);
    }

    /**
     * 查询资产盘点流程列表
     *
     * @param assetProcessCounting 资产盘点流程
     * @return 资产盘点流程
     */
    @Override
    public List<AssetProcessCountingVO> selectAssetProcessCountingList(AssetProcessCounting assetProcessCounting)
    {
        // 筛选条件
        LambdaQueryWrapper<AssetProcessCounting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetProcessCounting::getTaskCode, assetProcessCounting.getTaskCode());
        if (StringUtils.isNotBlank(assetProcessCounting.getAssetCode())) {
            wrapper.like(AssetProcessCounting::getAssetCode, assetProcessCounting.getAssetCode());
        }
        if (StringUtils.isNotBlank(assetProcessCounting.getUserCode())) {
//            String userNickName = assetProcessCounting.getUserNickName();
//            List<SysUser> userList = sysUserService.getUserByNickName(userNickName);
//            if (CollectionUtil.isNotEmpty(userList)) {
//                wrapper.in(AssetProcessCounting::getUserCode, userList.stream().map(SysUser::getUserName));
//            } else {
//                return Collections.emptyList();
//            }
            wrapper.eq(AssetProcessCounting::getUserCode, assetProcessCounting.getUserCode());
        }
        if (StringUtils.isNotBlank(assetProcessCounting.getCountingStatus())) {
            wrapper.eq(AssetProcessCounting::getCountingStatus, assetProcessCounting.getCountingStatus());
        }
        List<AssetProcessCounting> assetProcessCountingList = assetProcessCountingMapper.selectList(wrapper);
        List<AssetProcessCountingVO> assetProcessCountingVOList = new ArrayList<>();
        for (AssetProcessCounting obj : assetProcessCountingList) {
            Asset asset = assetService.selectAssetByAssetCode(obj.getAssetCode());
            SysUser responsiblePerson = sysUserService.getUserByUserName(asset.getResponsiblePersonCode());
            String inventoryPerson = "";
            if (StringUtils.isNotBlank(obj.getUserCode())) {
                SysUser userByUserName = sysUserService.getUserByUserName(obj.getUserCode());
                if (Objects.nonNull(userByUserName)) {
                    inventoryPerson = userByUserName.getNickName();
                }
            }
            SysDept dept = deptService.selectDeptById(responsiblePerson.getDeptId());
            AssetProcessCountingVO vo = new AssetProcessCountingVO().setUserCode(obj.getUserCode())
                    .setUserNickName(inventoryPerson)
                    .setCompanyName(asset.getCompanyName())
                    .setAssetCode(asset.getAssetCode())
                    .setAssetName(asset.getAssetName())
                    .setFactoryNo(asset.getFactoryNo())
                    .setStandard(asset.getStandard())
                    .setUsageScenario(asset.getUsageScenario())
                    .setManageDept(asset.getManageDept())
                    .setResponsiblePersonName(responsiblePerson.getNickName())
                    .setResponsiblePersonCode(responsiblePerson.getUserName())
                    .setResponsiblePersonDept(dept.getDeptName())
                    .setLocation(asset.getLocation())
                    .setCountingTime(obj.getCountingTime())
                    .setCountingStatus(obj.getCountingStatus())
                    .setComment(obj.getComment());
            assetProcessCountingVOList.add(vo);
        }
        return assetProcessCountingVOList;
    }


    @Override
    public JSONObject countingStatusCount(String taskCode) {
        JSONObject result = new JSONObject();

        LambdaQueryWrapper<AssetProcessCounting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetProcessCounting::getTaskCode, taskCode);
        List<AssetProcessCounting> assetProcessCountingList = assetProcessCountingMapper.selectList(wrapper);

        int total = 0;
        int notCounted = 0;
        int counted = 0;
        int abnormal = 0;
        if (CollectionUtil.isNotEmpty(assetProcessCountingList)) {
            total = assetProcessCountingList.size();
            for (AssetProcessCounting obj : assetProcessCountingList) {
                String status = obj.getCountingStatus();
                if (AssetCountingStatus.NOT_COUNTED.getStatus().equals(status)) {
                    notCounted++;
                }
                if (AssetCountingStatus.COUNTED.getStatus().equals(status)) {
                    counted++;
                }
                if (AssetCountingStatus.ABNORMAL.getStatus().equals(status)) {
                    abnormal++;
                }
            }
        }
        counted += abnormal;

        result.put("total", total);
        result.put("notCounted", notCounted);
        result.put("counted", counted);
        result.put("abnormal", abnormal);

        return result;
    }


    /**
     * 新增资产盘点流程
     *
     * @return 结果
     */
    @Override
    public int insertAssetProcessCounting(AssetProcessCounting entity)
    {
       return assetProcessCountingMapper.insert(entity);
    }

    /**
     * 修改资产盘点流程
     *
     * @param assetProcessCounting 资产盘点流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessCounting(AssetProcessCounting assetProcessCounting)
    {
        assetProcessCounting.setUpdateTime(DateUtils.getNowDate());
        return assetProcessCountingMapper.updateAssetProcessCounting(assetProcessCounting);
    }

    /**
     * 批量删除资产盘点流程
     *
     * @param ids 需要删除的资产盘点流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessCountingByIds(Long[] ids)
    {
        return assetProcessCountingMapper.deleteAssetProcessCountingByIds(ids);
    }

    /**
     * 删除资产盘点流程信息
     *
     * @param id 资产盘点流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessCountingById(Long id)
    {
        return assetProcessCountingMapper.deleteAssetProcessCountingById(id);
    }

}
