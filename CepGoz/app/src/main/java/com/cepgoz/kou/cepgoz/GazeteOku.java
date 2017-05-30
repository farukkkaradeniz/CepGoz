package com.cepgoz.kou.cepgoz;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.IOException;

import dmax.dialog.SpotsDialog;

public class GazeteOku extends AppCompatActivity {
    private String[] kitaplar;
    private String[] urller;
    String UrlLer;
    ListView lVkitaplar;
    static MediaPlayer mPlayer;

    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gazete_oku);
        String deger = WebServiceBook.getAllNewsPapers();
        BookProccess bb = new BookProccess();
        String[] _BooksAndUrls=bb.duzenleme(deger);
        String[] books=new String[_BooksAndUrls.length/2];
        String[] linkler=new String[_BooksAndUrls.length/2];
        int j=0;
        for(int i=0;i<_BooksAndUrls.length;i+=2){
            books[j]=_BooksAndUrls[i];
            linkler[j]=_BooksAndUrls[i+1];
            j++;
        }
        urller=linkler;
        lVkitaplar=(ListView)findViewById(R.id.lVgazeteOkuma);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        dialog = new SpotsDialog(getApplicationContext(),R.style.CustomProgressBar);
//        ArrayAdapter<String> adapterListView=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,
//                android.R.id.text1,books);
        ArrayAdapter<String> adapterListView = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,
                android.R.id.text1,books);
        lVkitaplar.setAdapter(adapterListView);
        lVkitaplar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playAudio(urller[position]);
            }
        });

       // Toast.makeText(this, WebServiceBook.getAllBooks(), Toast.LENGTH_LONG).show();
        bb.duzenleme(deger);
        //Toast.makeText(this, WebServiceBook.getAllBooks(),Toast.LENGTH_LONG).show();

    }
    public void playAudio(String url){
        // dialog.show();
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(url);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mPlayer.start();
        // dialog.dismiss();
    }
    public void prevTrack(View view){
        int ilerizaman=3500;
        if(0<=mPlayer.getCurrentPosition()-ilerizaman){
            mPlayer.seekTo(mPlayer.getCurrentPosition()-ilerizaman);
        }
    }
    public void forwardTrack(View view){
        Toast.makeText(getApplicationContext(),"ileri",Toast.LENGTH_LONG).show();
        int forward=3500;
        int lengthTrack = mPlayer.getDuration();
        if(lengthTrack>=mPlayer.getCurrentPosition()+forward){
            mPlayer.seekTo(mPlayer.getCurrentPosition()+forward);
        }
    }
    public void stopTrack(View view){
        if(mPlayer!=null && mPlayer.isPlaying()){
            mPlayer.stop();
            mPlayer.release();
        }
    }
    public void yenigazeteYukle(View view){
        startActivity(new Intent(this,SoundSaveGazete.class));
    }
}
