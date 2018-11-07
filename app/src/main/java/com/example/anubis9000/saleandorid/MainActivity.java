package com.example.anubis9000.saleandorid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSONObject;
import com.example.anubis9000.saleandorid.SSL.MyHttpsClient;
import com.example.anubis9000.saleandorid.model.CallInfoVo;
import com.example.anubis9000.saleandorid.recordUtil.AudioTest;
import com.example.anubis9000.saleandorid.recordUtil.MyRecorder;
import com.example.anubis9000.saleandorid.utils.DateUtils;
import com.example.anubis9000.saleandorid.utils.PhoneInfoUtil;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity";

    // static final String TAG = "Recorder";
    public static MyRecorder recorder = new MyRecorder();

    public static CallInfoVo callInfoVo = new CallInfoVo();

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Checks if the app has permission to write to device storage If the app
     * does not has permission then the user will be prompted to grant
     * permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private com.tencent.smtt.sdk.WebView tencent_webview;
//    private String backendUrl = MyApplication.getContext().getString(R.string.backend_url);

    private String backendUrl = "http://这里填写域名?rememberMe=true";

    private String url = backendUrl;

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        // TODO Auto-generated method stub
        tencent_webview = (com.tencent.smtt.sdk.WebView) findViewById(R.id.mainWebview);

        Random random = new Random();
        url = url + "&a=" + DateUtils.date2Int(new Date()) + random.nextInt(100);

        tencent_webview.loadUrl(url);
        com.tencent.smtt.sdk.WebSettings webSettings = tencent_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染等级

        tencent_webview.setVerticalScrollbarOverlay(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //在js中调用本地java方法
        tencent_webview.addJavascriptInterface(new JsInterface(this), "AndroidWebView");


        tencent_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                CookieSyncManager.getInstance().sync();
//                Log.i("listener", url);
            }
        });

//        tencent_webview.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                tencent_webview.loadUrl(url);
//            }
//        }, 500);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && tencent_webview.canGoBack()) {
            tencent_webview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {
            QbSdk.preInit(getApplicationContext(), null);// 设置X5初始化完成的回调接口
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        preinitX5WebCore();
        //预加载x5内核
        Intent intent = new Intent(this, AdvanceLoadX5Service.class);
        startService(intent);


        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();

//        verifyStoragePermissions(this);
//
//        //初始化录音
//        AudioTest.getInstance().createObject(11025);
//
//        //拿到手机号码
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        String deviceid = tm.getDeviceId();
//        String tel = tm.getLine1Number();//手机号码
//        callInfoVo.setSellerPhone(tel);
//
//        String imei = tm.getSimSerialNumber();
//        String imsi = tm.getSubscriberId();

    }

    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void showInfoFromJs(String ob) {

            JSONObject jsonOb = JSONObject.parseObject(ob);
            callInfoVo = new CallInfoVo();
            callInfoVo.setSellerId(jsonOb.getString("sellerId"));
            callInfoVo.setSchoolId(jsonOb.getString("schoolId"));
            callInfoVo.setLinkMan(jsonOb.getString("linkMan"));
            callInfoVo.setLinkManJob(jsonOb.getString("linkManJob"));
            callInfoVo.setCustomerId(jsonOb.getString("customerId"));
            callInfoVo.setProductId(jsonOb.getString("productId"));
            callInfoVo.setCustomerPhone(jsonOb.getString("customerPhone"));
            callInfoVo.setSellerName(jsonOb.getString("sellerName"));

            //增加callid生成
            String callId = jsonOb.getString("sellerId") + "-" + DateUtils.dateToString("yyyyMMddHHmmssSSS");
            callInfoVo.setCallId(callId);

//            Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
        }
    }


    //handler 需要是静态的不然容易内存泄漏，声明一个子类避免内部类直接调用外部
    static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);


            // UI界面的更新等相关操作
        }
    }

    public final static PhoneStateListener listener = new PhoneStateListener() {
        private String phoneState = "";

        MyHandler handler = new MyHandler();

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {

//                if(callInfoVo.getCustomerPhone()==null){
//                    Log.e("mylog", "拨打之前没有成功获取电话信息");
//                    return;
//                }
//
//                //发送方案2
//                List<NameValuePair> nvp = new ArrayList<>();
//                nvp.add(new BasicNameValuePair("sellerId", callInfoVo.getSellerId()));
//                nvp.add(new BasicNameValuePair("sellerName", callInfoVo.getSellerName()));
//                nvp.add(new BasicNameValuePair("schoolId", callInfoVo.getSchoolId()));
//                nvp.add(new BasicNameValuePair("linkMan", callInfoVo.getLinkMan()));
//                nvp.add(new BasicNameValuePair("linkManJob", callInfoVo.getLinkManJob()));
//                nvp.add(new BasicNameValuePair("customerId", callInfoVo.getCustomerId()));
//                nvp.add(new BasicNameValuePair("productId", callInfoVo.getProductId()));
//                nvp.add(new BasicNameValuePair("phone", callInfoVo.getCustomerPhone()));
//                nvp.add(new BasicNameValuePair("sellerPhone", "13126528823"));
//                nvp.add(new BasicNameValuePair("callOutStartTime", callInfoVo.getCallOutStartTime()));
//                nvp.add(new BasicNameValuePair("fwdAnswerTime", callInfoVo.getFwdAnswerTime()));
//                nvp.add(new BasicNameValuePair("callEndTime", callInfoVo.getCallEndTime()));
//                nvp.add(new BasicNameValuePair("waveName", callInfoVo.getWaveName()));
//                nvp.add(new BasicNameValuePair("callId", callInfoVo.getCallId()));
//
//                try {
//                    String backendUrl = MyApplication.getContext().getString(R.string.backend_url);
////                    String backendUrl = "http://192.168.1.175";
//                    String resultStr = MyHttpsClient.service(null,backendUrl + "/api/web-phone-mov/mobile-call-info", nvp.toArray(new NameValuePair[nvp.size()]));
//
//                    //发送成功就刷新掉对象内容
//                    callInfoVo = new CallInfoVo();
//                } catch (IOException e) {
//                    Log.e("mylog", e.toString());
//                }
//
//
//                // 在这里进行 http request.网络请求相关操作
//                Message msg = new Message();
//                Bundle data = new Bundle();
//                data.putString("value", "请求结果");
//                msg.setData(data);
//                handler.sendMessage(msg);
            }
        };

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

