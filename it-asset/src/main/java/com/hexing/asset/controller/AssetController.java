package com.hexing.asset.controller;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hexing.asset.domain.AssetProcess;
import com.hexing.asset.domain.dto.StatisQueryParam;
import com.hexing.asset.domain.vo.SimpleStatisticVO;
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
        // 筛选条件
        LambdaQueryWrapper<Asset> assetWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<AssetProcess> assetProcessWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(params.getDept())) {
            List<String> deptIdList = sysDeptService.selectDeptByParentId(Long.valueOf(params.getDept()));
            List<String> userCodeList = sysUserService.selectUserByDeptId(deptIdList);
            assetWrapper.in(Asset::getResponsiblePersonCode, userCodeList);
            assetProcessWrapper.in(AssetProcess::getUserCode, userCodeList);
        }

        assetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), Asset::getCreateBy, params.getStartDate())
                .le(ObjectUtil.isNotNull(params.getEndDate()), Asset::getCreateBy, params.getEndDate());
        assetProcessWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), AssetProcess::getCreateTime, params.getStartDate())
                .le(ObjectUtil.isNotNull(params.getEndDate()), AssetProcess::getCreateTime, params.getEndDate());


        List<Asset> assetList = assetService.list(assetWrapper);

        Integer totalNum = assetList.size();                                                /* 资产总数 */
        Double totalValue = assetList.stream().mapToDouble(Asset::getTotalValue).sum();     /* 资产原值 */
        Double totalNetWorth = assetList.stream().mapToDouble(Asset::getNetWorth).sum();    /* 资产净值 */
        Integer storageNum = totalNum;/* 入库数 */

        List<AssetProcess> assetProcessList = assetProcessService.list(assetProcessWrapper);
        Long transformNum = assetProcessList.stream()                                       /* 改造数 */
                .filter(x -> DingTalkAssetProcessType.PROCESS_TRANSFORM.getCode().equals(x.getProcessType()))
                .count();
        Long scrapNum = assetProcessList.stream()                                           /* 报废数 */
                .filter(x -> DingTalkAssetProcessType.PROCESS_SCRAP.getCode().equals(x.getProcessType()))
                .count();
        Long sellOutNum = assetProcessList.stream()                                         /* 外卖数 */
                .filter(x -> DingTalkAssetProcessType.PROCESS_SALE_OUT.getCode().equals(x.getProcessType()))
                .count();

        Map<String, Object> data = new HashMap<>();

        List<SimpleStatisticVO> mainStatistic = new ArrayList<>();
        mainStatistic.add(new SimpleStatisticVO("资产总数", totalNum));
        mainStatistic.add(new SimpleStatisticVO("资产原值", totalValue));
        mainStatistic.add(new SimpleStatisticVO("资产净值", totalNetWorth));
        mainStatistic.add(new SimpleStatisticVO("入库数", storageNum));
        mainStatistic.add(new SimpleStatisticVO("改造数", transformNum));
        mainStatistic.add(new SimpleStatisticVO("报废数", scrapNum));
        mainStatistic.add(new SimpleStatisticVO("外卖数", sellOutNum));

        data.put("main", mainStatistic);

        // 饼图
        Map<String, Long> statusMap = assetList
                .stream().map(Asset::getAssetStatus)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<SimpleStatisticVO> statusCount = new ArrayList<>();
        for (Map.Entry<String, Long> entry : statusMap.entrySet()) {
            statusCount.add(new SimpleStatisticVO(entry.getKey(), entry.getValue()));
        }

        data.put("pie", statusCount);

        return AjaxResult.success(data);
    }

    @PostMapping("/assetCountByCategory")
    public AjaxResult assetCountByCategory(/*@RequestBody StatisQueryParam params*/) {
        // 筛选条件
        LambdaQueryWrapper<Asset> assetWrapper = new LambdaQueryWrapper<>();

//        if (StringUtils.isNotEmpty(params.getDept())) {
//            List<String> deptIdList = sysDeptService.selectDeptByParentId(Long.valueOf(params.getDept()));
//            List<String> userCodeList = sysUserService.selectUserByDeptId(deptIdList);
//            assetWrapper.in(Asset::getResponsiblePersonCode, userCodeList);
//        }

//        assetWrapper.ge(ObjectUtil.isNotNull(params.getStartDate()), Asset::getCreateBy, params.getStartDate())
//                .le(ObjectUtil.isNotNull(params.getEndDate()), Asset::getCreateBy, params.getEndDate());


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


        Map<String, Object> data = new HashMap<>();
        data.put("categoryNumCount", categoryNumCount);
        data.put("totalValueCategoryCount", totalValueCategoryCount);
        data.put("netWorthCategoryCount", netWorthCategoryCount);

        return AjaxResult.success(data);
    }

    @PostMapping("/assetCountByDept")
    public AjaxResult assetCountByDept(@RequestBody StatisQueryParam params) {
        List<List<SimpleStatisticVO>> data = new ArrayList<>();
        // 筛选条件
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
                    tempList.add(new SimpleStatisticVO("deptname", dept.getDeptName()));
                    tempList.add(new SimpleStatisticVO("totalNum", totalNum));
                    tempList.add(new SimpleStatisticVO("totalValue", totalValue));
                    tempList.add(new SimpleStatisticVO("totalNetWorth", totalNetWorth));
                    data.add(tempList);
                }
                return AjaxResult.success(data);
            } else {                       /* 若为公司下的部门 */
                LambdaQueryWrapper<Asset> assetWrapper = new LambdaQueryWrapper<>();
                List<String> deptIdList = sysDeptService.selectDeptByParentId(sysDept.getDeptId());
                deptIdList.add(String.valueOf(sysDept.getDeptId())); // 将当前部门的ID也包含在其子孙部门的ID列表中
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
                tempList.add(new SimpleStatisticVO("deptname", sysDept.getDeptName()));
                tempList.add(new SimpleStatisticVO("totalNum", totalNum));
                tempList.add(new SimpleStatisticVO("totalValue", totalValue));
                tempList.add(new SimpleStatisticVO("totalNetWorth", totalNetWorth));
                data.add(tempList);
            }
        }

        return AjaxResult.success(data);
    }

}
