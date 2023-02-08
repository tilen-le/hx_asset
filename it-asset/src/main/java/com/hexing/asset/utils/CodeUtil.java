package com.hexing.asset.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hexing.asset.domain.AssetManagementConfig;
import com.hexing.asset.domain.dto.MaterialCategorySimpleDTO;
import com.hexing.common.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CodeUtil {

    private static String materialCategoryJsonFile;

    @Value("${conf.assetCategoryConfFilePath}")
    public void setMaterialCategoryJsonFile(String path) {
        CodeUtil.materialCategoryJsonFile = path;
    }

    /**
     * 读取resource目录下的json文件，读取为json字符串
     *
     * @param jsonFilePath json文件路径
     * @return json字符串
     */
    public static String getJsonStr(String jsonFilePath) {
        StringBuilder sb = new StringBuilder();
        try {
            File jsonFile = new File(jsonFilePath);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取物料号资产类别对应关系JSON对象
     *
     * @return
     */
    public static JSONArray getAssetCategoryTree() {
        return JSONObject.parseArray(getJsonStr(materialCategoryJsonFile));
    }


    /**
     * 物料号解析
     *
     * @param materialNumber 物料号
     * @param categoryDict   物料号资产类别对应关系
     * @return 资产类型DTO
     */
    public static MaterialCategorySimpleDTO parseMaterialNumber(String materialNumber, JSONObject categoryDict) {
        if (StringUtils.isEmpty(materialNumber)) {
            return null;
        }
        MaterialCategorySimpleDTO dto = new MaterialCategorySimpleDTO();

        String assetTypeCode = materialNumber.substring(0, 1); // 没什么用，留着提供代码可读性
        String assetCategoryCode = materialNumber.substring(1, 3);
        String assetSubCategoryCode = materialNumber.substring(3, 5);

        // 由于目前只有固定资产一种assetType，因此直接取json第一级的description字段值
        dto.setAssetType(categoryDict.getString("description"));
        JSONArray assetCategoryJOArray = categoryDict.getJSONArray("child");
        for (int i = 0; i < assetCategoryJOArray.size(); i++) {
            JSONObject jo = (JSONObject) assetCategoryJOArray.get(i);
            if (assetCategoryCode.equals(jo.get("code"))) {
                dto.setAssetCategory(jo.getString("description"));
                JSONArray assetSubCategoryList = jo.getJSONArray("child");
                for (int j = 0; j < assetSubCategoryList.size(); j++) {
                    JSONObject subJo = (JSONObject) assetSubCategoryList.get(j);
                    if (assetSubCategoryCode.equals(subJo.get("code"))) {
                        dto.setAssetSubCategory(subJo.getString("description"));
                    }
                }
            }
        }

        if (StringUtils.isEmpty(dto.getAssetType())
                && StringUtils.isEmpty(dto.getAssetCategory())
                && StringUtils.isEmpty(dto.getAssetSubCategory())) {
            return null;
        }

        return dto;
    }

    /**
     * @param config       资产配置
     * @param  config
     * @return 资产类型DTO
     */
    public static MaterialCategorySimpleDTO getAssetTypeName(AssetManagementConfig config) {
        if (ObjectUtils.isEmpty(config)) {
            return null;
        }
        MaterialCategorySimpleDTO dto = new MaterialCategorySimpleDTO();

        String assetTypeCode = config.getAssetType();
        String assetCategoryCode = config.getAssetCategory();
        String assetSubCategoryCode = config.getAssetSubCategory();
        JSONArray assetCategoryTree = CodeUtil.getAssetCategoryTree();
        Object assetTypeObj = assetCategoryTree.stream().filter(x -> JSONObject.parseObject(x.toString()).getString("code").equals(assetTypeCode)).findFirst().orElse(null);
        Object assetCategoryObj =null;
        if(ObjectUtils.isNotEmpty(assetTypeObj)){
            String description = JSONObject.parseObject(assetTypeObj.toString()).getString("description");
            dto.setAssetType(description);
            JSONArray child = JSONObject.parseObject(assetTypeObj.toString()).getJSONArray("child");
            assetCategoryObj = child.stream().filter(x -> JSONObject.parseObject(x.toString()).getString("code").equals(assetCategoryCode)).findFirst().orElse(null);
        }
        if (ObjectUtils.isNotEmpty(assetCategoryObj)){
            String description = JSONObject.parseObject(assetCategoryObj.toString()).getString("description");
            dto.setAssetCategory(description);
            JSONArray child = JSONObject.parseObject(assetCategoryObj.toString()).getJSONArray("child");

            String subName="";
            String tempName="";
            if (StringUtils.isNotBlank(assetSubCategoryCode)&&assetSubCategoryCode.contains(",")){
                String[] split = assetSubCategoryCode.split(",");
                for (String s : split) {
                    Object assetSubCategoryObj = child.stream().filter(x -> JSONObject.parseObject(x.toString()).getString("code").equals(s)).findFirst().orElse(null);
                    if (ObjectUtils.isNotEmpty(assetSubCategoryObj)){
                        subName = JSONObject.parseObject(assetSubCategoryObj.toString()).getString("description");
                        tempName += subName + ",";
                    }
                    if (StringUtils.isNotBlank(tempName)){
                        subName =tempName.substring(0, tempName.lastIndexOf(","));
                    }
                }
            }else {
                Object assetSubCategoryObj = child.stream().filter(x -> JSONObject.parseObject(x.toString()).getString("code").equals(assetSubCategoryCode)).findFirst().orElse(null);
                if (ObjectUtils.isNotEmpty(assetSubCategoryObj)){
                    subName = JSONObject.parseObject(assetSubCategoryObj.toString()).getString("description");
                }
            }
            dto.setAssetSubCategory(subName);
        }
        if (StringUtils.isEmpty(dto.getAssetType())
                && StringUtils.isEmpty(dto.getAssetCategory())
                && StringUtils.isEmpty(dto.getAssetSubCategory())) {
            return null;
        }

        return dto;
    }
}
