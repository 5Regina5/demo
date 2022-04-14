package com.example.musicreco.audio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;

import com.example.musicreco.R;
import com.example.musicreco.bean.AudioBean;
import com.example.musicreco.databinding.ActivityAudioListBinding;
import com.example.musicreco.utils.AudioInfoDialog;
import com.example.musicreco.utils.AudioInfoUtils;
import com.example.musicreco.utils.Contants;
import com.example.musicreco.utils.DialogUtils;
import com.example.musicreco.utils.RenameDialog;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AudioListActivity extends AppCompatActivity {
    private ActivityAudioListBinding binding;
    private List<AudioBean>mDatas;
    private AudioListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAudioListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //设置数据源和适配器
        mDatas=new ArrayList<>();
        adapter=new AudioListAdapter(this,mDatas);
        binding.audioLv.setAdapter(adapter);
        //加载数据
        loadDatas();
//设置监听事件
        setEvents();
        }

    private void setEvents() {
        adapter.setOnItemPlayClickListener(playClickListener);
        binding.audioLv.setOnItemLongClickListener(longClickListener);
    }
    AdapterView.OnItemLongClickListener longClickListener=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            showPopMenu(view,position);
            return false;
        }
    };

    private void showPopMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.RIGHT);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.audio_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_info:
                        showFileInfoDialog(position);
                        break;
                    case R.id.menu_del:
                        deleteFileByPos(position);
                        break;
                    case R.id.menu_rename:
                        showRenameDialog(position);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();

    }
//详情对话框
    private void showFileInfoDialog(int position) {
        AudioBean bean=mDatas.get(position);
        AudioInfoDialog dialog=new AudioInfoDialog(this);
        dialog.show();
        dialog.setDialogWidth();
        dialog.setInfo(bean);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void showRenameDialog(int position) {
        AudioBean bean=mDatas.get(position);
        String title=bean.getTitle();
        RenameDialog dialog=new RenameDialog(this);
        dialog.show();
        dialog.setDialogWidth();
        dialog.setTipText(title);
        dialog.setOnEnsureListener(new RenameDialog.OnEnsureListener() {
            @Override
            public void OnEnsure(String msg) {
                renameByPosition(msg,position);
            }
        });
    }

    private void renameByPosition(String msg, int position) {
        AudioBean audioBean = mDatas.get(position);
        if (audioBean.getTitle().equals(msg)) {
            return;
        }
        String path = audioBean.getPath();
        String fileSuffix = audioBean.getFileSuffix();
        File srcFile = new File(path);//原来的文件
        //修改路径
        String destPath=srcFile.getParent()+File.separator+msg+fileSuffix;
        File destFile=new File(destPath);
        //进行重命名
        srcFile.renameTo(destFile);
        //从内存修改
        audioBean.setTitle(msg);
        audioBean.setPath(destPath);
        adapter.notifyDataSetChanged();
    }

    private void deleteFileByPos(int position) {
        AudioBean bean=mDatas.get(position);
        String title=bean.getTitle();
        String path=bean.getPath();
        DialogUtils.showNormalDialog(this, "提示信息", "是否确定删除指定文件",
                "确定", new DialogUtils.OnLeftClicklListener() {
                    @Override
                    public void onLeftCLick() {
                        File file =new File(path);
                        file.getAbsoluteFile().delete();
                        mDatas.remove(bean);
                        adapter.notifyDataSetChanged();
                    }
                }, "取消", new DialogUtils.OnRightClicklListener() {
                    @Override
                    public void onRightCLick() {

                    }
                });
    }

    AudioListAdapter.OnItemPlayClickListener playClickListener=new AudioListAdapter.OnItemPlayClickListener() {
        @Override
        public void onItemPlayClick(AudioListAdapter adapter, View convertView, View playView, int position) {

        }
    };


    private void loadDatas() {
        //1.获取指定路径下的音频文件
        File fetchFile = new File(Contants.PATH_FETCH_DIR_RECORD);
        File[] listFiles=fetchFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (new File(dir,name).isDirectory()) {
                    return false;
                }
                if (name.endsWith(".mp3")||name.endsWith(".amr")) {
                    return true;
                }
                return false;
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        AudioInfoUtils audioInfoUtils = AudioInfoUtils.getInstance();
        //2.遍历数组当中的文件
        for (int i = 0; i < listFiles.length; i++) {
            File audioFile=listFiles[i];
            String fname = audioFile.getName();
            String title=fname.substring(0,fname.lastIndexOf("."));
            String suffix=fname.substring(fname.lastIndexOf("."));
            //获取文件最后修改时间
            long flastMod=audioFile.lastModified();
            String time=sdf.format(flastMod);
            //获取文件字节数
            long flength=audioFile.length();
            //获取文件路径
            String audioPath=audioFile.getAbsolutePath();
            long duration=audioInfoUtils.getAudioFileDuration(audioPath);
            String formatDuration = audioInfoUtils.getAudioFileFormatDuration(duration);
            AudioBean audioBean = new AudioBean(i + "", title, time, formatDuration, audioPath, duration, flastMod, suffix, flength);
            mDatas.add(audioBean);
        }
        audioInfoUtils.releaseRetriever();//释放多媒体资料的资源对象
        //按时间排序
        Collections.sort(mDatas, new Comparator<AudioBean>() {
            @Override
            public int compare(AudioBean o1, AudioBean o2) {
                if (o1.getLastModified()<o2.getLastModified()) {
                    return 1;
                }else if (o1.getLastModified()==o2.getLastModified()) {
                    return 0;
                }
                return -1;
            }
        });
        adapter.notifyDataSetChanged();
    }
}