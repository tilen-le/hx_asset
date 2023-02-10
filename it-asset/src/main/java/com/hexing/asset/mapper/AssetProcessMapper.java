package com.hexing.asset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.asset.domain.AssetProcessField;
import com.hexing.asset.domain.AssetProcess;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资产流程Mapper接口
 *
 * @author zxy
 * @date 2022-11-04
 */
@Repository
public interface AssetProcessMapper extends BaseMapper<AssetProcess> {

    List<AssetProcess> selectProcessWithDomain(@Param("process") AssetProcess process, @Param("params") List<AssetProcessField> searchDomains);

}
