package com.hexing.asset.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hexing.asset.domain.AssetsProcess;

/**
 * 资产流程Service接口
 *
 * @author zxy
 * @date 2022-11-04
 */
public interface IAssetsProcessService extends IService<AssetsProcess>
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
     * 批量删除资产流程
     *
     * @param ids 需要删除的资产流程主键集合
     * @return 结果
     */
    public int deleteAssetsProcessByIds(Long[] ids);

    /**
     * 删除资产流程信息
     *
     * @param id 资产流程主键
     * @return 结果
     */
    public int deleteAssetsProcessById(Long id);
}
