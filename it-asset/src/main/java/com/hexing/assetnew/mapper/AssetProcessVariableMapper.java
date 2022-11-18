package com.hexing.assetnew.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.assetnew.domain.AssetProcessVariable;
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

    /**
     * 查询流程值表列表
     *
     * @param processIdList
     * @return
     */
    List<AssetProcessVariable> selectProcessVariableWithCondition(List<Long> processIdList);


}
