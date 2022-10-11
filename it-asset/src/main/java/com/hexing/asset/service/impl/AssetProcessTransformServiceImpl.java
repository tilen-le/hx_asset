package com.hexing.asset.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.AssetProcessDisposal;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessTransformMapper;
import com.hexing.asset.domain.AssetProcessTransform;
import com.hexing.asset.service.IAssetProcessTransformService;

/**
 * 资产改造流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-20
 */
@Service
public class AssetProcessTransformServiceImpl extends ServiceImpl<AssetProcessTransformMapper, AssetProcessTransform> implements IAssetProcessTransformService
{
    @Autowired
    private AssetProcessTransformMapper assetProcessTransformMapper;
    @Autowired
    private IAssetProcessService assetProcessService;
    /**
     * 查询资产改造流程
     *
     * @param id 资产改造流程主键
     * @return 资产改造流程
     */
    @Override
    public AssetProcessTransform selectAssetProcessTransformById(Long id)
    {
        return assetProcessTransformMapper.selectAssetProcessTransformById(id);
    }

    /**
     * 查询资产改造流程列表
     *
     * @param assetProcessTransform 资产改造流程
     * @return 资产改造流程
     */
    @Override
    public List<AssetProcessTransform> selectAssetProcessTransformList(AssetProcessTransform assetProcessTransform)
    {
        LambdaQueryWrapper<AssetProcessTransform> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(assetProcessTransform.getUserCode())) {
            wrapper.eq(AssetProcessTransform::getUserCode, assetProcessTransform.getUserCode());
        }
        if (StringUtils.isNotBlank(assetProcessTransform.getAssetCode())) {
            wrapper.eq(AssetProcessTransform::getAssetCode, assetProcessTransform.getAssetCode());
        }
        if (StringUtils.isNotBlank(assetProcessTransform.getInstanceId())) {
            wrapper.eq(AssetProcessTransform::getInstanceId, assetProcessTransform.getInstanceId());
        }
        if (StringUtils.isNotBlank(assetProcessTransform.getStatus())) {
            wrapper.eq(AssetProcessTransform::getStatus, assetProcessTransform.getStatus());
        }
        return assetProcessTransformMapper.selectList(wrapper);
    }

    /**
     * 新增资产改造流程
     *
     * @param assetProcessTransform 资产改造流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessTransform(AssetProcessTransform assetProcessTransform)
    {
        return assetProcessTransformMapper.insert(assetProcessTransform);
    }

    /**
     * 修改资产改造流程
     *
     * @param assetProcessTransform 资产改造流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessTransform(AssetProcessTransform assetProcessTransform)
    {
        assetProcessTransform.setUpdateTime(DateUtils.getNowDate());
        return assetProcessTransformMapper.updateAssetProcessTransform(assetProcessTransform);
    }

    /**
     * 批量删除资产改造流程
     *
     * @param ids 需要删除的资产改造流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessTransformByIds(Long[] ids)
    {
        return assetProcessTransformMapper.deleteAssetProcessTransformByIds(ids);
    }

    /**
     * 删除资产改造流程信息
     *
     * @param id 资产改造流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessTransformById(Long id)
    {
        return assetProcessTransformMapper.deleteAssetProcessTransformById(id);
    }

    @Override
    public int transformAssets(JSONObject params){
        JSONObject data = params.getObject("data", JSONObject.class);
        String instanceId = data.getString("instanceId");
        String userCode = data.getString("userCode");
        String userName = data.getString("userName");
        String processType = data.getString("processType");
        JSONArray assetList = data.getJSONArray("assets");
        if (data.containsKey("fileInfo")){
            String fileInfo = data.getJSONArray("fileInfo").toString();
            fileInfo = AssetProcessDisposalServiceImpl.downLoadFile(fileInfo,instanceId);
            for (Object o : assetList) {
                String assetCode = JSONUtil.parseObj(o).getStr("assetCode");
                AssetProcess assetProcess = new AssetProcess();
                assetProcess.setAssetCode(assetCode);
                assetProcess.setUserCode(userCode);
                assetProcess.setUserName(userName);
                assetProcess.setProcessType(processType);
                assetProcess.setCreateTime(new Date());
                assetProcessService.insertAssetProcess(assetProcess);

                AssetProcessTransform entity = new AssetProcessTransform();
                entity.setAssetCode(assetCode);
                entity.setProcessId(assetProcess.getId());
                entity.setUserCode(userCode);
                entity.setUserName(userName);
                entity.setFileInfo(fileInfo);
                entity.setStatus(DingTalkAssetProcessType.PROCESS_STATUS_UNCOMPLETED.getCode());
                entity.setInstanceId(instanceId);
                entity.setCreateTime(new Date());
                insertAssetProcessTransform(entity);
            }
        }else {
            String fileInfoAdd = data.getJSONArray("fileInfoAdd").toString();
            fileInfoAdd =AssetProcessDisposalServiceImpl.downLoadFile(fileInfoAdd,instanceId);
            LambdaQueryWrapper<AssetProcessTransform> w = new LambdaQueryWrapper<>();
            for (Object o : assetList) {
                String assetCode = JSONUtil.parseObj(o).getStr("assetCode");
                w.eq(AssetProcessTransform::getAssetCode, assetCode).eq(AssetProcessTransform::getInstanceId, instanceId);
                AssetProcessTransform entity = getOne(w);
                entity.setFileInfoAdd(fileInfoAdd);
                entity.setStatus(DingTalkAssetProcessType.PROCESS_STATUS_COMPLETED.getCode());
                entity.setUpdateTime(new Date());
                updateById(entity);
            }
        }
        return 1;
    }

}
