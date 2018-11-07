package com.example.anubis9000.saleandorid.recordUtil;

/**
 * Created by anubis9000 on 2017/12/25.
 */
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class MyRecorder implements MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener {

    private String phoneNumber;

    private MediaRecorder mrecorder;

    private boolean started = false; //录音机是否已经启动

    private boolean isCommingNumber = false;//是否是来电

    private String TAG = "Recorder";

    public MyRecorder(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
    }

    public MyRecorder() {

    }

    File recordName = null;

    public void start() throws InterruptedException {

        started = true;

        mrecorder = new MediaRecorder();

        File recordPath = new File(
                Environment.getExternalStorageDirectory()
                , "/My record");
        if (!recordPath.exists()) {
            recordPath.mkdirs();
            Log.i("recorder", "创建目录");
        }

        String callDir = "呼出";
        if (isCommingNumber) {
            callDir = "呼入";
        }

        String fileName = callDir + "-" + phoneNumber + "-"
                + new SimpleDateFormat("yy-MM-dd_HH-mm-ss")
                .format(new Date(System.currentTimeMillis())) + ".mp3";//实际是3gp
        recordName = new File(recordPath, fileName);

        try {
            recordName.createNewFile();
            Log.i("recorder", "创建文件" + recordName.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        mrecorder.setAudioSamplingRate(11025);
        mrecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mrecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

//        mrecorder.setAudioEncodingBitRate(11025);
//        mrecorder.setAudioSamplingRate(11025);

        mrecorder.setOutputFile(recordName.getAbsolutePath());

        try {
            mrecorder.prepare();

            mrecorder.setOnInfoListener(this);
            mrecorder.setOnErrorListener(this);

            Thread.sleep(1000);
            mrecorder.start();
            started = true;
            Log.i(TAG , "录音开始");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void stop() {
        try {
            if (mrecorder!=null) {
                mrecorder.stop();
                mrecorder.reset();
                mrecorder.release();
                mrecorder = null;
            }
            started = false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        Log.i(TAG , "录音结束");

        new UploadFile().execute(recordName);
    }



    public void pause() {

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean hasStarted) {
        this.started = hasStarted;
    }

    public boolean isCommingNumber() {
        return isCommingNumber;
    }

    public void setIsCommingNumber(boolean isCommingNumber) {
        this.isCommingNumber = isCommingNumber;
    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {

    }

    @Override
    public void onError(MediaRecorder mediaRecorder, int i, int i1) {

        Log.e(TAG, "onError: ");

    }
}