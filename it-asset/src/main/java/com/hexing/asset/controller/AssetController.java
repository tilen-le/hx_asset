package com.hexing.asset.controller;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.dto.StatisQueryParam;
import com.hexing.asset.domain.vo.SimpleStatisticVO;
import com.hexing.asset.enums.AssetStatisticType;
import com.hexing.asset.enums.DingTalkAssetProcessType;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.common.annotation.DataScope;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.core.domain.model.LoginUser;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.ServletUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.framework.web.service.TokenService;
import com.hexing.system.service.ISysDeptService;
import com.hexing.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hexing.common.annotation.Log;
import com.hexing.common.core.controller.BaseController;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.enums.BusinessType;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.service.IAssetService;
import com.hexing.common.utils.poi.ExcelUtil;
import com.hexing.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;

import static com.hexing.common.utils.PageUtil.startPage;

/**
 * 资产表Controller
 *
 * @author zxy
 * @date 2022-09-08
 */
@Api(tags = "资产信息管理")
@RestController
@RequestMapping("/asset")
public class AssetController extends BaseController {

    @Autowired
    private IAssetService assetService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IAssetProcessService assetProcessService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询资产列表
     */
    @ApiOperation("查询资产列表")
    @PreAuthorize("@ss.hasPermi('asset:collection:list')")
    @GetMapping("/list")
    public TableDataInfo list(Asset asset) {
        startPage();
        List<Asset> list = assetService.selectAssetList(asset);
        return getDataTable(list);
    }

    /**
     * 导出资产表列表
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:export')")
    @Log(title = "资产表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Asset asset) {
        List<Asset> list = assetService.selectAssetList(asset);
//        List<Asset> list = assetService.list();
        ExcelUtil<Asset> util = new ExcelUtil<>(Asset.class);
        return util.exportExcel(list, "固定资产数据");
    }

    /**
     * 获取资产详细信息
     */
    @ApiOperation("获取资产详细信息")
    @PreAuthorize("@ss.hasPermi('asset:asset:query')")
    @GetMapping(value = "/getInfo/{assetCode}")
    public AjaxResult getInfo(@PathVariable String assetCode) {
        Asset asset = assetService.selectAssetByAssetCode(assetCode);
        return AjaxResult.success(asset);
    }

