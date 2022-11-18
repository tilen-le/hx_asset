package com.hexing.assetnew.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.assetnew.domain.AssetProcessVariable;
import com.hexing.assetnew.mapper.AssetProcessVariableMapper;
import com.hexing.assetnew.service.IAssetProcessVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程值Service业务层处理
 *
 * @author zxy
 * @date 2022-11-03
 */
@Service
public class AssetProcessVariableServiceImpl extends ServiceImpl<AssetProcessVariableMapper, AssetProcessVariable> implements IAssetProcessVariableService {

}
