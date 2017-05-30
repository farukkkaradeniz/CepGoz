package com.cepgoz.kou.cepgoz;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cepgoz.kou.cepgoz.arama.Consts;
import com.cepgoz.kou.cepgoz.arama.DataHolder;
import com.quickblox.auth.QBAuth;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.result.Result;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.users.result.QBUserResult;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class splash extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    public static QBChatService chatService;
    private static ArrayList<QBUser> users = new ArrayList<>();
    private volatile boolean resultReceived = true;
    private QBUser currentUser;
    String TAG = "AndroidRtc";
    AlertDialog dialog;
    private boolean backresultforclose=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dialog = new SpotsDialog(splash.this,R.style.CustomProgressBar);
        dialog.show();
        Thread mythread=new Thread(){

            @Override
            public void run() {
                try {
                    this.sleep(3000);
                } catch (InterruptedException e) {

                }
                finally {
                    splash.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor mPrefsEditor = pref.edit();
                            String username = pref.getString(PREF_USERNAME, "");
                            String password = pref.getString(PREF_PASSWORD, "");
                            dialog.dismiss();
                            if ((username.length()>3 && password.length()>5)) {
                                QBSettings.getInstance().fastConfigInit(Consts.APP_ID, Consts.AUTH_KEY, Consts.AUTH_SECRET);
                                QBChatService.setDebugEnabled(true);
                                if (!QBChatService.isInitialized()) {
                                    QBChatService.init(splash.this);
                                    chatService = QBChatService.getInstance();
                                }
                                giris(pref.getString(PREF_USERNAME, ""), pref.getString(PREF_PASSWORD, ""));
                            } else {
                                Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                                startActivity(intent);
                            }
                        }
                    });

                    finish();
                }
            }
        };
        mythread.start();


    }
    public void giris(String login,String password){
        //dialog.show();
        ArrayList<QBUser> users = DataHolder.getUsersList();
        boolean result = false;
        QBUser user = new QBUser(login,password);
        user.setId(users.get(0).getId());
        user.setFullName(users.get(0).getFullName());
        //createSession(user);
        for(int i=0;i<users.size();i++){
            if(users.get(i).getLogin().equals(login) && users.get(i).getPassword().equals(password)){
                user.setId(users.get(i).getId());
                user.setFullName(users.get(i).getFullName());
                createSession(user);
                result = true;
                break;
            }
        }
        if(!result){
            Toast.makeText(this, "Kullanıcı adı veya şifre hatası", Toast.LENGTH_LONG).show();
            SharedPreferences prev = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
            prev.edit().clear().commit();
        }
    }
    private void createSession(final QBUser user) {
        QBAuth.createSession(user.getLogin(), user.getPassword(), new QBCallbackImpl() {
            @Override
            public void onComplete(Result result) {
                if (result.isSuccess()) {
                    QBUsers.signIn(user.getLogin(), user.getPassword(), new QBCallbackImpl() {
                        @Override
                        public void onComplete(Result result) {
                            if (result.isSuccess()) {
                                Log.d(TAG, "signIn success");
                                QBUserResult qbUserResult = (QBUserResult) result;
                                currentUser = user;
                                DataHolder.setLoggedUser(currentUser);
                                onSuccess(user);
                                //startCallActivity(user.getLogin());
                            } else {
                                for (String s : result.getErrors()) {
                                    Log.d(TAG, "signIn error: " + s);
                                }
                            }
                        }
                    });
                } else {
                    for (String s : result.getErrors()) {
                        Log.d(TAG, "createSession error: " + s);
                    }
                }
            }
        });
    }
    public void onSuccess(final QBUser user){
        Log.d(TAG, "onSuccess create session with params");
        currentUser=user;
        DataHolder.setLoggedUser(currentUser);
        if (chatService.isLoggedIn()) {
            resultReceived = true;
            startCallActivity(user.getLogin());
            Log.d(TAG, "First if");
        } else {
            chatService.login(user, new QBEntityCallbackImpl<QBUser>() {

                @Override
                public void onSuccess() {
                    Log.d(TAG, "onSuccess login to chat");
                    resultReceived = true;

                    splash.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // showProgress(false);
                            dialog.dismiss();
                        }
                    });
                    startCallActivity(user.getLogin());
                    Log.d(TAG, "Else blocks");
                }

                @Override
                public void onError(List errors) {
                    resultReceived = true;

                    //showProgress(false);

                    Toast.makeText(splash.this, "Error when login", Toast.LENGTH_SHORT).show();
                    for (Object error : errors) {
                        Log.d(TAG, error.toString());
                    }
                }
            });
        }

    }
    private void startCallActivity(final String login) {
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor mPrefsEditor = pref.edit();
        mPrefsEditor.putString(PREF_USERNAME, login);
        mPrefsEditor.commit();
        startActivity(new Intent(getApplicationContext(),AnaEkran.class));
        /*Intent intent = new Intent(splash.this, CallActivity.class);
        intent.putExtra("login", login);
        startActivityForResult(intent, Consts.CALL_ACTIVITY_CLOSE);
        startActivity(intent);*/
    }



}
