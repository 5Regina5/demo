package com.example.musicreco.audio;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.musicreco.R;
import com.example.musicreco.bean.AudioBean;
import com.example.musicreco.databinding.ItemAudioBinding;

import java.util.List;

public class AudioListAdapter extends BaseAdapter{
    private Context context;
    private List<AudioBean>mDatas;
    public interface OnItemPlayClickListener{
        void  onItemPlayClick(AudioListAdapter adapter,View convertView,View playView,int position);
    }
    private OnItemPlayClickListener onItemPlayClickListener;

    public void setOnItemPlayClickListener(OnItemPlayClickListener onItemPlayClickListener) {
        this.onItemPlayClickListener = onItemPlayClickListener;
    }

    public AudioListAdapter(Context context, List<AudioBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.item_audio,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        //获取指定位置数据控件设置
        AudioBean audioBean=mDatas.get(position);
        holder.ab.tvTime.setText(audioBean.getTime());
        holder.ab.tvDuration.setText(audioBean.getDuration());
        holder.ab.tvTitle.setText(audioBean.getTitle());
        if (audioBean.isPlaying()) {
            holder.ab.lvControll.setVisibility(View.VISIBLE);
            holder.ab.pb.setMax(100);
            holder.ab.pb.setProgress(audioBean.getCurrentProgress());
            holder.ab.ivPlay.setImageResource(R.mipmap.red_pause);
        }else{
            holder.ab.ivPlay.setImageResource(R.mipmap.red_play);
            holder.ab.lvControll.setVisibility(View.GONE);
        }
        View itemView=convertView;
        //点击图标可以播放或者暂停录音内容
        holder.ab.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemPlayClickListener!=null) {
                    onItemPlayClickListener.onItemPlayClick(AudioListAdapter.this,itemView,v,position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        ItemAudioBinding ab;
        public ViewHolder(View v){
            ab=ItemAudioBinding.bind(v);
        }
    }
}

