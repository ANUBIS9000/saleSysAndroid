package com.example.anubis9000.saleandorid.utils;

/**
 * Created by anubis9000 on 2017/12/28.
 */

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClient {
    public final static String TAG = "HttpClient";
    private static final CloseableHttpClient simpleClient = HttpClientBuilder.create()
            .setMaxConnTotal(1000).build();
    private static String ENCODING = "UTF-8";
    private static PoolingHttpClientConnectionManager httpClientConnectionManager;

    static {
        httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        httpClientConnectionManager.setMaxTotal(300);
        httpClientConnectionManager.setDefaultMaxPerRoute(200);
    }

    public static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager).build();
        return httpClient;
    }

    public static String get(String url) {
        String res = null;
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(new HttpGet(url));
            res = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return res;
    }

    public static String post(String url, Map<String, Object> params, Map<String, Object> headers) {
        String responseText = null;
        CloseableHttpResponse response = null;
        try {
            StringBuffer httpContent = new StringBuffer();
            httpContent.append("请求地址：" + url);
            HttpPost method = new HttpPost(url);
            if (params != null && !params.isEmpty()) {
                httpContent.append("，发送参数：");
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    String key = param.getKey();
                    Object value = param.getValue();
                    if (value != null) {
                        String val = value.toString().trim();
                        httpContent.append(key + "=" + value + "&");
                        paramList.add(new BasicNameValuePair(key, val));
                    }
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
            }
            if (headers != null && !headers.isEmpty()) {
                httpContent.append("，发送头部：");
                for (Map.Entry<String, Object> header : headers.entrySet()) {
                    String key = header.getKey();
                    Object value = header.getValue();
                    if (value != null) {
                        String val = value.toString().trim();
                        httpContent.append(key + "=" + value + ";");
                        method.setHeader(key, val);
                    }
                }
            }
            response = getHttpClient().execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity, ENCODING);
                httpContent.append("，返回内容：" + responseText);
            }
            Log.i(TAG, httpContent.toString());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return responseText;
    }

    public static String post(String url, Map<String, Object> params) {
        Map<String, Object> headers = new HashMap<String, Object>();
        return post(url, params, headers);
    }

    public static <T> T post(String url, Map<String, Object> params, Map<String, Object> headerMap,
                             Class<T> clazz) {
        String responseText = post(url, params, headerMap);
        if (responseText != null)
            return JSON.parseObject(responseText, clazz);
        return null;
    }

    public static <T> T post(String url, Map<String, Object> params, Class<T> clazz) {
        return post(url, params, null, clazz);
    }

    /**
     * post use charset
     *
     * @param url      target url
     * @param paramMap param map<String,Object>
     * @param charset  charset
     * @return String content
     * @throws Exception any exceptions
     */
    public static String postUseCharset(String url, Map<String, Object> paramMap, String charset)
            throws Exception {
        final HttpPost request = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : paramMap.keySet()) {
            params.add(new BasicNameValuePair(key, paramMap.get(key).toString()));
        }
        request.setEntity(new UrlEncodedFormEntity(params, charset));
        final CloseableHttpResponse execute = simpleClient.execute(request);
        if (execute != null) {
            try {
                return EntityUtils.toString(execute.getEntity(), charset);
            } finally {
                try {
                    execute.close();
                } catch (Throwable e) {
                    // e.printStackTrace();
                }
            }
        }
        return null;
    }
}
