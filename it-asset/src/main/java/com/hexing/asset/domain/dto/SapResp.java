package com.hexing.asset.domain.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * SAP响应DTO
 *
 * @author sty
 * @date 2023-02-10
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class SapResp {
    /**
     * 状态
     */
    private String CODE;
    /**
     * 消息
     */
    private String MSG;
    /**
     * 资产编号
     */
    private JSONObject DATA;
}
