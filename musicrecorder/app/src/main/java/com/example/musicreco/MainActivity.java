package com.example.musicreco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.musicreco.audio.AudioListActivity;
import com.example.musicreco.databinding.ActivityMainBinding;
import com.example.musicreco.recoder.RecorderActivity;
import com.example.musicreco.utils.Contants;
import com.example.musicreco.utils.IFileInter;
import com.example.musicreco.utils.PermissionUtils;
import com.example.musicreco.utils.SDCardUtils;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    int time=3;
    String []permissions={Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE

    };
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what==1) {
                time--;
                if (time==0) {
                    startActivity(new Intent(MainActivity.this, AudioListActivity.class));
                    finish();
                }else{
                    binding.mainTv.setText(time+"");
                    handler.sendEmptyMessageDelayed(1,1000);
                }
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.mainTv.setText(time+"");
        PermissionUtils.getInstance().onRequestPermission(this,permissions,listener);
    }
    PermissionUtils.OnPermissionCallbackListener listener=new PermissionUtils.OnPermissionCallbackListener() {
        @Override
        public void onGranted() {
           //判断是否有文件夹
            createAppDir();
            //倒计时进入
            handler.sendEmptyMessageDelayed(1,1000);
        }

        @Override
        public void onDenied(List<String> deniedPermissions) {
            PermissionUtils.getInstance()
                    .showDialogTipUserGotoAppSetting(MainActivity.this);
        }
    };

    private void createAppDir() {
        File recoderDir = SDCardUtils.getInstance().createAppFetchDir(IFileInter.FETCH_DIR_AUDIO);
        Contants.PATH_FETCH_DIR_RECORD=recoderDir.getAbsolutePath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.getInstance().onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }
    public void record(View view){
        startActivity(new Intent(this, RecorderActivity.class));
    }

}