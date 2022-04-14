package com.example.musicreco.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {

    public interface OnLeftClicklListener{
        public void onLeftCLick();
    }
    public interface OnRightClicklListener{
        public void onRightCLick();
    }

    public static void showNormalDialog(Context context,String title,String msg
    ,String leftBtn,OnLeftClicklListener leftListener,String rightBtn,OnRightClicklListener
                                        rightListener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(msg);
        builder.setNegativeButton(leftBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(leftListener!=null){
                    leftListener.onLeftCLick();
                    dialogInterface.cancel();
                }
            }
        });
        builder.setPositiveButton(rightBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (rightListener!=null) {
                    rightListener.onRightCLick();
                    dialogInterface.cancel();
                }
            }
        });
        builder.create().show();
    }
}
