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

public class KitapOkuma extends AppCompatActivity {
    private String[] kitaplar;
    private RatingBar rb_customColor;
    private String[] urller;
    String UrlLer;
    ListView lVkitaplar;
    static MediaPlayer mPlayer;

    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_okuma);
        String deger = WebServiceBook.getAllBooks();
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
        lVkitaplar=(ListView)findViewById(R.id.lVkitapOkuma);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        dialog = new SpotsDialog(getApplicationContext(),R.style.CustomProgressBar);
        ArrayAdapter<String> adapterListView=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,
                android.R.id.text1,books);
        lVkitaplar.setAdapter(adapterListView);
        lVkitaplar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playAudio(urller[position]);
            }
        });

//        Toast.makeText(this, WebServiceBook.getAllBooks(),Toast.LENGTH_LONG).show();
//        bb.duzenleme(deger);
//        Toast.makeText(this, WebServiceBook.getAllBooks(),Toast.LENGTH_LONG).show();
        rb_customColor = (RatingBar) findViewById(R.id.ratingBar5);

		/*
		 * For custom color only using layerdrawable to fill the star colors
		 */
        LayerDrawable stars = (LayerDrawable) rb_customColor
                .getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#26ce61"),
                PorterDuff.Mode.SRC_ATOP); // for filled stars
        stars.getDrawable(1).setColorFilter(Color.parseColor("#FFFF00"),
                PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars.getDrawable(0).setColorFilter(Color.CYAN,
                PorterDuff.Mode.SRC_ATOP); // for empty stars

        rb_customColor.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Toast.makeText(getApplicationContext(), String.valueOf(rating), Toast.LENGTH_SHORT).show();
            }
        });
        rb_customColor.setVisibility(View.INVISIBLE);
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
        rb_customColor.setVisibility(View.VISIBLE);
    }
    public void yeniKitapYukle(View view){
        startActivity(new Intent(this,SoundSave.class));
    }
}
