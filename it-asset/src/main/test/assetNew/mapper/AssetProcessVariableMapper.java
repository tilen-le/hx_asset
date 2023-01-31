package com.hexing.assetNew.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.assetNew.domain.AssetProcessVariable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 流程值Mapper接口
 *
 * @author zxy
 * @date 2022-11-03
 */
@Repository
public interface AssetProcessVariableMapper extends BaseMapper<AssetProcessVariable> {

    List<AssetProcessVariable> selectVariableListByProcessId(Long processId);

    List<AssetProcessVariable> selectVarWithProcessIds(List<Long> processIds);
}
