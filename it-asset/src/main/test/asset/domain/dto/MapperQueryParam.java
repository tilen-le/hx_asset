package com.hexing.assetNew.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.hexing.assetNew.domain.AssetProcessField;
import com.hexing.assetNew.domain.Process;
import com.hexing.assetNew.domain.AssetsProcess;
import com.hexing.assetNew.enums.AssetProcessType;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.bean.BeanTool;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Data
public class MapperQueryParam {
    /** 流程类型编号 */
    private String processType;
    /** 关联表名 */
    private String relationTableName;
    /** 关联表对应外键（数据库字段名） */
    private String relationTableForeignKey;
    /** 关联表对应外键驼峰名 */
    private String relationTableForeignKeyCamel;
    /** 流程查询条件 */
    private Map<String, Object> processCondition;
    /** 关联表查询条件sql列表 */
    private List<String> sqlConditionList;

    public MapperQueryParam() {
    }

    public MapperQueryParam(AssetsProcess process, List<AssetProcessField> fieldList) {
        // 从枚举中查询对应class流程的流程编号
        this.processType = AssetProcessType.getValue(process);
        getSqlConditionList(process, fieldList);
        this.processCondition = BeanTool.objectToMap(process);
    }

    private void getSqlConditionList(AssetsProcess process, List<AssetProcessField> fieldList) {
        for (AssetProcessField field : fieldList) {
            if (StringUtils.isNotEmpty(field.getRelationTable())) {
                this.relationTableName = field.getRelationTable();
                this.relationTableForeignKey = field.getRelationTableField();
                this.relationTableForeignKeyCamel = field.getFieldKey();
                break;
            }
        }
        this.sqlConditionList = new ArrayList<>();
        for (AssetProcessField field : fieldList) {
            if (StringUtils.isNotEmpty(field.getSearchPattern())
                    && StringUtils.isNotEmpty((String) BeanTool.getFieldValue(process, field.getFieldKey()))) {
                String searchPattern = field.getSearchPattern();
                String dbFieldName = StrUtil.toUnderlineCase(field.getFieldKey());
                String value = (String) BeanTool.getFieldValue(process, field.getFieldKey());
                // 下面代码后续需封装
                if ("like".equals(searchPattern)) {
                    value = "%" + value + "%";
                }
                value = "\"" + value + "\"";

                String sql = formatSQLCondition(dbFieldName, searchPattern, value);
                this.sqlConditionList.add(sql);
            }
        }
    }

    private String formatSQLCondition(String field, String operation, String value) {
        StringJoiner joiner = new StringJoiner(" ");
        return joiner.add(field).add(operation).add(value).toString();
    }

}
