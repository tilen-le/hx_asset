package com.hexing.system.domain;

import com.hexing.common.annotation.Excel;
import com.hexing.common.annotation.Excel.ColumnType;
import com.hexing.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 参数配置表 sys_config
 *
 * @author ruoyi
 */
public class OdoCodeDTO implements Serializable
{
    private static final long serialVersionUID = 1L;


    private String code;

    private String name;

    private String dept_code;

    private String parent_code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept_code() {
        return dept_code;
    }

    public void setDept_code(String dept_code) {
        this.dept_code = dept_code;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }
}
