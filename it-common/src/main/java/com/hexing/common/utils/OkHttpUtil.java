package com.hexing.common.utils;

import com.hexing.common.exception.ServiceException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OkHttpUtil {

    private static final Logger log = LoggerFactory.getLogger(OkHttpUtil.class);

    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    public static String postJson(String url, String json) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSONTYPE, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body1 = response.body();
            if (body1 == null) {
                return null;
            }
            return body1.string();
        } catch (Exception e) {
            log.error("post请求失败");
            throw new ServiceException(url + "请求失败");
        }
    }

    public static String getJson(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            if (body == null) {
                return null;
            }
            return body.string();
        } catch (Exception e) {
            log.error("get请求失败");
            throw new ServiceException(url + "请求失败");
        }
    }


}
