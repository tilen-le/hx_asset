package com.hexing.asset.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hexing.asset.domain.AssetProcessField;
import com.hexing.asset.domain.AssetProcessVariable;
import com.hexing.asset.domain.AssetsProcess;
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
public interface AssetsProcessMapper extends BaseMapper<AssetsProcess>
{
    /**
     * 查询资产流程
     *
     * @param id 资产流程主键
     * @return 资产流程
     */
    public AssetsProcess selectAssetsProcessById(Long id);

    /**
     * 查询资产流程列表
     *
     * @param assetsProcess 资产流程
     * @return 资产流程集合
     */
    public List<AssetsProcess> selectAssetsProcessList(AssetsProcess assetsProcess);

    List<AssetProcessVariable> selectVariableListByProcessId(String processId);

    /**
     * 新增资产流程
     *
     * @param assetsProcess 资产流程
     * @return 结果
     */
    public int insertAssetsProcess(AssetsProcess assetsProcess);

    /**
     * 修改资产流程
     *
     * @param assetsProcess 资产流程
     * @return 结果
     */
    public int updateAssetsProcess(AssetsProcess assetsProcess);

    /**
     * 删除资产流程
     *
     * @param id 资产流程主键
     * @return 结果
     */
    public int deleteAssetsProcessById(Long id);

    /**
     * 批量删除资产流程
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetsProcessByIds(Long[] ids);

    List<AssetsProcess> selectProcessWithCondition(@Param("processType") String processType, @Param("params") Map<String, Object> params);


    List<AssetsProcess> selectProcessWithDomain(@Param("process") AssetsProcess process, @Param("params")List<AssetProcessField> searchDomains);

    List<AssetProcessVariable> selectVarWithProcessIds(List<Long> processIds);

    List<AssetsProcess> selectProcessWithCondition(MapperQueryParam param);
}
