package com.cepgoz.kou.cepgoz;

import android.Manifest;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cepgoz.kou.cepgoz.arama.CallActivity;
import com.cepgoz.kou.cepgoz.arama.Consts;
import com.cepgoz.kou.cepgoz.arama.DataHolder;
import com.quickblox.chat.QBChatService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AnaEkran extends AppCompatActivity {
    int check = 1000;
    ImageView videocall,audiocall,barcode,bookreader;
    ArrayList<String> results;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_USER_STATE="userState";
    private MyService s;
    private int PERMISSION_INTERNET=123;
    GetTemperature getTemperature;
    TextToSpeech t1;
    private int PERMISSION_GROUP=123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent= new Intent(this, MyService.class);
      izinler();

        bindService(intent, mConnection,
                Context.BIND_AUTO_CREATE);
        setContentView(R.layout.activity_ana_ekran);
        getTemperature = new GetTemperature(getApplicationContext(),"kocaeli");
        videocall = (ImageView) findViewById(R.id.imageViewVideoCall);
        audiocall = (ImageView) findViewById(R.id.imageViewAudioCall);
        bookreader = (ImageView) findViewById(R.id.imageViewBook);
        barcode = (ImageView) findViewById(R.id.imageViewBarcode);
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor mPrefsEditor = pref.edit();
        String userState = pref.getString(PREF_USER_STATE, "");
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(AnaEkran.this,
                    Manifest.permission.INTERNET)){
            }else{
                ActivityCompat.requestPermissions(AnaEkran.this,
                        new String[]{Manifest.permission.INTERNET},PERMISSION_INTERNET);
            }
        }
        if((userState.equals("1"))) {
            //CeviriBaslat();
        }
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    public void izinler(){

        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(AnaEkran.this,
                    Manifest.permission.CAMERA)){
            }else{
                ActivityCompat.requestPermissions(AnaEkran.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_CONTACTS,
                                Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA},PERMISSION_GROUP);
            }
        }

        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(AnaEkran.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(AnaEkran.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_GROUP);
            }
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(AnaEkran.this,
                    Manifest.permission.READ_CONTACTS)){
            }else{
                ActivityCompat.requestPermissions(AnaEkran.this,
                        new String[]{Manifest.permission.READ_CONTACTS},PERMISSION_GROUP);
            }
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(AnaEkran.this,
                    Manifest.permission.READ_PHONE_STATE)){
            }else{
                ActivityCompat.requestPermissions(AnaEkran.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_GROUP);
            }
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            MyService.MyBinder b = (MyService.MyBinder) binder;
            s = b.getService();
            startService(new Intent(getApplicationContext(), MyService.class));
        }
        public void onServiceDisconnected(ComponentName className) {
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent= new Intent(this, MyService.class);
        bindService(intent, mConnection,
                Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"Ana Sayfa",Toast.LENGTH_SHORT).show();
                break;
            case R.id.sayfa2:
                Toast.makeText(getApplicationContext(),"Sayfa2",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cikis:{
                SharedPreferences prev = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                prev.edit().clear().commit();
                if (QBChatService.isInitialized()){
                    //rtcClient.destroy();
                    //QBChatService.getInstance().logout();
                    finish();
                    Intent intent = new Intent(getApplicationContext(),ActivityMain.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(),ActivityMain.class);
                    startActivity(intent);
                }
            }

            break;
        }
        return false;
    }
    private void CeviriBaslat() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Haydi birşeyler söyle!");
        startActivityForResult(i, check);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == check && resultCode == RESULT_OK) {
            results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        }
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor mPrefsEditor = pref.edit();
        String username = pref.getString(PREF_USERNAME, "");
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).toLowerCase().equals("bir") || results.get(i).toLowerCase().equals(1)){
                Intent intent = new Intent(getApplicationContext(), CallActivity.class);
                intent.putExtra("login", username);
                startActivityForResult(intent, Consts.CALL_ACTIVITY_CLOSE);
                startActivity(intent);
            }
            if (results.get(i).toLowerCase().equals("iki") || results.get(i).toLowerCase().equals(2)){
                startActivity(new Intent(getApplicationContext(), GazeteOku.class));
            }
            if (results.get(i).toLowerCase().equals("üç") || results.get(i).toLowerCase().equals(3)){
                startActivity(new Intent(getApplicationContext(),KitapOkuma.class));
            }
            if (results.get(i).toLowerCase().equals("dört") || results.get(i).toLowerCase().equals(4)){
                startActivity(new Intent(this,BarcodeScanner.class));
            }if (results.get(i).toLowerCase().equals("beş") || results.get(i).toLowerCase().equals(5)){
                MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                    @Override
                    public void gotLocation(Location location){
                        String latitude = String.valueOf(location.getLatitude());
                        String longitude = String.valueOf(location.getLongitude());
                        WebServiceLocation wbs = new WebServiceLocation();
                        wbs.insertNewLocation(DataHolder.getLoggedUser().getId(),longitude,latitude);
                        String result = wbs.findCloseHelper(DataHolder.getLoggedUser().getId());
                        wbs.setNotificationLocationState(Integer.parseInt(result), 1);
                    }
                };
                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(this, locationResult);
                t1.speak("En yakın kullanıcıya istek gönderildi.", TextToSpeech.QUEUE_FLUSH, null);
            } if (results.get(i).toLowerCase().equals("altı") || results.get(i).toLowerCase().equals(6)){
                getWeather();
            }
        }
    }
    public void videoCallImageView(View view){
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor mPrefsEditor = pref.edit();
        String username = pref.getString(PREF_USERNAME, "");
        Intent intent = new Intent(getApplicationContext(), CallActivity.class);
        intent.putExtra("login", username);
        startActivityForResult(intent, Consts.CALL_ACTIVITY_CLOSE);
        startActivity(intent);     }
    public void audioCallImageView(View view){
        startActivity(new Intent(getApplicationContext(), GazeteOku.class));
    }
    public void bookImageView(View view){
        startActivity(new Intent(getApplicationContext(), KitapOkuma.class));
    }
    public void barcodeImageView(View view){
        startActivity(new Intent(getApplicationContext(), BarcodeScanner.class));
    }

    public void locationImageView(View view){
     MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                WebServiceLocation wbs = new WebServiceLocation();
                wbs.insertNewLocation(DataHolder.getLoggedUser().getId(),longitude,latitude);
                String result = wbs.findCloseHelper(DataHolder.getLoggedUser().getId());
                wbs.setNotificationLocationState(Integer.parseInt(result), 1);
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
        t1.speak("En yakın kullanıcıya istek gönderildi.", TextToSpeech.QUEUE_FLUSH, null);
    }

    public void getWeather() {
        Calendar c = Calendar.getInstance();
        String metin = "time " + String.valueOf(c.get(Calendar.HOUR)) + " and " + String.valueOf(c.get(Calendar.MINUTE)) +
                "Sıcaklık" + getTemperature.getCurrentTemperatureField();
        Toast.makeText(getApplicationContext(), metin, Toast.LENGTH_SHORT).show();
        t1.speak(metin, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void havaDurumuImageView(View view){

        getWeather();

    }


}
