package com.example.musicreco.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.musicreco.R;
import com.example.musicreco.databinding.DialogRenameBinding;

import java.util.List;

public class RenameDialog extends Dialog implements View.OnClickListener {

    private DialogRenameBinding binding;
    //创建点击确定执行的接口函数
    public interface OnEnsureListener{
        public void OnEnsure(String msg);//填进去的重命名名称
    }
   private OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public RenameDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DialogRenameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.dialogRenameBtnCancel.setOnClickListener(this);
        binding.dialogRenameBtnEnsure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_rename_btn_cancel:
                cancel();
                break;
            case R.id.dialog_rename_btn_ensure:
                if (onEnsureListener != null) {
                    String msg = binding.dialogRenameEt.getText().toString().trim();
                    onEnsureListener.OnEnsure(msg);
                }
                cancel();
                break;
        }
    }
        //设置EditText显示原来的标题名称
        public void setTipText(String oldText){
            binding.dialogRenameEt.setText(oldText);
        }
        //对话框宽度
        public void setDialogWidth(){
        //获取当前屏幕窗口对象
            Window window=getWindow();
            //获取窗口信息参数
            WindowManager.LayoutParams wlp=window.getAttributes();
            //获取屏幕宽度
            Display display = window.getWindowManager().getDefaultDisplay();
            wlp.width=display.getWidth();
            wlp.gravity= Gravity.BOTTOM;
            window.setBackgroundDrawableResource(android.R.color.transparent);//窗口透明
            window.setAttributes(wlp);//设置窗口参数
            //自动弹出软键盘
            handler.sendEmptyMessageDelayed(1,100);
        }
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            InputMethodManager manager= (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
            return false;
        }
    });

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {
        super.onProvideKeyboardShortcuts(data, menu, deviceId);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
