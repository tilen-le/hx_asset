package com.hexing.asset.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hexing.asset.enums.UIPCodeEnum;
import com.hexing.asset.service.IUIPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UIPServiceImpl implements IUIPService {

    @Value("${uip.uipTransfer}")
    private String uipTransfer;

    /**
     * 通过UIP推送给SAP
     *
     * @param data   请求数据
     * @param code   uip接口码
     * @param logMsg 日志文本
     * @return uip响应体
     */
    @Override
    public String sendToSAP(JSONArray data, String code, String logMsg) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("INBOUND", data);
        params.add("interfaceCode", code);

        log.info("====" + logMsg + "，推送SAP：" + params);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(uipTransfer, params, String.class);

        String body = responseEntity.getBody();
        log.info("====" + logMsg + "，SAP响应：" + body);

        return body;
    }

}
