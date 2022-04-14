package com.example.musicreco.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.musicreco.bean.AudioBean;
import com.example.musicreco.databinding.DialogAudioinfoBinding;

import java.text.DecimalFormat;

public class AudioInfoDialog extends Dialog {
    private DialogAudioinfoBinding binding;
    public AudioInfoDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DialogAudioinfoBinding.inflate(getLayoutInflater());
        CardView root = binding.getRoot();
        setContentView(root);
        binding.tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
    public void setDialogWidth(){
        //获取当前屏幕窗口对象
        Window window=getWindow();
        //获取窗口信息参数
        WindowManager.LayoutParams wlp=window.getAttributes();
        //获取屏幕宽度
        Display display = window.getWindowManager().getDefaultDisplay();
        wlp.width=display.getWidth()-20;
        wlp.gravity= Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);//窗口透明
        window.setAttributes(wlp);//设置窗口参数


    }
    public void setInfo(AudioBean bean){
        binding.tvTime.setText(bean.getTime());
        binding.tvTitle.setText(bean.getTitle());
        binding.tvPath.setText(bean.getPath());
        String size=calFileSize(bean.getFileLength());
        binding.tvSize.setText(size);

    }

    private String calFileSize(long fileLength) {
        DecimalFormat format=new DecimalFormat("#.00");
        if (fileLength>=1024*1024) {
            return format.format(fileLength*1.0/(1024*1024))+"MB";
        }
        if (fileLength>=1024) {
            return format.format(fileLength*1.0/1024)+"KB";
        }
        if (fileLength<1024){
            return fileLength+"B";
        }


        return "0KB";
    }
}