//            // 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
//
//            super.onCallStateChanged(state, incomingNumber);
//
//            Log.i(TAG, "有状态");
//
//            switch (state) {
//
//                case TelephonyManager.CALL_STATE_IDLE: {
//                    if (!"CALL_STATE_IDLE".equals(phoneState)) {
//                        if ("CALL_STATE_OFFHOOK".equals(phoneState)) {
//
//                            // System.out.println("挂断");
//                            Log.i(TAG, "挂断");
//
//                            Calendar c = Calendar.getInstance();
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            String nowTime = sdf.format(c.getTime());
//                            Log.i(TAG, nowTime);
//                            callInfoVo.setCallEndTime(nowTime);
//
//
//                            // MyApplication.getContext().stopService(new
//                            // Intent(MyApplication.getContext(),
//                            // AudioRecorderService.class));
//
//                            try {
//                                if (!recorder.isCommingNumber() && recorder.isStarted()) {
//                                    Log.i(TAG, "已挂断 关闭录音机");
//                                    recorder.stop();
//                                }
//                            }catch(Exception e){
//                                e.printStackTrace();
//                            }
//
//                            //测试获取话单信息
//                            //获取通话记录
//                            ContentResolver cr;
//                            cr = MyApplication.getContext().getContentResolver();
//                            String callHistoryListStr = PhoneInfoUtil.getCallHistoryList(null, cr);
//                            Log.i("phoneinfo", "请求结果为-->" + callHistoryListStr);
//                            if (!"".equals(callHistoryListStr)) {
//                                callInfoVo.setFwdAnswerTime(callHistoryListStr);
//                            }
//
////                            if(AudioTest.getInstance().isInitSucces()){
////                                AudioTest.getInstance().stopAudio();
////                                Log.i(TAG, "已挂断 关闭录音机");
////                            }else{
////                                Log.i(TAG, "录音及没有成功初始化");
////                            }
//
//
//                            // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
//                            new Thread(networkTask).start();
//
//                        }
//
//                        phoneState = "CALL_STATE_IDLE";
//                    }
//
//                    break;
//                }
//                case TelephonyManager.CALL_STATE_OFFHOOK: {
//                    if (!"CALL_STATE_OFFHOOK".equals(phoneState)) {
//                        phoneState = "CALL_STATE_OFFHOOK";
//
//                        // System.out.println("接听");
//                        Log.i(TAG, "接听");
//
//                        Calendar c = Calendar.getInstance();
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String nowTime = sdf.format(c.getTime());
//                        Log.i(TAG, nowTime);
//                        callInfoVo.setCallOutStartTime(nowTime);
//
//                        //不再用默认时间
//                        //callInfoVo.setFwdAnswerTime(nowTime);
//
//                        // MyApplication.getContext().startService(new
//                        // Intent(MyApplication.getContext(),
//                        // AudioRecorderService.class));
//
//                        try {
//                            recorder.setPhoneNumber(incomingNumber);
//                            recorder.setIsCommingNumber(false);
//                            if (!recorder.isCommingNumber() && !recorder.isStarted()) {
//                                Log.i(TAG, "去电已接通 启动录音机");
//
//                                try {
//                                    recorder.start();
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//
////                        if(AudioTest.getInstance().isInitSucces()){
////                            AudioTest.getInstance().startAudio(incomingNumber);
////                            Log.i(TAG, "去电已接通 启动录音机");
////                        }else{
////                            Log.i(TAG, "录音及没有成功初始化");
////                        }
//
//                    }
//
//                    break;
//                }
//                case TelephonyManager.CALL_STATE_RINGING: {
//                    if (!"CALL_STATE_RINGING".equals(phoneState)) {
//                        phoneState = "CALL_STATE_RINGING";
//
//                        // System.out.println("响铃:来电号码" + incomingNumber);
//                        Log.i(TAG, "响铃:来电号码" + incomingNumber);
//
//                        // 输出来电号码
//                    }
//
//                    break;
//                }
//
//                default: {
//                    phoneState = "";
//                    break;
//                }
//
//            }

        }

    };


}
