package com.hexing.asset.service;

import com.alibaba.fastjson.JSONArray;
import com.hexing.asset.domain.dto.SapResp;

public interface IUIPService {

    /**
     * 通过UIP推送给SAP
     *
     * @param data   请求数据
     * @param code   uip接口码
     * @param logMsg 日志文本
     * @return uip响应体
     */
    String sendToSAP(JSONArray data, String code, String logMsg);
}
