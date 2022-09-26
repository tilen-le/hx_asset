package com.hexing.asset.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class StatisQueryParam {
    /** 组织架构编号 */
    private String dept;
    /** 年月类型 */
    private String type;
    /** 开始时间 */
    private Date startDate;
    /** 结束时间 */
    private Date endDate;
}