    /**
     * 新增资产表
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:add')")
    @Log(title = "资产表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Asset asset) {
        return toAjax(assetService.insertAsset(asset));
    }

    /**
     * 修改资产表
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:edit')")
    @Log(title = "资产表", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody Asset asset) {
        return toAjax(assetService.updateAsset(asset));
    }

    /**
     * 删除资产表
     *
     * @editor 80015306
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:remove')")
    @Log(title = "资产表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{assetCodes}")
    public AjaxResult remove(@PathVariable List<String> assetCodes) {
        return toAjax(assetService.deleteAssetByAssetCodes(assetCodes));
    }

    /**
     * 资产导入
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:import')")
    @Log(title = "资产信息导入", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Asset> util = new ExcelUtil<>(Asset.class);
        List<Asset> assetList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = assetService.importAsset(assetList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    /**
     * 资产导入模板下载
     */
    @PreAuthorize("@ss.hasPermi('asset:asset:template')")
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<Asset> util = new ExcelUtil<>(Asset.class);
        return util.importTemplateExcel("资产信息导入模板");
    }

    /**
     * 总资产统计和资产状态统计饼图
     */
    @PostMapping("/assetCount")
    public AjaxResult assetCount(@RequestBody StatisQueryParam params) {
        Map<String, Object> data = new HashMap<>();

        if (StringUtils.isEmpty(params.getDept())) {
            return AjaxResult.error("未指定组织范围");
        }
        if (ObjectUtil.isEmpty(params.getStartDate()) || ObjectUtil.isEmpty(params.getEndDate())) {
            return AjaxResult.error("未指定起止日期");
        }

        // 筛选条件
        LambdaQueryWrapper<Asset> assetWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<AssetProcess> assetProcessWrapper = new LambdaQueryWrapper<>();

        List<String> deptIdList = sysDeptService.selectDeptByParentId(Long.valueOf(params.getDept()));
        List<String> userCodeList = sysUserService.selectUserByDeptId(deptIdList);
        assetWrapper.in(Asset::getResponsiblePersonCode, userCodeList);

        // 资产的起止日期筛选对应字段为“资本化日期/资产价值录入日期”
        assetWrapper.ge(Asset::getCapitalizationDate, params.getStartDate())
                .le(Asset::getCapitalizationDate, params.getEndDate());

        List<Asset> assetList = assetService.list(assetWrapper);

        int totalNum = 0;           /* 资产总数 */
        double totalValue = 0;      /* 资产原值 */
        double totalNetWorth = 0;   /* 资产净值 */
        int storageNum = 0;         /* 入库数 */
        long transformNum = 0;      /* 改造数 */
        long scrapNum = 0;          /* 报废数 */
        long sellOutNum = 0;        /* 外卖数 */

        List<SimpleStatisticVO> statusCount = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(assetList)) {
            totalNum = assetList.size();
            totalValue = assetList.stream().mapToDouble(Asset::getTotalValue).sum();
            totalNetWorth = assetList.stream().mapToDouble(Asset::getNetWorth).sum();
            storageNum = totalNum;

            assetProcessWrapper
                    .in(AssetProcess::getAssetCode
                            ,assetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()))
                    .ge(AssetProcess::getCreateTime, params.getStartDate())
                    .le(AssetProcess::getCreateTime, params.getEndDate());
            List<AssetProcess> assetProcessList = assetProcessService.list(assetProcessWrapper);
            transformNum = assetProcessList.stream()
                    .filter(x -> DingTalkAssetProcessType.PROCESS_TRANSFORM.getCode().equals(x.getProcessType()))
                    .count();
            scrapNum = assetProcessList.stream()
                    .filter(x -> DingTalkAssetProcessType.PROCESS_SCRAP.getCode().equals(x.getProcessType()))
                    .count();
            sellOutNum = assetProcessList.stream()
                    .filter(x -> DingTalkAssetProcessType.PROCESS_SALE_OUT.getCode().equals(x.getProcessType()))
                    .count();

            // 饼图
            Map<String, Long> statusMap = assetList.stream()
                    .map(Asset::getAssetStatus)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            for (Map.Entry<String, Long> entry : statusMap.entrySet()) {
                statusCount.add(new SimpleStatisticVO(entry.getKey(), entry.getValue()));
            }
        }

        List<SimpleStatisticVO> mainStatistic = new ArrayList<>();
        mainStatistic.add(new SimpleStatisticVO("资产总数", totalNum));
        mainStatistic.add(new SimpleStatisticVO("资产原值(元)", totalValue));
        mainStatistic.add(new SimpleStatisticVO("资产净值(元)", totalNetWorth));
        mainStatistic.add(new SimpleStatisticVO("入库数", storageNum));
        mainStatistic.add(new SimpleStatisticVO("改造数", transformNum));
        mainStatistic.add(new SimpleStatisticVO("报废数", scrapNum));
        mainStatistic.add(new SimpleStatisticVO("外卖数", sellOutNum));

        data.put("main", mainStatistic);
        data.put("pie", statusCount);

        return AjaxResult.success(data);
    }

    /**
     * 根据资产分类描述统计数量/原值/净值
     *
     * @param params
     * @return
     */
    @PostMapping("/assetCountByCategory")
    public AjaxResult assetCountByCategory(@RequestBody StatisQueryParam params) {
        // 筛选条件
        LambdaQueryWrapper<Asset> assetWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isEmpty(params.getDept())) {
            return AjaxResult.error("未指定组织范围");
        }
        if (ObjectUtil.isEmpty(params.getStartDate()) || ObjectUtil.isEmpty(params.getEndDate())) {
            return AjaxResult.error("未指定起止日期");
        }

        List<String> deptIdList = sysDeptService.selectDeptByParentId(Long.valueOf(params.getDept()));
        List<String> userCodeList = sysUserService.selectUserByDeptId(deptIdList);
        assetWrapper.in(Asset::getResponsiblePersonCode, userCodeList);

        assetWrapper.ge(Asset::getCapitalizationDate, params.getStartDate())
                .le(Asset::getCapitalizationDate, params.getEndDate());

        List<Asset> assetList = assetService.list(assetWrapper);

        List<SimpleStatisticVO> categoryNumCount = assetList.stream()
                .collect(Collectors.groupingBy(Asset::getCategory))
                .entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    Integer value = entry.getValue().size();
                    return new SimpleStatisticVO(key, value);
                }).collect(Collectors.toList());

        List<SimpleStatisticVO> totalValueCategoryCount = assetList.stream()
                .collect(Collectors.groupingBy(Asset::getCategory))
                .entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    Double value = entry.getValue().stream().mapToDouble(Asset::getTotalValue).sum();
                    return new SimpleStatisticVO(key, value);
                }).collect(Collectors.toList());

        List<SimpleStatisticVO> netWorthCategoryCount = assetList.stream()
                .collect(Collectors.groupingBy(Asset::getCategory))
                .entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    Double value = entry.getValue().stream().mapToDouble(Asset::getNetWorth).sum();
                    return new SimpleStatisticVO(key, value);
                }).collect(Collectors.toList());

        Map<String, List<SimpleStatisticVO>> data = new HashMap<>();
        data.put("categoryNumCount", categoryNumCount);
        data.put("totalValueCategoryCount", totalValueCategoryCount);
        data.put("netWorthCategoryCount", netWorthCategoryCount);

        return AjaxResult.success(data);
    }

    /**
     * 根据部门统计数量/原值/净值
     *
     * @param params
     * @return
     */
    @PostMapping("/assetCountByDept")
    public AjaxResult assetCountByDept(@RequestBody StatisQueryParam params) {

        Map<String, List<SimpleStatisticVO>> data = new HashMap<>();

        final String NUM_DEPT_COUNT = "numDeptCount";
        final String TOTAL_VALUE_DEPT_COUNT = "totalValueDeptCount";
        final String TOTAL_NET_WORTH = "totalNetWorth";

        if (StringUtils.isEmpty(params.getDept())) {
            return AjaxResult.error("未指定组织范围");
        }
        if (ObjectUtil.isEmpty(params.getStartDate()) || ObjectUtil.isEmpty(params.getEndDate())) {
            return AjaxResult.error("未指定起止日期");
        }

        Long deptId = Long.valueOf(params.getDept());
        SysDept sysDept = sysDeptService.selectDeptById(deptId);
        if (ObjectUtil.isNull(sysDept)) {
            return AjaxResult.error("系统无该组织架构信息");
        }

        List<SysDept> childDeptList = new ArrayList<>();
        childDeptList.add(sysDept);
        if (sysDept.getParentId() == 0L) {
            // 查询所有下一级部门
            SysDept deptQuery = new SysDept();
            deptQuery.setParentId(deptId);
            childDeptList = sysDeptService.queryChildDeptList(deptQuery);
            if (CollUtil.isEmpty(childDeptList)) {
                return AjaxResult.error("该组织架构下无子部门");
            }
        }

        data.put(NUM_DEPT_COUNT, new ArrayList<>());
        data.put(TOTAL_VALUE_DEPT_COUNT, new ArrayList<>());
        data.put(TOTAL_NET_WORTH, new ArrayList<>());
        for (SysDept dept : childDeptList) {
            LambdaQueryWrapper<Asset> assetWrapper = new LambdaQueryWrapper<>();
            assetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), Asset::getCapitalizationDate, params.getStartDate())
                    .le(ObjectUtil.isNotNull(params.getEndDate()), Asset::getCapitalizationDate, params.getEndDate());
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

            data.get(NUM_DEPT_COUNT).add(new SimpleStatisticVO(dept.getDeptName(), totalNum));
            data.get(TOTAL_VALUE_DEPT_COUNT).add(new SimpleStatisticVO(dept.getDeptName(), totalValue));
            data.get(TOTAL_NET_WORTH).add(new SimpleStatisticVO(dept.getDeptName(), totalNetWorth));
        }

        return AjaxResult.success(data);
    }

    /**
     * 入库/报废/外卖/改造-数量和时间折线图统计
     *
     * @param params
     * @return
     */
    @PostMapping("/assetProcessTypeTimeNumCount")
    public AjaxResult assetProcessTypeTimeNumCount(@RequestBody StatisQueryParam params) {

        List<SimpleStatisticVO> data = new ArrayList<>();

        // 入库
        LambdaQueryWrapper<Asset> storageAssetWrapper = new LambdaQueryWrapper<>();
        storageAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), Asset::getCreateTime, params.getStartDate())
                .le(ObjectUtil.isNotNull(params.getEndDate()), Asset::getCreateTime, params.getEndDate());
        if (StringUtils.isNotEmpty(params.getDept())) {
            List<String> deptIdList = sysDeptService.selectDeptByParentId(Long.valueOf(params.getDept()));
            List<String> userCodeList = sysUserService.selectUserByDeptId(deptIdList);
            storageAssetWrapper.in(Asset::getResponsiblePersonCode, userCodeList);
        }
        List<Asset> storageAssetList = assetService.list(storageAssetWrapper);
        Map<String, Long> storageTimeNumCount = new HashMap<>();
        if (AssetStatisticType.MONTH.equals(params.getType())) {
            storageTimeNumCount = storageAssetList.stream()
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCreateTime(), "yyyy-MM"), Collectors.counting())
                    );
        } else if (AssetStatisticType.YEAR.equals(params.getType())) {
            storageTimeNumCount = storageAssetList.stream()
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCreateTime(), "yyyy"), Collectors.counting())
                    );
        }

        // 报废
        LambdaQueryWrapper<AssetProcess> scrapAssetWrapper = new LambdaQueryWrapper<>();
        scrapAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_SCRAP.getCode());
        scrapAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
        if (StringUtils.isNotEmpty(params.getDept())) {
            scrapAssetWrapper.in(AssetProcess::getAssetCode,
                    storageAssetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
        }
        List<AssetProcess> scrapProcessList = assetProcessService.list(scrapAssetWrapper);
        Map<String, Long> scrapProcessNumCount = new HashMap<>();
        if (AssetStatisticType.MONTH.equals(params.getType())) {
            scrapProcessNumCount = scrapProcessList.stream()
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCreateTime(), "yyyy-MM"), Collectors.counting())
                    );
        } else if (AssetStatisticType.YEAR.equals(params.getType())) {
            scrapProcessNumCount = scrapProcessList.stream()
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCreateTime(), "yyyy"), Collectors.counting())
                    );
        }

        // 外卖
        LambdaQueryWrapper<AssetProcess> sellOutAssetWrapper = new LambdaQueryWrapper<>();
        sellOutAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_SALE_OUT.getCode());
        sellOutAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
        if (StringUtils.isNotEmpty(params.getDept())) {
            sellOutAssetWrapper.in(AssetProcess::getAssetCode,
                    storageAssetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
        }
        List<AssetProcess> sellOutProcessList = assetProcessService.list(sellOutAssetWrapper);
        Map<String, Long> sellOutProcessNumCount = new HashMap<>();
        if (AssetStatisticType.MONTH.equals(params.getType())) {
            sellOutProcessNumCount = sellOutProcessList.stream()
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCreateTime(), "yyyy-MM"), Collectors.counting())
                    );
        } else if (AssetStatisticType.YEAR.equals(params.getType())) {
            sellOutProcessNumCount = sellOutProcessList.stream()
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCreateTime(), "yyyy"), Collectors.counting())
                    );
        }

        // 改造
        LambdaQueryWrapper<AssetProcess> transformAssetWrapper = new LambdaQueryWrapper<>();
        transformAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_TRANSFORM.getCode());
        transformAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
        if (StringUtils.isNotEmpty(params.getDept())) {
            transformAssetWrapper.in(AssetProcess::getAssetCode,
                    storageAssetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
        }
        List<AssetProcess> transformProcessList = assetProcessService.list(transformAssetWrapper);
        Map<String, Long> transformProcessNumCount=new HashMap<>();
        if (AssetStatisticType.MONTH.equals(params.getType())) {
            transformProcessNumCount = transformProcessList.stream()
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCreateTime(), "yyyy-MM"), Collectors.counting())
                    );
        } else if (AssetStatisticType.YEAR.equals(params.getType())) {
            transformProcessNumCount = transformProcessList.stream()
                    .collect(
                            Collectors.groupingBy(o -> DateUtil.format(o.getCreateTime(), "yyyy"), Collectors.counting())
                    );
        }

