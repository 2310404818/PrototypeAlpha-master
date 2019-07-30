package com.swj.prototypealpha.swj.util.Record;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

import com.swj.prototypealpha.MyApplication;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

/**
 * 录音的 Service
 *
 */

public class RecordingService extends Service {

    private static final String LOG_TAG = "RecordingService";
    private  File soundFile;
    private MediaRecorder mRecorder = null;
    private String mFileName = null;
    private String mFilePath = null;

    //开始的时间和过去的时间、计时器
    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private TimerTask mIncrementTimerTask = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            stopRecording();
        }
        super.onDestroy();
    }

    public void startRecording() {
        setFileNameAndPath();
        Log.d("好的花花u花花","i大家急啊急");
        mRecorder     = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  //音频输入源
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);   //设置输出格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);   //设置编码格式
        mRecorder.setOutputFile(soundFile.getAbsolutePath());
        try {
            mRecorder.prepare();
            mRecorder.start();  //开始录制
            mStartingTimeMillis = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFileNameAndPath() {
        Log.d("就嗲家急啊急啊急","基地啊及佳佳");
        MyApplication myApplication = (MyApplication) getApplication();
        File dir1 = new File(myApplication.getFile(),myApplication.getProjectName()+"voice");
        if(!dir1.exists()){
            dir1.mkdirs();
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = date1.format(date);
        File dir = new File(dir1,datenow);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH.mm.ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        soundFile = new File(dir,str+".amr");
        Log.d(String.valueOf(soundFile),"调集骄傲");
        if(!soundFile.exists()){
            try {
                soundFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void stopRecording() {
        mRecorder.stop();
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        mRecorder.release();
    /*
        getSharedPreferences("sp_name_audio", MODE_PRIVATE)
                .edit()
                .putString("audio_path", mFilePath)
                .putLong("elpased", mElapsedMillis)
                .apply();
                */
        if (mIncrementTimerTask != null) {
            mIncrementTimerTask.cancel();
            mIncrementTimerTask = null;
        }
        mRecorder = null;
    }

}
