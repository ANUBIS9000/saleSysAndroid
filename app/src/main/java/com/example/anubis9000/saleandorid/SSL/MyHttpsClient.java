package com.example.anubis9000.saleandorid.SSL;

/**
 * Created by anubis9000 on 2017/12/29.
 */

import android.content.Context;
import android.content.res.AssetManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MyHttpsClient {
    private static final int SET_CONNECTION_TIMEOUT = 50 * 1000;
    private static final int SET_SOCKET_TIMEOUT = 200 * 1000;

    public static HttpClient getNewHttpClient() {
        try {
            // 获取系统默认的KeyStore对象
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            // 使用系统默认的KeyStore对象初始化SSLSocketFactory
            // 注意这里的SSLSocketFactory是org.apache.http.conn.ssl中的
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);

            HttpParams params = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(params, 60000);
            HttpConnectionParams.setSoTimeout(params, 60000);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            // 注意为https协议绑定之前定义的SSLSocketFactory对象
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            HttpConnectionParams.setConnectionTimeout(params,
                    SET_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SET_SOCKET_TIMEOUT);
            HttpClient client = new DefaultHttpClient(ccm, params);

            return client;
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private static KeyStore certTrusted(Context context) throws Exception {
        // 从资源文件中获取.cer证书文件
        AssetManager am = context.getAssets();
        InputStream ins = am.open("12306.cer");
        try {
            // 读取证书
            CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
            java.security.cert.Certificate cer = cerFactory.generateCertificate(ins);
            // 创建一个证书库，并将证书导入证书库
            KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
            keyStore.load(null, null);
            keyStore.setCertificateEntry("12306", (java.security.cert.Certificate) cer);
            return keyStore;
        } finally {
            ins.close();
        }
    }

    public static String service(String bodyStr, String apiUrl, NameValuePair... params)
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

            Calendar cal = Calendar.getInstance();
            String spec = apiUrl;
            List<NameValuePair> param = new ArrayList<>();
            Collections.addAll(param, params);

            HttpClient hc = getNewHttpClient();
            HttpPost hg = new HttpPost(apiUrl);

            hg.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));

            if (null!=bodyStr&&!"".equals(bodyStr)) {
                StringEntity se = new StringEntity(bodyStr);
                hg.setEntity(se);
            }

            HttpResponse response = hc.execute(hg);

            //使用下面的代码访问B类站点
//            // 获取keystore
//            KeyStore keystore = certTrusted(this);
//            // keystore对象作为getNewHttpClient的参数
//            HttpClient hc = MyHttpsClient.getNewHttpClient(keystore);
//            HttpGet hg = new HttpGet(apiUrl);
//            HttpResponse response = hc.execute(hg);

            String retureString = EntityUtils.toString(response.getEntity(), "UTF-8");
            httpContent.append("，返回内容：" + retureString);
            httpContent.append("，访问接口用时：" + (System.currentTimeMillis() - startTime));

            return retureString;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