//        {2022-09=124, 2022-06=5, 2022-07=2, 2021-08=3}
//        {2022-09=3}
//        {2022-09=1}
//        {2022-09=6}

        List<String> monthBetween = new ArrayList<>();
        if (ObjectUtil.isNotNull(params.getStartDate()) || ObjectUtil.isNotNull(params.getEndDate())) {
            try {
                monthBetween = DateUtils.getMonthBetween(params.getStartDate(), params.getEndDate());
            } catch (Exception e) {
                logger.error("", e);
            }
        }

        List<Long> storageTimeNumCountList = new ArrayList<>();
        List<Long> scrapProcessNumCountList = new ArrayList<>();
        List<Long> sellOutProcessNumCountList = new ArrayList<>();
        List<Long> transformProcessNumCountList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(monthBetween)) {
            for (String month : monthBetween) {
                storageTimeNumCountList.add(storageTimeNumCount.getOrDefault(month, 0L));
                scrapProcessNumCountList.add(scrapProcessNumCount.getOrDefault(month, 0L));
                sellOutProcessNumCountList.add(sellOutProcessNumCount.getOrDefault(month, 0L));
                transformProcessNumCountList.add(transformProcessNumCount.getOrDefault(month, 0L));
            }
        }

        data.add(new SimpleStatisticVO("x", monthBetween));
        data.add(new SimpleStatisticVO("入库", storageTimeNumCountList));
        data.add(new SimpleStatisticVO("报废", scrapProcessNumCountList));
        data.add(new SimpleStatisticVO("外卖", sellOutProcessNumCountList));
        data.add(new SimpleStatisticVO("改造", transformProcessNumCountList));

        return AjaxResult.success(data);
    }

    @PostMapping("/assetProcessTypeCategoryNumCount")
    public AjaxResult assetProcessTypeCategoryNumCount(@RequestBody StatisQueryParam params) {
        //  入库
        List<SimpleStatisticVO> data = new ArrayList<>();
        LambdaQueryWrapper<Asset> storageAssetWrapper = new LambdaQueryWrapper<>();
        storageAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), Asset::getCreateTime, params.getStartDate())
                .le(ObjectUtil.isNotNull(params.getEndDate()), Asset::getCreateTime, params.getEndDate());
        if (StringUtils.isNotEmpty(params.getDept())) {
            List<String> deptIdList = sysDeptService.selectDeptByParentId(Long.valueOf(params.getDept()));
            List<String> userCodeList = sysUserService.selectUserByDeptId(deptIdList);
            storageAssetWrapper.in(Asset::getResponsiblePersonCode, userCodeList);
        }
        List<Asset> storageAssetList = assetService.list(storageAssetWrapper);
        Map<String, Long> storageCategoryNumCount = storageAssetList.stream()
                .collect(Collectors.groupingBy(Asset::getCategory, Collectors.counting()));

        // 报废
        LambdaQueryWrapper<AssetProcess> scrapAssetWrapper = new LambdaQueryWrapper<>();
        scrapAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_SCRAP.getCode());
        scrapAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
        scrapAssetWrapper.in(AssetProcess::getAssetCode,
                storageAssetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
        List<AssetProcess> scrapProcessList = assetProcessService.list(scrapAssetWrapper);
        List<String> scrapAssetCode = scrapProcessList.stream()
                .map(AssetProcess::getAssetCode).collect(Collectors.toList());
        Map<String, Long> scrapProcessNumCount = storageAssetList.stream()
                .filter(x -> scrapAssetCode.contains(x.getAssetCode()))
                .collect(Collectors.groupingBy(Asset::getCategory, Collectors.counting()));


        // 外卖
        LambdaQueryWrapper<AssetProcess> sellOutAssetWrapper = new LambdaQueryWrapper<>();
        sellOutAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_SALE_OUT.getCode());
        sellOutAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
        sellOutAssetWrapper.in(AssetProcess::getAssetCode,
                storageAssetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
        List<AssetProcess> sellOutProcessList = assetProcessService.list(sellOutAssetWrapper);
        List<String> sellOutAssetCode = sellOutProcessList.stream()
                .map(AssetProcess::getAssetCode).collect(Collectors.toList());
        Map<String, Long> sellOutProcessNumCount = storageAssetList.stream()
                .filter(x -> sellOutAssetCode.contains(x.getAssetCode()))
                .collect(Collectors.groupingBy(Asset::getCategory, Collectors.counting()));

        // 改造
        LambdaQueryWrapper<AssetProcess> transformAssetWrapper = new LambdaQueryWrapper<>();
        transformAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_TRANSFORM.getCode());
        transformAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
        transformAssetWrapper.in(AssetProcess::getAssetCode,
                storageAssetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
        List<AssetProcess> transformProcessList = assetProcessService.list(transformAssetWrapper);
        List<String> transformAssetCode = transformProcessList.stream()
                .map(AssetProcess::getAssetCode).collect(Collectors.toList());
        Map<String, Long> transformProcessNumCount = storageAssetList.stream()
                .filter(x -> transformAssetCode.contains(x.getAssetCode()))
                .collect(Collectors.groupingBy(Asset::getCategory, Collectors.counting()));

//        {固定资产-电脑及软件=1, 固定资产-其他设备=19, 固定资产-机器设备=52, 固定资产-运输设备=2, 固定资产-办公设备=2, 固定资产-电子设备=58}
//        {固定资产-电子设备=1}
//        {固定资产-电子设备=1}
//        {固定资产-电子设备=1}

        Set<String> xAxisValue = storageCategoryNumCount.keySet();

        List<Long> storageNumCountList = new ArrayList<>();
        List<Long> scrapProcessNumCountList = new ArrayList<>();
        List<Long> sellOutProcessNumCountList = new ArrayList<>();
        List<Long> transformProcessNumCountList = new ArrayList<>();
        for (String val : xAxisValue) {
            storageNumCountList.add(storageCategoryNumCount.getOrDefault(val, 0L));
            scrapProcessNumCountList.add(scrapProcessNumCount.getOrDefault(val, 0L));
            sellOutProcessNumCountList.add(sellOutProcessNumCount.getOrDefault(val, 0L));
            transformProcessNumCountList.add(transformProcessNumCount.getOrDefault(val, 0L));
        }

        data.add(new SimpleStatisticVO("x", xAxisValue));
        data.add(new SimpleStatisticVO("入库", storageNumCountList));
        data.add(new SimpleStatisticVO("报废", scrapProcessNumCountList));
        data.add(new SimpleStatisticVO("外卖", sellOutProcessNumCountList));
        data.add(new SimpleStatisticVO("改造", transformProcessNumCountList));

        return AjaxResult.success(data);
    }

    @PostMapping("/assetProcessTypeDeptNumCount")
    public AjaxResult assetProcessTypeDeptNumCount(@RequestBody StatisQueryParam params) {
        List<SimpleStatisticVO> data = new ArrayList<>();
        List<String> xAxisValue = new ArrayList<>();
        List<Long> storageNumCountList = new ArrayList<>();
        List<Long> scrapProcessNumCountList = new ArrayList<>();
        List<Long> sellOutProcessNumCountList = new ArrayList<>();
        List<Long> transformProcessNumCountList = new ArrayList<>();

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
                    // 入库
                    LambdaQueryWrapper<Asset> assetWrapper = new LambdaQueryWrapper<>();
                    assetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), Asset::getCreateTime, params.getStartDate())
                            .le(ObjectUtil.isNotNull(params.getEndDate()), Asset::getCreateTime, params.getEndDate());
                    List<String> deptIdList = sysDeptService.selectDeptByParentId(dept.getDeptId());
                    deptIdList.add(String.valueOf(dept.getDeptId())); // 将当前部门的ID也包含在其子孙部门的ID列表中
                    List<String> userCodeList = sysUserService.selectUserByDeptId(deptIdList);
                    List<Asset> assetList = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(userCodeList)) {
                        assetWrapper.in(Asset::getResponsiblePersonCode, userCodeList);
                        assetList = assetService.list(assetWrapper);
                    }
                    if (CollectionUtil.isEmpty(assetList)) continue;
                    xAxisValue.add(dept.getDeptName());
                    storageNumCountList.add((long) assetList.size());
                    // 报废
                    LambdaQueryWrapper<AssetProcess> scrapAssetWrapper = new LambdaQueryWrapper<>();
                    scrapAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_SCRAP.getCode());
                    scrapAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                            .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
                    scrapAssetWrapper.in(AssetProcess::getAssetCode,
                            assetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
                    List<AssetProcess> scrapProcessList = assetProcessService.list(scrapAssetWrapper);
                    List<String> scrapAssetCode = scrapProcessList.stream()
                            .map(AssetProcess::getAssetCode).collect(Collectors.toList());
                    Long scrapAssetNum = assetList.stream().filter(x -> scrapAssetCode.contains(x.getAssetCode())).count();
                    scrapProcessNumCountList.add(scrapAssetNum);
                    // 外卖
                    LambdaQueryWrapper<AssetProcess> sellOutAssetWrapper = new LambdaQueryWrapper<>();
                    sellOutAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_SALE_OUT.getCode());
                    sellOutAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                            .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
                    sellOutAssetWrapper.in(AssetProcess::getAssetCode,
                            assetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
                    List<AssetProcess> sellOutProcessList = assetProcessService.list(sellOutAssetWrapper);
                    List<String> sellOutAssetCode = sellOutProcessList.stream()
                            .map(AssetProcess::getAssetCode).collect(Collectors.toList());
                    Long sellOutAssetNum = assetList.stream().filter(x -> sellOutAssetCode.contains(x.getAssetCode())).count();
                    sellOutProcessNumCountList.add(sellOutAssetNum);
                    // 改造
                    LambdaQueryWrapper<AssetProcess> transformAssetWrapper = new LambdaQueryWrapper<>();
                    transformAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_TRANSFORM.getCode());
                    transformAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                            .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
                    transformAssetWrapper.in(AssetProcess::getAssetCode,
                            assetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
                    List<AssetProcess> transformProcessList = assetProcessService.list(transformAssetWrapper);
                    List<String> transformAssetCode = transformProcessList.stream()
                            .map(AssetProcess::getAssetCode).collect(Collectors.toList());
                    Long transformAssetNum = assetList.stream().filter(x -> transformAssetCode.contains(x.getAssetCode())).count();
                    transformProcessNumCountList.add(transformAssetNum);

                }
                data.add(new SimpleStatisticVO("x", xAxisValue));
                data.add(new SimpleStatisticVO("入库", storageNumCountList));
                data.add(new SimpleStatisticVO("报废", scrapProcessNumCountList));
                data.add(new SimpleStatisticVO("外卖", sellOutProcessNumCountList));
                data.add(new SimpleStatisticVO("改造", transformProcessNumCountList));

                return AjaxResult.success(data);
            } else {                       /* 若为公司下的部门 */
                // 入库
                LambdaQueryWrapper<Asset> assetWrapper = new LambdaQueryWrapper<>();
                assetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), Asset::getCreateTime, params.getStartDate())
                        .le(ObjectUtil.isNotNull(params.getEndDate()), Asset::getCreateTime, params.getEndDate());
                List<String> deptIdList = sysDeptService.selectDeptByParentId(sysDept.getDeptId());
                deptIdList.add(String.valueOf(sysDept.getDeptId())); // 将当前部门的ID也包含在其子孙部门的ID列表中
                List<String> userCodeList = sysUserService.selectUserByDeptId(deptIdList);
                List<Asset> assetList = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(userCodeList)) {
                    assetWrapper.in(Asset::getResponsiblePersonCode, userCodeList);
                    assetList = assetService.list(assetWrapper);
                }
                xAxisValue.add(sysDept.getDeptName());
                storageNumCountList.add((long) assetList.size());
                // 报废
                LambdaQueryWrapper<AssetProcess> scrapAssetWrapper = new LambdaQueryWrapper<>();
                scrapAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_SCRAP.getCode());
                scrapAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                        .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
                scrapAssetWrapper.in(AssetProcess::getAssetCode,
                        assetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
                List<AssetProcess> scrapProcessList = assetProcessService.list(scrapAssetWrapper);
                List<String> scrapAssetCode = scrapProcessList.stream()
                        .map(AssetProcess::getAssetCode).collect(Collectors.toList());
                Long scrapAssetNum = assetList.stream().filter(x -> scrapAssetCode.contains(x.getAssetCode())).count();
                scrapProcessNumCountList.add(scrapAssetNum);
                // 外卖
                LambdaQueryWrapper<AssetProcess> sellOutAssetWrapper = new LambdaQueryWrapper<>();
                sellOutAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_SALE_OUT.getCode());
                sellOutAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                        .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
                sellOutAssetWrapper.in(AssetProcess::getAssetCode,
                        assetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
                List<AssetProcess> sellOutProcessList = assetProcessService.list(sellOutAssetWrapper);
                List<String> sellOutAssetCode = sellOutProcessList.stream()
                        .map(AssetProcess::getAssetCode).collect(Collectors.toList());
                Long sellOutAssetNum = assetList.stream().filter(x -> sellOutAssetCode.contains(x.getAssetCode())).count();
                sellOutProcessNumCountList.add(sellOutAssetNum);
                // 改造
                LambdaQueryWrapper<AssetProcess> transformAssetWrapper = new LambdaQueryWrapper<>();
                transformAssetWrapper.eq(AssetProcess::getProcessType, DingTalkAssetProcessType.PROCESS_TRANSFORM.getCode());
                transformAssetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                        .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());
                transformAssetWrapper.in(AssetProcess::getAssetCode,
                        assetList.stream().map(Asset::getAssetCode).collect(Collectors.toList()));
                List<AssetProcess> transformProcessList = assetProcessService.list(transformAssetWrapper);
                List<String> transformAssetCode = transformProcessList.stream()
                        .map(AssetProcess::getAssetCode).collect(Collectors.toList());
                Long transformAssetNum = assetList.stream().filter(x -> transformAssetCode.contains(x.getAssetCode())).count();
                transformProcessNumCountList.add(transformAssetNum);

                data.add(new SimpleStatisticVO("x", xAxisValue));
                data.add(new SimpleStatisticVO("入库", storageNumCountList));
                data.add(new SimpleStatisticVO("报废", scrapProcessNumCountList));
                data.add(new SimpleStatisticVO("外卖", sellOutProcessNumCountList));
                data.add(new SimpleStatisticVO("改造", transformProcessNumCountList));

                return AjaxResult.success(data);
            }
        }

        return AjaxResult.success(data);
    }

}
