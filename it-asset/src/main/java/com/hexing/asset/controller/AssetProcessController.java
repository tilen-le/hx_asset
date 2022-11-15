package com.hexing.asset.controller;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hexing.assetnew.domain.Asset;
import com.hexing.asset.domain.dto.StatisQueryParam;
import com.hexing.asset.domain.vo.SimpleStatisticVO;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.assetnew.service.IAssetService;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysDictData;
import com.hexing.common.utils.StringUtils;
import com.hexing.system.service.ISysDeptService;
import com.hexing.system.service.ISysDictDataService;
import com.hexing.system.service.ISysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hexing.common.annotation.Log;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.enums.BusinessType;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;

/**
 * 流程总Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/asset/process")
public class AssetProcessController extends BaseController {
    @Autowired
    private IAssetProcessService assetProcessService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDictDataService sysDictDataService;

    /**
     * 查询流程总列表
     */
    @PreAuthorize("@ss.hasPermi('asset:process:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssetProcess assetProcess) {
        startPage();
        List<AssetProcess> list = assetProcessService.selectAssetProcessList(assetProcess);
        return getDataTable(list);
    }

    /**
     * 导出流程总列表
     */
    @PreAuthorize("@ss.hasPermi('asset:process:export')")
    @Log(title = "流程总", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssetProcess assetProcess) {
        List<AssetProcess> list = assetProcessService.selectAssetProcessList(assetProcess);
        List<SysDictData> dictDataStatusList = sysDictDataService.selectDictDataByType("dingtalk_asset_process_type");
        Map<String,String> dictMap =new HashMap();
        for (SysDictData sysDictData :dictDataStatusList){
            dictMap.put(sysDictData.getDictValue(),sysDictData.getDictLabel());
        }
        for (AssetProcess process :list){
            if (dictMap.containsKey(process.getProcessType())){
                process.setProcessType(dictMap.get(process.getProcessType()));
            }
        }
        ExcelUtil<AssetProcess> util = new ExcelUtil<AssetProcess>(AssetProcess.class);
        return util.exportExcel(list, "资产调拨流程数据");
    }

    /**
     * 获取流程总详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:process:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(assetProcessService.selectAssetProcessById(id));
    }

    /**
     * 新增流程总
     */
    @PreAuthorize("@ss.hasPermi('asset:process:add')")
    @Log(title = "流程总", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssetProcess assetProcess) {
        return toAjax(assetProcessService.insertAssetProcess(assetProcess));
    }

    /**
     * 修改流程总
     */
    @PreAuthorize("@ss.hasPermi('asset:process:edit')")
    @Log(title = "流程总", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssetProcess assetProcess) {
        return toAjax(assetProcessService.updateAssetProcess(assetProcess));
    }

    /**
     * 删除流程总
     */
    @PreAuthorize("@ss.hasPermi('asset:process:remove')")
    @Log(title = "流程总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(assetProcessService.deleteAssetProcessByIds(ids));
    }

    @PostMapping("/processCount")
    public AjaxResult processCount(@RequestBody StatisQueryParam params) {

        // 入库

        // 报废
        LambdaQueryWrapper<AssetProcess> scrapWrapper = new LambdaQueryWrapper<>();
        scrapWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_SCRAP.getCode());
        List<AssetProcess> scrapProcess = assetProcessService.list(scrapWrapper);
        List<String> scrapAssetCodeList = scrapProcess.stream()
                .map(AssetProcess::getAssetCode).collect(Collectors.toList());
        List<Asset> scrapAssetList = assetService.list(new LambdaQueryWrapper<Asset>()
                .in(Asset::getResponsiblePersonCode, scrapAssetCodeList));
        Map<String, Asset> scrapAssetMap = scrapAssetList.stream()
                .collect(Collectors.toMap(Asset::getAssetCode, asset -> asset));

        // 根据时间统计数量
        Map<String, Long> scrapTimeNumCount = scrapProcess.stream()
                .collect(
                        Collectors.groupingBy(o -> DateUtil.format(o.getCreateTime(), "yyyy-MM"), Collectors.counting())
                );

        // 根据资产分类描述统计数量
        List<SimpleStatisticVO> scrapCategoryNumCount = scrapAssetList.stream()
                .collect(Collectors.groupingBy(Asset::getCategory))
                .entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    Integer value = entry.getValue().size();
                    return new SimpleStatisticVO(key, value);
                }).collect(Collectors.toList());
        System.out.println(scrapCategoryNumCount);

        // 根据部门统计数量
        if (StringUtils.isNotEmpty(params.getDept())) {
            Long deptId = Long.valueOf(params.getDept());
            SysDept sysDept = sysDeptService.selectDeptById(deptId);
            if (ObjectUtil.isNull(sysDept)) {
                return AjaxResult.error("系统无该组织架构信息");
            }
            // 若为公司
            if (sysDept.getParentId() == 0L) {  /* 若为公司 */
                // 查询所有下一级部门
                SysDept deptQuery = new SysDept();
                deptQuery.setParentId(deptId);
                List<SysDept> childDeptList = sysDeptService.queryChildDeptList(deptQuery);
                if (CollUtil.isEmpty(childDeptList)) {
                    return AjaxResult.error("该组织架构下无子部门");
                }
                for (SysDept dept : childDeptList) {
                    LambdaQueryWrapper<Asset> assetWrapper = new LambdaQueryWrapper<>();
                    List<String> deptIdList = sysDeptService.selectDeptByParentId(dept.getDeptId());
                    deptIdList.add(String.valueOf(dept.getDeptId())); // 将当前部门的ID也包含在其子孙部门的ID列表中
                    List<String> userCodeList = sysUserService.selectUserByDeptId(deptIdList);
                    List<Asset> assetList = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(userCodeList)) {
                        assetWrapper.in(Asset::getResponsiblePersonCode, userCodeList);
                        assetList = assetService.list(assetWrapper);
                    }
                    Integer totalNum = assetList.size();
                    Double totalValue = assetList.stream().mapToDouble(Asset::getTotalValue).sum();
                    Double totalNetWorth = assetList.stream().mapToDouble(Asset::getNetWorth).sum();
                    List<SimpleStatisticVO> tempList = new ArrayList<>();
                    tempList.add(new SimpleStatisticVO("totalNum", totalNum));
                    tempList.add(new SimpleStatisticVO("totalValue", totalValue));
                    tempList.add(new SimpleStatisticVO("totalNetWorth", totalNetWorth));
                    Map<String, List<SimpleStatisticVO>> temp = new HashMap<>();
                    temp.put(dept.getDeptName(), tempList);
                }
            }


            // 外卖

            // 改造


            return null;
        }
        return null;
    }
}
