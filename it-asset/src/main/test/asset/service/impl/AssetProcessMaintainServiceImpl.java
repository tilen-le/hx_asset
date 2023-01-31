package com.hexing.assetNew.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.assetNew.domain.AssetProcess;
import com.hexing.assetNew.service.IAssetProcessService;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexing.assetNew.mapper.AssetProcessMaintainMapper;
import com.hexing.assetNew.domain.AssetProcessMaintain;
import com.hexing.assetNew.service.IAssetProcessMaintainService;

/**
 * 资产维修流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessMaintainServiceImpl extends ServiceImpl<AssetProcessMaintainMapper, AssetProcessMaintain> implements IAssetProcessMaintainService
{
    @Autowired
    private AssetProcessMaintainMapper assetProcessMaintainMapper;
    @Autowired
    private IAssetProcessService assetProcessService;
    /**
     * 查询资产维修流程
     *
     * @param id 资产维修流程主键
     * @return 资产维修流程
     */
    @Override
    public AssetProcessMaintain selectAssetProcessMaintainById(Long id)
    {
        return assetProcessMaintainMapper.selectAssetProcessMaintainById(id);
    }

    /**
     * 查询资产维修流程列表
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 资产维修流程
     */
    @Override
    public List<AssetProcessMaintain> selectAssetProcessMaintainList(AssetProcessMaintain assetProcessMaintain)
    {
        LambdaQueryWrapper<AssetProcessMaintain> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(assetProcessMaintain.getUserCode())) {
            wrapper.eq(AssetProcessMaintain::getUserCode, assetProcessMaintain.getUserCode());
        }
        if (StringUtils.isNotBlank(assetProcessMaintain.getAssetCode())) {
            wrapper.eq(AssetProcessMaintain::getAssetCode, assetProcessMaintain.getAssetCode());
        }
        if (StringUtils.isNotBlank(assetProcessMaintain.getInstanceId())) {
            wrapper.eq(AssetProcessMaintain::getInstanceId, assetProcessMaintain.getInstanceId());
        }
        return assetProcessMaintainMapper.selectList(wrapper);
    }

    /**
     * 新增资产维修流程
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessMaintain(AssetProcessMaintain assetProcessMaintain)
    {
        return assetProcessMaintainMapper.insert(assetProcessMaintain);
    }

    /**
     * 修改资产维修流程
     *
     * @param assetProcessMaintain 资产维修流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessMaintain(AssetProcessMaintain assetProcessMaintain)
    {
        assetProcessMaintain.setUpdateTime(DateUtils.getNowDate());
        return assetProcessMaintainMapper.updateAssetProcessMaintain(assetProcessMaintain);
    }

    /**
     * 批量删除资产维修流程
     *
     * @param ids 需要删除的资产维修流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessMaintainByIds(Long[] ids)
    {
        return assetProcessMaintainMapper.deleteAssetProcessMaintainByIds(ids);
    }

    /**
     * 删除资产维修流程信息
     *
     * @param id 资产维修流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessMaintainById(Long id)
    {
        return assetProcessMaintainMapper.deleteAssetProcessMaintainById(id);
    }

    @Override
    public int maintainAssets(JSONObject params){
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

                AssetProcessMaintain entity = new AssetProcessMaintain();
                entity.setAssetCode(assetCode);
                entity.setProcessId(assetProcess.getId());
                entity.setUserCode(userCode);
                entity.setUserName(userName);
                entity.setFileInfo(fileInfo);
                entity.setInstanceId(instanceId);
                entity.setCreateTime(new Date());
                insertAssetProcessMaintain(entity);
            }
        }else {
            String fileInfoAdd = data.getJSONArray("fileInfoAdd").toString();
            fileInfoAdd =AssetProcessDisposalServiceImpl.downLoadFile(fileInfoAdd,instanceId);
            LambdaQueryWrapper<AssetProcessMaintain> w = new LambdaQueryWrapper<>();
            for (Object o : assetList) {
                String assetCode = JSONUtil.parseObj(o).getStr("assetCode");
                w.eq(AssetProcessMaintain::getAssetCode, assetCode).eq(AssetProcessMaintain::getInstanceId, instanceId);
                AssetProcessMaintain entity = getOne(w);
                entity.setUpdateTime(new Date());
                updateById(entity);
            }
        }
        return 1;
    }
}
