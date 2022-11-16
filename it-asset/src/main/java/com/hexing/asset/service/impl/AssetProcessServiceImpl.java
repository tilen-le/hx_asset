package com.hexing.asset.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.assetnew.domain.Asset;
import com.hexing.asset.domain.dto.StatisQueryParam;
import com.hexing.asset.domain.vo.AssetLifeCycleVO;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.assetnew.service.IAssetService;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.ISysDictDataService;
import com.hexing.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessMapper;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.service.IAssetProcessService;

import static com.hexing.common.utils.PageUtil.startPage;

/**
 * 流程总Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessServiceImpl extends ServiceImpl<AssetProcessMapper, AssetProcess> implements IAssetProcessService {
    @Autowired
    private AssetProcessMapper assetProcessMapper;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private ISysDictDataService sysDictDataService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询流程总
     *
     * @param id 流程总主键
     * @return 流程总
     */
    @Override
    public AssetProcess selectAssetProcessById(Long id) {
        return assetProcessMapper.selectById(id);
    }

    /**
     * 查询流程总列表
     *
     * @param assetProcess 流程总
     * @return 流程总
     */
    @Override
    public List<AssetProcess> selectAssetProcessList(AssetProcess assetProcess) {
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
        startPage();
        return assetProcessMapper.selectList(wrapper);
    }

    /**
     * 新增流程总
     *
     * @param assetProcess 流程总
     * @return 结果
     */
    @Override
    public int insertAssetProcess(AssetProcess assetProcess) {
        return assetProcessMapper.insert(assetProcess);
    }

    /**
     * 修改流程总
     *
     * @param assetProcess 流程总
     * @return 结果
     */
    @Override
    public int updateAssetProcess(AssetProcess assetProcess) {
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
    public int deleteAssetProcessByIds(Long[] ids) {
        return assetProcessMapper.deleteAssetProcessByIds(ids);
    }

    /**
     * 删除流程总信息
     *
     * @param id 流程总主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessById(Long id) {
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

    /**
     * 获取资产的生命周期
     *
     * @param assetCode 平台资产编号
     * @return
     */
    @Override
    public AssetLifeCycleVO getAssetLifeCycle(String assetCode) {
        LambdaQueryWrapper<AssetProcess> wrapper = new LambdaQueryWrapper<>();
        // 查询资产的所有流程 按照创建时间进行升序排序
        wrapper.eq(AssetProcess::getAssetCode, assetCode)
                .orderByAsc(AssetProcess::getCreateTime);
        List<AssetProcess> assetProcessList = this.list(wrapper);

        AssetLifeCycleVO vo = new AssetLifeCycleVO();

        Asset asset = assetService.getOne(new LambdaQueryWrapper<Asset>().eq(Asset::getAssetCode, assetCode));

        AssetProcess importNode = new AssetProcess();
        importNode.setProcessType("000");
        importNode.setCreateTime(asset.getCreateTime());
        importNode.setUserCode(asset.getCreateBy());
        assetProcessList.add(0, importNode);

        if (CollectionUtil.isNotEmpty(assetProcessList)) {
            for (AssetProcess assetProcess : assetProcessList) {
                SysUser user = sysUserService.getUserByUserName(assetProcess.getUserCode());
                assetProcess.setUserName(user.getNickName());
            }
        }

        vo.setAssetStatus(asset.getAssetStatus());

        vo.setAssetProcessList(assetProcessList);

        vo.setMaintainCount(CollectionUtil.isNotEmpty(assetProcessList) ? assetProcessList.stream()
                .filter(e -> DingTalkAssetProcessType.PROCESS_MAINTAIN.getCode().equals(e.getProcessType()))
                .count() : 0L);
        vo.setTransformCount(CollectionUtil.isNotEmpty(assetProcessList) ? assetProcessList.stream()
                .filter(e -> DingTalkAssetProcessType.PROCESS_TRANSFORM.getCode().equals(e.getProcessType()))
                .count() : 0L);
        vo.setSellOutCount(CollectionUtil.isNotEmpty(assetProcessList) ? assetProcessList.stream()
                .filter(e -> DingTalkAssetProcessType.PROCESS_SALE_OUT.getCode().equals(e.getProcessType()))
                .count() : 0L);


        return vo;
    }

}
