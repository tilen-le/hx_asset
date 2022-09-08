package com.hexing.asset.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.hexing.asset.domain.AssetProcess;

/**
 * 流程总Mapper接口
 *
 * @author zxy
 * @date 2022-09-08
 */
@Repository
public interface AssetProcessMapper extends BaseMapper<AssetProcess>
{
    /**
     * 查询流程总
     *
     * @param id 流程总主键
     * @return 流程总
     */
    public AssetProcess selectAssetProcessById(Long id);

    /**
     * 查询流程总列表
     *
     * @param assetProcess 流程总
     * @return 流程总集合
     */
    public List<AssetProcess> selectAssetProcessList(AssetProcess assetProcess);

    /**
     * 新增流程总
     *
     * @param assetProcess 流程总
     * @return 结果
     */
    public int insertAssetProcess(AssetProcess assetProcess);

    /**
     * 修改流程总
     *
     * @param assetProcess 流程总
     * @return 结果
     */
    public int updateAssetProcess(AssetProcess assetProcess);

    /**
     * 删除流程总
     *
     * @param id 流程总主键
     * @return 结果
     */
    public int deleteAssetProcessById(Long id);

    /**
     * 批量删除流程总
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssetProcessByIds(Long[] ids);
}
