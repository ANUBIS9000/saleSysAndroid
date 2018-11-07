package com.example.anubis9000.saleandorid.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by anubis9000 on 2017/12/28.
 */
//子线程：使用POST方法向服务器发送用户名、密码等数据
class PostThread extends Thread {

    String name;
    String pwd;

    public PostThread(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    @Override
    public void run() {
        HttpClient httpClient = new DefaultHttpClient();
        String url = "http://192.168.1.112:8080/test.jsp";
        //第二步：生成使用POST方法的请求对象
        HttpPost httpPost = new HttpPost(url);
        //NameValuePair对象代表了一个需要发往服务器的键值对
        NameValuePair pair1 = new BasicNameValuePair("name", name);
        NameValuePair pair2 = new BasicNameValuePair("password", pwd);
        //将准备好的键值对对象放置在一个List当中
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(pair1);
        pairs.add(pair2);
        try {
            //创建代表请求体的对象（注意，是请求体）
            HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
            //将请求体放置在请求对象当中
            httpPost.setEntity(requestEntity);
            //执行请求对象
            try {
                //第三步：执行请求对象，获取服务器发还的相应对象
                HttpResponse response = httpClient.execute(httpPost);
                //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                if (response.getStatusLine().getStatusCode() == 200) {
                    //第五步：从相应对象当中取出数据，放到entity当中
                    HttpEntity entity = response.getEntity();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(entity.getContent()));
                    String result = reader.readLine();
                    Log.d("HTTP", "POST:" + result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}