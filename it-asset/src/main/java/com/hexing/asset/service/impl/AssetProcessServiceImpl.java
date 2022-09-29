package com.hexing.asset.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetInventoryTask;
import com.hexing.asset.domain.dto.StatisQueryParam;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.ISysDictDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessMapper;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.service.IAssetProcessService;

/**
 * 流程总Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessServiceImpl extends ServiceImpl<AssetProcessMapper, AssetProcess> implements IAssetProcessService
{
    @Autowired
    private AssetProcessMapper assetProcessMapper;
    @Autowired
    private ISysDictDataService sysDictDataService;
    /**
     * 查询流程总
     *
     * @param id 流程总主键
     * @return 流程总
     */
    @Override
    public AssetProcess selectAssetProcessById(Long id)
    {
        return assetProcessMapper.selectById(id);
    }

    /**
     * 查询流程总列表
     *
     * @param assetProcess 流程总
     * @return 流程总
     */
    @Override
    public List<AssetProcess> selectAssetProcessList(AssetProcess assetProcess)
    {
        LambdaQueryWrapper<AssetProcess> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(assetProcess.getProcessType())) {
            wrapper.eq(AssetProcess::getProcessType, assetProcess.getProcessType());
        }
        if (StringUtils.isNotBlank(assetProcess.getAssetCode())) {
            wrapper.eq(AssetProcess::getAssetCode, assetProcess.getAssetCode());
        }
        if (StringUtils.isNotBlank(assetProcess.getUserCode())) {
            wrapper.eq(AssetProcess::getUserCode, assetProcess.getUserCode());
        }
        List<SysDictData> dictDataList = sysDictDataService.selectDictDataByType("dingtalk_asset_process_type");
        List<String> processTypeList = dictDataList.stream().map(SysDictData::getDictValue).collect(Collectors.toList());
        wrapper.in(AssetProcess::getProcessType, processTypeList);
        return assetProcessMapper.selectList(wrapper);
    }

    /**
     * 新增流程总
     *
     * @param assetProcess 流程总
     * @return 结果
     */
    @Override
    public int insertAssetProcess(AssetProcess assetProcess)
    {
        return assetProcessMapper.insert(assetProcess);
    }

    /**
     * 修改流程总
     *
     * @param assetProcess 流程总
     * @return 结果
     */
    @Override
    public int updateAssetProcess(AssetProcess assetProcess)
    {
        assetProcess.setUpdateTime(DateUtils.getNowDate());
        return assetProcessMapper.updateAssetProcess(assetProcess);
    }

    /**
     * 批量删除流程总
     *
     * @param ids 需要删除的流程总主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessByIds(Long[] ids)
    {
        return assetProcessMapper.deleteAssetProcessByIds(ids);
    }

    /**
     * 删除流程总信息
     *
     * @param id 流程总主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessById(Long id)
    {
        return assetProcessMapper.deleteAssetProcessById(id);
    }

    @Override
    public List<AssetProcess> queryAssetProcessForStatisticByType(String processType, StatisQueryParam params, List<Asset> assetList) {
        LambdaQueryWrapper<AssetProcess> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetProcess::getProcessType, processType);
        wrapper.ge(AssetProcess::getCreateTime, params.getStartDate())
                .le(AssetProcess::getCreateTime, params.getEndDate());
        wrapper.in(AssetProcess::getAssetCode,
                assetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
        return this.list(wrapper);
    }
}
