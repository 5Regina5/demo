package com.example.musicreco.utils;

import android.media.MediaMetadataRetriever;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioInfoUtils {
    //获取音频相关内容工具类
    private MediaMetadataRetriever mediaMetadataRetriever;
    private AudioInfoUtils(){};
    private static AudioInfoUtils utils;
    public static AudioInfoUtils getInstance(){
        if (utils==null) {
            synchronized (AudioInfoUtils.class){
                if (utils==null) {
                    utils=new AudioInfoUtils();
                }
            }
        }
        return utils;
    }
    public  long getAudioFileDuration(String filePath){
        long duration=0;
        if (mediaMetadataRetriever==null) {
            mediaMetadataRetriever=new MediaMetadataRetriever();
        }
        mediaMetadataRetriever.setDataSource(filePath);
        String s=mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        duration=Long.parseLong(s);
        return duration;
    }
    public String getAudioFileFormatDuration(String format,long durlong){
        durlong-=24*3600*1000;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(durlong));
    }
    public String getAudioFileFormatDuration(long durlong){
        return getAudioFileFormatDuration("HH:mm:ss",durlong);

    }
    public String getAudioFileArtist(String filepath){
        if (mediaMetadataRetriever==null) {
            mediaMetadataRetriever=new MediaMetadataRetriever();
        }
        mediaMetadataRetriever.setDataSource(filepath);
        String artist=mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        return artist;
    }
    public void releaseRetriever(){
        if (mediaMetadataRetriever!=null) {
            mediaMetadataRetriever.release();
            mediaMetadataRetriever=null;
        }
    }
}
