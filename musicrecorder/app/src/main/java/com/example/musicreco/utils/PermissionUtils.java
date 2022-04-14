package com.example.musicreco.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.musicreco.StartSystemPageUtils;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    private static PermissionUtils permissionUtils;
    private PermissionUtils(){}
    private final int mRequestCode = 100;//权限请求码
    //接口
    public interface OnPermissionCallbackListener{
        void onGranted();//表示权限会执行的方法
        void onDenied(List<String>deniedPermissions);
    }
    private OnPermissionCallbackListener mListener;
    public static PermissionUtils getInstance(){
        if (permissionUtils==null){
            synchronized (PermissionUtils.class){
                if (permissionUtils==null){
                    permissionUtils=new PermissionUtils();
                }
            }
        }
        return permissionUtils;
    }

    public void onRequestPermission(Activity context,String []permissions,OnPermissionCallbackListener listener){
        mListener=listener;
        //判断版本，6.0以上需要申请
        if (Build.VERSION.SDK_INT>= 23){
            //创建集合，将用户之前未授予的权限放到集合中
            List<String> mPermissionList = new ArrayList<>();
            //逐个判断权限是否通过授权
            for (int i=0;i< permissions.length;i++){
                int res=ContextCompat.checkSelfPermission(context,permissions[i]);
                if(res != PackageManager.PERMISSION_GRANTED){
                    mPermissionList.add(permissions[i]);
                }
            }
            //申请权限
            if(mPermissionList.size()>0){
                String[] permission_arr=mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(context,permission_arr,mRequestCode);
            }else{
                //说明权限通过
                mListener.onGranted();
            }
        }
    }

    public void onRequestPermissionsResult(Activity context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
       if(requestCode==mRequestCode){
           List<String>deniedPermissions=new ArrayList<>();
           if(grantResults.length>0){
               for (int i=0;i< grantResults.length;++i){
                   if(grantResults[i] !=PackageManager.PERMISSION_GRANTED){
                       deniedPermissions.add(permissions[i]);
                   }
               }
           }
           if(deniedPermissions.size()==0){
               mListener.onGranted();
           }else{
               mListener.onDenied(deniedPermissions);
           }

       }else{
           //所有权限都接收了
           mListener.onGranted();
       }
    }
//提示用户设置页面开启权限
    public void showDialogTipUserGotoAppSetting(Activity context){
        DialogUtils.showNormalDialog(context, "提示信息", "已经禁用权限，请手动申请", "取消",
                new DialogUtils.OnLeftClicklListener() {
                    @Override
                    public void onLeftCLick() {
                        context.finish();
                    }
                }, "确定", new DialogUtils.OnRightClicklListener() {
                    @Override
                    public void onRightCLick() {
                        StartSystemPageUtils.goToAppSetting(context);
                        context.finish();
                    }
                });
    }
}
