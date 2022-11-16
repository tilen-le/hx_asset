package com.hexing.assetnew.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.assetnew.domain.AssetProcessVariable;
import org.springframework.stereotype.Repository;

/**
 * 流程值Mapper接口
 *
 * @author zxy
 * @date 2022-11-03
 */
@Repository
public interface AssetProcessVariableMapper extends BaseMapper<AssetProcessVariable> {

    List<AssetProcessVariable> selectProcessVariableWithCondition(List<Long> processIdList);

    /**
     * 批量插入
     * @param variableList
     * @return
     */
    Integer insertBatch(List<AssetProcessVariable> variableList);

}
