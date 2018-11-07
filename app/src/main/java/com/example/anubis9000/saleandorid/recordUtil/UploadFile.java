package com.example.anubis9000.saleandorid.recordUtil;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.anubis9000.saleandorid.MainActivity;
import com.example.anubis9000.saleandorid.MyApplication;
import com.example.anubis9000.saleandorid.R;
import com.example.anubis9000.saleandorid.SSL.MyHttpsClient;
import com.example.anubis9000.saleandorid.model.CallInfoVo;

import junit.framework.Assert;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anubis9000 on 2018/1/15.
 */

public class UploadFile extends AsyncTask<File, Void, Void> {

    String backendUrl = MyApplication.getContext().getString(R.string.backend_url) + "/api/web-phone-mov/waveUpload";

    @Override
    protected Void doInBackground(File... files) {

        if (files[0] == null)

            return null;

        String backendUrl = MyApplication.getContext().getString(R.string.backend_url);

        //文件上传测试方法1
//        upload(files[0],backendUrl + "/api/web-phone-mov/waveUpload");

        //文件上传测试方法2
//        uploadfile1(files);


        //文件上传测试方法3
        //流方式上传
//        httpUrlConnection(files[0]);

        //文件上传测试方法4
//        httpUrlSeri(files[0]);

        //文件上传测试方法5
        httpUrlBody(files[0]);

        return null;

    }

    //文件上传测试方法1
    public static void upload(File file, String reqUrl) {
//        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        try {

            HttpPost httppost = new HttpPost(reqUrl);
            MultipartEntity multipartEntity = new MultipartEntity();

            ContentBody fid = new FileBody(file);

            multipartEntity.addPart("file", fid);
            httppost.setEntity(multipartEntity);
            HttpResponse response = httpclient.execute(httppost);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                HttpEntity entity = response.getEntity();

                String result = EntityUtils.toString(entity);

                Log.i("TAG", "*******" + result);

            } else {

                Log.i("TAG", "请求出了问题");

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //文件上传测试方法2
    protected void uploadfile1(File... files) {

        try {

            File fileToUpload = files[0];

            String boundary = "*****";

            String lineEnd = "\r\n";

            String twoHyphens = "--";

            int maxBufferSize = 1 * 1024 * 1024;

            String fileName = fileToUpload.getAbsolutePath();

            FileInputStream fis = new FileInputStream(new File(fileName));

            String backendUrl = MyApplication.getContext().getString(R.string.backend_url);

            URL serverUrl = new URL(
                    backendUrl + "/api/web-phone-mov/waveUpload");

            HttpURLConnection connection = (HttpURLConnection) serverUrl
                    .openConnection();

            connection.setDoInput(true);

            connection.setDoOutput(true);

            connection.setUseCaches(false);

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");

//            connection.setRequestProperty("ENCTYPE", "multipart/form-data");

//            connection.setRequestProperty("Content-Type",
//                    "multipart/form-data;boundary=" + boundary);

            connection.setRequestProperty("Content-Type",
                    "application/json");


            connection.setRequestProperty("uploaded_file", fileName);

            connection.setRequestProperty("waveName", "waveName");

            DataOutputStream dos = new DataOutputStream(
                    connection.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                    + fileName + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            // create a buffer of maximum size

            int bytesAvailable = fis.available();

            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];

            // read file and write it into form...

            int bytesRead = fis.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);

                bytesAvailable = fis.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);

                bytesRead = fis.read(buffer, 0, bufferSize);

            }

            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)

            int serverResponseCode = connection.getResponseCode();

            String serverResponseMessage = connection.getResponseMessage();

            if (serverResponseCode == 200) {

                Toast.makeText(MyApplication.getContext(),
                        "File is uploaded successfully", Toast.LENGTH_SHORT)
                        .show();

            }

            fis.close();

            dos.flush();

            dos.close();

        } catch (Exception e) {

            Log.e("AudioRecorder:Asynctask", e.getMessage());

        }
    }

    //文件上传测试方法3
    private void httpUrlConnection(File file) {
        try {
            String pathUrl = backendUrl;
            //建立连接
            URL url = new URL(pathUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            ////设置连接属性
            httpConn.setDoOutput(true);//使用 URL 连接进行输出
            httpConn.setDoInput(true);//使用 URL 连接进行输入
            httpConn.setUseCaches(false);//忽略缓存
            httpConn.setRequestMethod("POST");//设置URL请求方法

            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            String requestString = new String(Base64.encodeBase64(buffer));

            //设置请求属性
            //获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            byte[] requestStringBytes = requestString.getBytes("UTF-8");
            httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
            httpConn.setRequestProperty("Content-Type", "application/octet-stream");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");
            //
            String name = URLEncoder.encode("测试文件", "utf-8");
            httpConn.setRequestProperty("waveName", name);

            //建立输出流，并写入数据
            OutputStream outputStream = httpConn.getOutputStream();
            outputStream.write(requestStringBytes);
            outputStream.close();
            //获得响应状态
            int responseCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {//连接成功

                //当正确响应时处理数据
                StringBuffer sb = new StringBuffer();
                String readLine;
                BufferedReader responseReader;
                //处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //文件上传测试方法4 直接序列化 formData有大小限制不能直接传输大文件
    private void httpUrlSeri(File file) {
        try {
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            String requestString = new String(Base64.encodeBase64(buffer));

            //发送方案2
            List<NameValuePair> nvp = new ArrayList<>();
            nvp.add(new BasicNameValuePair("mediaBase64Content", requestString));
            nvp.add(new BasicNameValuePair("name", "测试文件"));

            try {
                String resultStr = MyHttpsClient.service(null, backendUrl, nvp.toArray(new NameValuePair[nvp.size()]));
            } catch (IOException e) {
                Log.e("mylog", e.toString());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //文件上传测试方法5 放入body体中
    private void httpUrlBody(File file) {
        try {
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            String requestString = new String(Base64.encodeBase64(buffer));

            //发送方案2
            List<NameValuePair> nvp = new ArrayList<>();
//            nvp.add(new BasicNameValuePair("mediaBase64Content", requestString));

            String callId = MainActivity.callInfoVo.getCallId();
            nvp.add(new BasicNameValuePair("callId", callId));

            String bodyStr = "{\"callId\":\"" + callId + "\" ,\"mediaBase64Content\":\"" + requestString + "\"}";

            try {
                String resultStr = MyHttpsClient.service(bodyStr, backendUrl, nvp.toArray(new NameValuePair[nvp.size()]));
            } catch (IOException e) {
                Log.e("mylog", e.toString());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
