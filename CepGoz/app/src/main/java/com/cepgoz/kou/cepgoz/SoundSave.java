package com.cepgoz.kou.cepgoz;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class SoundSave extends AppCompatActivity {
    ImageView mic,stop,upload;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private AudioManager audio;
    EditText edtBookName,edtWriterName;
    static final String FTP_HOST= "FTP_HOST";
    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "FTP_USER";
    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="FTP_PASS";
    private int PERMISSION_GROUP=12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_save);
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(SoundSave.this,
                    Manifest.permission.RECORD_AUDIO)){
            }else{
                ActivityCompat.requestPermissions(SoundSave.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},PERMISSION_GROUP);
            }
        }
        izinler();
        mic=(ImageView)findViewById(R.id.imageViewSoundSaveStartRecorder);
        stop=(ImageView)findViewById(R.id.imageViewSoundSaveStopRecorder);
        upload=(ImageView)findViewById(R.id.imageViewSoundSaveUploadRecord);
        stop.setVisibility(View.INVISIBLE);
        upload.setVisibility(View.INVISIBLE);
        edtBookName=(EditText)findViewById(R.id.edtSoundSaveBookName);
        edtWriterName=(EditText)findViewById(R.id.edtSoundSaveWriterName);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        for(int i=0;i<8;i++) {
            audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        }
        //outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/sd.mp3";
        myAudioRecorder=new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
    }

    public void izinler(){

        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.MODIFY_AUDIO_SETTINGS)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(SoundSave.this,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS)){
            }else{
                ActivityCompat.requestPermissions(SoundSave.this,
                        new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS},PERMISSION_GROUP);
            }
        }

        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(SoundSave.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            }else{
                ActivityCompat.requestPermissions(SoundSave.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_GROUP);
            }
        }


    }

    public void startAudioRecorder(View view){
        if (edtBookName.length()>5 && edtWriterName.length()>3){
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+edtBookName.getText()+".mp3";
        myAudioRecorder.setOutputFile(outputFile);

            try {
            Toast.makeText(this,"Kayıt Başladı",Toast.LENGTH_SHORT).show();
            mic.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        }else Toast.makeText(getApplicationContext(),"Lütfen kitap adı ve yazar adını kontrol ediniz...",Toast.LENGTH_SHORT).show();

    }

    public void stopAudioRecorder(View view){

        Toast.makeText(this,"Kayıt Bitti",Toast.LENGTH_SHORT).show();
        stop.setVisibility(View.INVISIBLE);
        upload.setVisibility(View.VISIBLE);
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder  = null;

    }

    public void uploadAudioRecord(View view){
        Toast.makeText(this,"Yükleniyor",Toast.LENGTH_SHORT).show();
        File f = new File("/sdcard/logo.png");
        uploadFile(f);

    }
    public void uploadFile(File fileName){
        if(edtWriterName.length()>5&&edtBookName.length()>5){
        Thread mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                FTPClient client = new FTPClient();
                try {
                    client.connect(FTP_HOST, 21);
                    client.login(FTP_USER, FTP_PASS);
                    client.setType(FTPClient.TYPE_BINARY);
                    client.changeDirectory("/httpdocs/deneme/");
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+edtBookName.getText().toString()+".mp3";
                    File f = new File(path);
                    if (!f.exists()) {
                        f.createNewFile();
                    }
                    client.upload(f, new MyTransferListener());
                    String link = "http://farukkaradeniz.com/deneme/"+edtBookName.getText().toString()+".mp3";
                    WebServiceBook.insertNewBook(edtBookName.getText().toString(), edtWriterName.getText().toString(), link);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        client.disconnect(true);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }

            }
        });
        mythread.start();
        }
        else Toast.makeText(getApplicationContext(),"Lütfen kitap adı ve yazar adını kontrol ediniz",Toast.LENGTH_SHORT).show();

    }
    /*******  Used to file upload and show progress  **********/
    public class MyTransferListener implements FTPDataTransferListener {
        public void started() {

        }
        public void transferred(int length) {

        }
        public void completed() {

        }
        public void aborted() {

        }
        public void failed() {

        }
    }

}
