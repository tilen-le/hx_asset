package com.hexing.assetnew.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.assetnew.domain.AssetProcessField;
import com.hexing.assetnew.domain.AssetProcessVariable;
import com.hexing.assetnew.domain.AssetsProcess;
import com.hexing.asset.domain.dto.MapperQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 资产流程Mapper接口
 *
 * @author zxy
 * @date 2022-11-04
 */
@Repository
public interface AssetsProcessMapper extends BaseMapper<AssetsProcess> {

    List<AssetsProcess> selectProcessWithDomain(@Param("process") AssetsProcess process, @Param("params") List<AssetProcessField> searchDomains);

}
