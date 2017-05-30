package com.cepgoz.kou.cepgoz;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.cepgoz.kou.cepgoz.arama.Id;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by faruk on 3.5.2016.
 */
public class MyService extends Service {
    private final IBinder mBinder = new MyBinder();
    private ArrayList<String> list = new ArrayList<String>();
    private static Timer timer = new Timer();
    private static long UPDATE_INTERVAL = 1*5*1000;  //default

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        _startService();
        String state = WebService.getNotificationState(Id.id);
        if (state.equals("1"))
        GenerateNotification.bildirimGoster(getApplicationContext());
//        return Service.START_STICKY;
        return Service.START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    private void _startService() {
        timer.scheduleAtFixedRate(
                new TimerTask() {

                    public void run() {
                        String state = WebService.getNotificationState(Id.id);
                        if (state.equals("1"))
                        GenerateNotification.bildirimGoster(getApplicationContext());
                        WebServiceLocation wbs = new WebServiceLocation();
                        String locationstate=wbs.getNotificationLocationState(Id.id);
                        if (locationstate.equals("1"))
                        GenerateNotification.bildirimGosterLokasyon(getApplicationContext());
                    }
                }, 1000,UPDATE_INTERVAL);
    }

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }
}

