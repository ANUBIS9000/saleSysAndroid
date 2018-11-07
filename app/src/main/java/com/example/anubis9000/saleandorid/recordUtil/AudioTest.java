package com.example.anubis9000.saleandorid.recordUtil;

/**
 * Created by anubis9000 on 2017/12/29.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class AudioTest {

    static String mPath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/My record/";
    private AudioRecord mAudioRecord;
    private AudioTrack mAudioTrack;
    private boolean mIsAudioing = false;
    private boolean mIsPlaying = false;
    private int mMinBufferSize;
    private int mRecordTime = 20;
    private int mFileCount = 10;
    private static AudioTest mAudioTest;

    public static AudioTest getInstance() {

        if (mAudioTest == null) {

            mAudioTest = new AudioTest();
        }
        return mAudioTest;
    }

    public void createObject(int sampleRateInHz) {
        int audioSource = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
        int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

        mMinBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioEncoding);

        int bufferSizeInBytes = mMinBufferSize * 2;
        mAudioRecord = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioEncoding, bufferSizeInBytes);

        int playBufSize = AudioTrack.getMinBufferSize(sampleRateInHz,
                channelConfig, audioEncoding);
        mAudioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
                sampleRateInHz, channelConfig, audioEncoding, playBufSize,
                AudioTrack.MODE_STREAM, mAudioRecord.getAudioSessionId());
    }

    public boolean isInitSucces() {

        boolean initRecorder = mAudioRecord != null
                && mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED;
        boolean initPlayer = mAudioTrack != null
                && mAudioTrack.getState() == AudioTrack.STATE_INITIALIZED;
        return initRecorder && initPlayer;
    }

    public void startAudio(String phoneNumber) {
        String fileName = "呼出-" + phoneNumber + "-"
                + new SimpleDateFormat("yy-MM-dd_HH-mm-ss")
                .format(new Date(System.currentTimeMillis())) + ".pcm";

        mIsAudioing = true;
        new AudioThread(fileName).start();
    }

    public void stopAudio() {

        mIsAudioing = false;
        if (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {

            mAudioRecord.stop();
        }
    }

    public void startPlayer() {

        mIsPlaying = true;
        new PlayThread().start();
    }

    public void stopPlayer() {

        mIsPlaying = false;
        if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {

            mAudioTrack.stop();
        }
    }

    /**
     * 结束录音与播放
     */
    public void stopVoice() {

        stopAudio();
        stopPlayer();

        mAudioRecord.release();
        mAudioRecord = null;
        mAudioTrack.release();
        mAudioTrack = null;
    }

    private class PlayThread extends Thread {

        public void run() {

            try {
                android.os.Process
                        .setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            } catch (Exception e) {
            }

            try {
                File file = new File(mPath, 15 + ".pcm");
                FileInputStream inputStream = new FileInputStream(file);

                byte[] audioData = new byte[mMinBufferSize];
                mAudioTrack.play();
                while (mIsPlaying) {

                    // 数据源
                    int read = inputStream.read(audioData, 0, mMinBufferSize);
                    mAudioTrack.write(audioData, 0, read);
                }

                inputStream.close();
                mAudioTrack.stop();
            } catch (Exception e) {

            }
        }
    }

    private class AudioThread extends Thread {

        String fileName;

        public AudioThread(String fileName) {

            this.fileName = fileName;
        }

        @Override
        public void run() {

            try {
                android.os.Process
                        .setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            } catch (Exception e) {
            }

            try {
                mAudioRecord.startRecording();
                byte[] buffer = new byte[mMinBufferSize];

                File file = new File(mPath, "" + fileName);

//                file.createNewFile();

                FileOutputStream outputStream = new FileOutputStream(file);

                while (mIsAudioing) {

                    int read = mAudioRecord.read(buffer, 0, mMinBufferSize);
                    for (int i = 0; i < read; i++) {
                        byte b = buffer[i];
                        // 写到文件
                        outputStream.write(buffer, 0, read);
                    }
                }

                mAudioRecord.stop();
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    }
}