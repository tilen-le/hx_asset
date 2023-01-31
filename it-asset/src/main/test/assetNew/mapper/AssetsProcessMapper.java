package com.hexing.assetNew.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.assetNew.domain.AssetProcessField;
import com.hexing.assetNew.domain.AssetsProcess;
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
public interface AssetsProcessMapper extends BaseMapper<AssetsProcess> {

    List<AssetsProcess> selectProcessWithDomain(@Param("process") AssetsProcess process, @Param("params") List<AssetProcessField> searchDomains);

}
