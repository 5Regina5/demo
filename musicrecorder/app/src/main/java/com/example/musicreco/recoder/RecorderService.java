package com.example.musicreco.recoder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.IBinder;

import com.example.musicreco.utils.Contants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecorderService extends Service {
    private MediaRecorder recorder;
    private boolean isAlive=false;
    private String recorderDirPath;//目录
    private SimpleDateFormat sdf;
    @Override
    public void onCreate() {
        super.onCreate();
        recorderDirPath= Contants.PATH_FETCH_DIR_RECORD;
        sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
    }
    public void startRecorder()  {
        if (recorder==null) {
            recorder=new MediaRecorder();
        }
        isAlive=true;
        recorder.reset();
        //设置参数
        setRecorder();
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //停止录音
    public void stopRecorder(){
        if (recorder!=null) {
            recorder.stop();
            recorder=null;

        }
    }

    private void setRecorder() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//从麦克风获取声音
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);//输出格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);//设置编码格式
        String time = sdf.format(new Date());//输出格式
        File file = new File(recorderDirPath,time+".amr");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        recorder.setOutputFile(file.getAbsolutePath());
        //最大录制时间
        recorder.setMaxDuration(10*60*1000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new RecorderBinder();
    }
    public class RecorderBinder extends Binder{
        public RecorderService getService(){return RecorderService.this;}
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}