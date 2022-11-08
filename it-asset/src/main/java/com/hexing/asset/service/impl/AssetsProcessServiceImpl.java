package com.hexing.asset.service.impl;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.AssetsProcess;
import com.hexing.asset.mapper.AssetsProcessMapper;
import com.hexing.asset.service.IAssetProcessFieldService;
import com.hexing.asset.service.IAssetProcessVariableService;
import com.hexing.asset.service.IAssetsProcessService;
import com.hexing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资产流程Service业务层处理
 *
 * @author zxy
 * @date 2022-11-04
 */
@Service
public class AssetsProcessServiceImpl extends ServiceImpl<AssetsProcessMapper, AssetsProcess> implements IAssetsProcessService
{
    @Autowired
    private AssetsProcessMapper processMapper;
    @Autowired
    private IAssetProcessVariableService variableService;
    @Autowired
    private IAssetProcessFieldService fieldService;

    /**
     * 查询资产流程
     *
     * @param id 资产流程主键
     * @return 资产流程
     */
    @Override
    public AssetsProcess selectAssetsProcessById(Long id)
    {
        return processMapper.selectAssetsProcessById(id);
    }

    /**
     *
     * @param processType 流程类型
     * @param params 查询参数
     * @return
     */
    public AssetsProcess getOne(String processType, Map<String, Object> params) {

        // 从字典表中查询流程对应的编码

        //


//        processMapper.selectProcessWithCondition(params);

        return null;
    }

    /**
     * 查询资产流程列表
     *
     * @param process 资产流程
     * @return 资产流程
     */
    @Override
    public List<AssetsProcess> selectAssetsProcessList(AssetsProcess process)
    {
//        LambdaQueryWrapper<AssetsProcess> processWrapper = new LambdaQueryWrapper<>();
//        if (StringUtils.isNotBlank(process.getProcessType())) {
//            processWrapper.eq(AssetsProcess::getProcessType, process.getProcessType());
//        }
//        List<AssetsProcess> processList = list(processWrapper);
//
//        LambdaQueryWrapper<AssetProcessVariable> variableWrapper = new LambdaQueryWrapper<>();
//
//        variableWrapper.in(AssetProcessVariable::getProcessId,
//                processList.stream().map(AssetsProcess::getId).collect(Collectors.toList()));
//        List<AssetProcessVariable> variableList = variableService.list(variableWrapper);





        return null;
    }

    /**
     * 新增资产流程
     *
     * @param assetsProcess 资产流程
     * @return 结果
     */
    @Override
    public int insertAssetsProcess(AssetsProcess assetsProcess)
    {
        assetsProcess.setCreateTime(DateUtils.getNowDate());
        return processMapper.insertAssetsProcess(assetsProcess);
    }

    /**
     * 修改资产流程
     *
     * @param assetsProcess 资产流程
     * @return 结果
     */
    @Override
    public int updateAssetsProcess(AssetsProcess assetsProcess)
    {
        assetsProcess.setUpdateTime(DateUtils.getNowDate());
        return processMapper.updateAssetsProcess(assetsProcess);
    }

    /**
     * 批量删除资产流程
     *
     * @param ids 需要删除的资产流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetsProcessByIds(Long[] ids)
    {
        return processMapper.deleteAssetsProcessByIds(ids);
    }

    /**
     * 删除资产流程信息
     *
     * @param id 资产流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetsProcessById(Long id)
    {
        return processMapper.deleteAssetsProcessById(id);
    }

}