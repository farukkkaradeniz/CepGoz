package com.cepgoz.kou.cepgoz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cepgoz.kou.cepgoz.arama.Consts;
import com.cepgoz.kou.cepgoz.arama.DataHolder;
import com.cepgoz.kou.cepgoz.arama.Id;
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

/**
 * Created by Lenovo on 31.12.2015.
 */
public class ActivityMain extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_USER_STATE="userState";
    EditText edtuserName,edtpassword;
    Button btnlogin;
    public static QBChatService chatService;
    private static ArrayList<QBUser> users = new ArrayList<>();
    private volatile boolean resultReceived = true;
    private QBUser currentUser;
    String TAG = "AndroidRtc";
    AlertDialog dialog;
    private boolean backresultforclose=false;
    CheckBox cbRememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        edtuserName = (EditText)findViewById(R.id.edtActMainUserName);
        edtpassword = (EditText)findViewById(R.id.edtActMainPassword);
        btnlogin = (Button)findViewById(R.id.btnActMainLogin);
        cbRememberMe = (CheckBox)findViewById(R.id.checkBoxRememberMe);
        QBSettings.getInstance().fastConfigInit(Consts.APP_ID, Consts.AUTH_KEY, Consts.AUTH_SECRET);
        QBChatService.setDebugEnabled(true);
        if (!QBChatService.isInitialized()){
            QBChatService.init(this);
            chatService = QBChatService.getInstance();
        }
        dialog = new SpotsDialog(ActivityMain.this,R.style.CustomProgressBar);
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor mPrefsEditor = pref.edit();
        String username = pref.getString(PREF_USERNAME, "");
        String password = pref.getString(PREF_PASSWORD, "");
        if((username.length()>2&& password.length()>5)) {
            giris(pref.getString(PREF_USERNAME, ""),pref.getString(PREF_PASSWORD, ""));
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
                                Id.id = currentUser.getId();
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

                    ActivityMain.this.runOnUiThread(new Runnable() {
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

                    Toast.makeText(ActivityMain.this, "Error when login", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(getApplicationContext(), AnaEkran.class));
        /*Intent intent = new Intent(ActivityMain.this, CallActivity.class);
        intent.putExtra("login", login);
        startActivityForResult(intent, Consts.CALL_ACTIVITY_CLOSE);
        startActivity(intent);*/
    }
    public void login(View view){
        dialog.show();
        ArrayList<QBUser> users = DataHolder.getUsersList();
        String login = edtuserName.getText().toString();
        String password = edtpassword.getText().toString();
        boolean result = false;
        QBUser user = new QBUser(login,password);
        user.setId(users.get(0).getId());
        user.setFullName(users.get(0).getFullName());
        if(cbRememberMe.isChecked()) {
            SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor mPrefsEditor = pref.edit();
            mPrefsEditor.putString(PREF_USERNAME, login);
            mPrefsEditor.putString(PREF_PASSWORD, password);
            if(cbRememberMe.isChecked())
            mPrefsEditor.putString(PREF_USER_STATE,"1");
            mPrefsEditor.commit();
        }
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
            SharedPreferences prev = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
            prev.edit().clear().commit();
            Toast.makeText(this,"Kullanıcı adı veya şifre hatası",Toast.LENGTH_LONG).show();
            dialog.dismiss();
            Toast.makeText(getApplicationContext(),"Kullanıcı adı ve şifreyi kontrol ediniz...",Toast.LENGTH_LONG).show();
        }
    }
    public void giris(String login,String password){
        dialog.show();
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
        if(!result)
            Toast.makeText(this,"Kullanıcı adı veya şifre hatası",Toast.LENGTH_LONG).show();
    }
    public void Singup(View view){
        dialog.dismiss();
        Intent intent = new Intent(this,SingupActivity.class);
        startActivity(intent);
    }

    public void AppExit()
    {
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }
    @Override
    public void onBackPressed() {
        if (backresultforclose){

            AppExit();
        }else{
            Toast.makeText(getApplicationContext(),"Çıkmak için geriye iki kez basınız...",Toast.LENGTH_SHORT).show();
            backresultforclose=true;
            startTime();
        }
    }
    private Handler mHandler = new Handler();

    private void startTime() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 2000);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            backresultforclose=false;
            mHandler.postDelayed(this, 2000);
        }
    };
}
