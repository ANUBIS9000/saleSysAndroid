package com.example.anubis9000.saleandorid.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by anubis9000 on 2017/12/29.
 */
public class HttpClinetAn {

    private static String ENCODING = "UTF-8";
    private static PoolingHttpClientConnectionManager httpClientConnectionManager;

    public static void make() {
//        httpClientConnectionManager = new PoolingHttpClientConnectionManager();
//        httpClientConnectionManager.setMaxTotal(300);
//        httpClientConnectionManager.setDefaultMaxPerRoute(200);
    }
//
//    public static CloseableHttpClient getHttpClient() {
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setConnectionManager(httpClientConnectionManager).build();
//        return httpClient;
//    }

    public static String service(String apiUrl, NameValuePair... params)
            throws IOException {
        StringBuffer httpContent = new StringBuffer();
        httpContent.append("请求地址：" + apiUrl);
        if (params != null) {
            httpContent.append("，发送params参数：");
            for (NameValuePair param : params) {
                String key = param.getName();
                Object value = param.getValue();
                httpContent.append(key + "=" + value + "&");
            }
        }

        long startTime = System.currentTimeMillis();
        CloseableHttpResponse resp = null;
        try {

//            KeyStore trustStore = KeyStore.getInstance(KeyStore
//                    .getDefaultType());
//            trustStore.load(null, null);
//            SSLSocketFactory sf = new SSLSocketFactory(trustStore);
//            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//            getHttpClient().getConnectionManager().getSchemeRegistry().register(new Scheme("https", sf, 443));


            Calendar cal = Calendar.getInstance();
            String spec = apiUrl;
            HttpPost request = new HttpPost(spec);
            List<NameValuePair> param = new ArrayList<>();
            Collections.addAll(param, params);
            request.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));

            //设置超时时间
//            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000)
//                    .setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
//            request.setConfig(requestConfig);

//            resp = getHttpClient().execute(request);
            resp = HttpClients.createDefault().execute(request);

            String retureString = EntityUtils.toString(resp.getEntity(), "UTF-8");
            httpContent.append("，返回内容：" + retureString);
            httpContent.append("，访问接口用时：" + (System.currentTimeMillis() - startTime));

            return retureString;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            String filteredPassword = Arrays.toString(params).replaceAll("PassWord=(.*?)[,|\\]]",
                    "PassWord=***");

            if (resp != null) {
                try {
                    resp.close();
                } catch (Throwable e) {

                }
            }
        }

        return "";

    }
}
