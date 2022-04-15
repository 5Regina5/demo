package com.example.newnewrecorder;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private SimpleDateFormat sdf;
    private Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] dizi = {android.Manifest.permission.RECORD_AUDIO,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE};


        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    dizi,
                    1234);
        }


        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        record = (Button) findViewById(R.id.record);
        stop.setEnabled(false);
        play.setEnabled(false);
        sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
        String time = sdf.format(new Date());
        File file = new File(Environment.getExternalStorageDirectory(),"/Music/"+time+".mp3" );

        /*
        java.io.File xmlFile = new java.io.File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/recording.3gp");
*/

        /*
        //calisan hali bu
       java.io.File xmlFile = new java.io.File((this
                .getApplicationContext().getFileStreamPath("firsdtauidio.mp3")
                .getPath()));
*/

        java.io.File xmlFile = new java.io.File(Environment.getExternalStorageDirectory(), "/Music/"+time+".mp3");



        //outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        //outputFile = file.getAbsolutePath();

        System.out.println("dosyayi kaydettigi yer: " + outputFile);


        myAudioRecorder = new MediaRecorder();



        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myAudioRecorder==null) {
                    myAudioRecorder=new MediaRecorder();
                }
                    myAudioRecorder.reset();
                    setRecorder();
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
                record.setEnabled(false);
                stop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });



        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    myAudioRecorder.stop();

                    myAudioRecorder = null;
                }catch (Exception e){
                    
                    e.printStackTrace();
                }
                record.setEnabled(true);
                stop.setEnabled(false);
                play.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Audio Recorder stopped", Toast.LENGTH_LONG).show();
            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
        });

    }

    private void setRecorder() {
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myAudioRecorder.setOutputFile(outputFile);
        sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
        String time = sdf.format(new Date());
        java.io.File xmlFile = new java.io.File(Environment.getExternalStorageDirectory(), "/Music/"+time+".mp3");
        outputFile = xmlFile.getAbsolutePath();
        if (!xmlFile.exists()) {
            try {
                xmlFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myAudioRecorder.setOutputFile(outputFile);
        //最大录制时间
        myAudioRecorder.setMaxDuration(10*60*1000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode == 1234){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                Toast.makeText(this,"izin var", Toast.LENGTH_SHORT).show();


            }

        }

    }

}
