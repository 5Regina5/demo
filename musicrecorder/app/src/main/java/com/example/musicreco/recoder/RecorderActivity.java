package com.example.musicreco.recoder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.example.musicreco.R;
import com.example.musicreco.audio.AudioListActivity;
import com.example.musicreco.databinding.ActivityRecoderBinding;

public class RecorderActivity extends AppCompatActivity {
    private ActivityRecoderBinding binding;
    private RecorderService recorderService;
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RecorderService.RecorderBinder binder= (RecorderService.RecorderBinder) service;
            recorderService=binder.getService();
            recorderService.startRecorder();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRecoderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=new Intent(this,RecorderService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                break;
            case R.id.iv_stop:
                recorderService.stopRecorder();
                Intent intent = new Intent(this, AudioListActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection!=null) {
            unbindService(connection);
        }
    }
}
