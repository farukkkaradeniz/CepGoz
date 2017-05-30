package com.cepgoz.kou.cepgoz;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cepgoz.kou.cepgoz.arama.CallActivity;
import com.cepgoz.kou.cepgoz.arama.Consts;
import com.cepgoz.kou.cepgoz.arama.DataHolder;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.result.Result;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.users.result.QBUserResult;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class SingupActivity extends AppCompatActivity {

    EditText edtfullname,edtemail,edtpassword,edtpaswordcomfirm,edtlogin;
    Spinner spnTags;
    Button btnSingup;
    Boolean emtypCheck=false;
    private String array_spinner[];
    private QBUser currentUser;
    private volatile boolean resultReceived = true;
    String TAG = "AndroidRtc";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        edtemail = (EditText)findViewById(R.id.edtActSingupEmail);
        edtfullname = (EditText)findViewById(R.id.edtActSingupFullName);
        edtpassword = (EditText)findViewById(R.id.edtActSingupPassword);
        edtpaswordcomfirm = (EditText)findViewById(R.id.edtActSingupPasswordComfirm);
        edtlogin = (EditText)findViewById(R.id.edtActSingupLogin);
        btnSingup = (Button)findViewById(R.id.btnActSingup);
        spnTags = (Spinner)findViewById(R.id.spnActSingupTags);
        array_spinner=new String[1];
        array_spinner[0]="AndroidRtc";
        Spinner s = (Spinner) findViewById(R.id.spnActSingupTags);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);

    }

    public void SingUp(View v){

        AlertDialog dialog = new SpotsDialog(SingupActivity.this, R.style.CustomProgressBar);
        dialog.show();
        QBUser user = new QBUser();
        user.setFullName(edtfullname.getText().toString());
        user.setEmail(edtemail.getText().toString());
        user.setLogin(edtlogin.getText().toString());
        user.setPassword(edtpassword.getText().toString());
        Toast.makeText(this, user.getPassword(), Toast.LENGTH_LONG).show();
        StringifyArrayList<String> tagsList = new StringifyArrayList<>();
        tagsList.add(array_spinner[0]);
        user.setTags(tagsList);
        final QBUser singupuser = user;
        QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                QBUsers.signUp(singupuser, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser user, Bundle args) {
                        // success
                        user.setPassword(edtpassword.getText().toString());
                        //Toast.makeText(this,strI,Toast.LENGTH_LONG).show();
                        createSession(user);
                    }

                    @Override
                    public void onError(List<String> errors) {
                        for (String s : errors) {
                            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onError(List<String> errors) {
                // errors
                for (String s : errors) {
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                }
            }
        });
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
                                QBUserResult qbUserResult = (QBUserResult) result;
                                currentUser = user;
                                DataHolder.setLoggedUser(currentUser);
                                onSuccess(user);
                                //startCallActivity(user.getLogin());
                            } else {
                                for (String s : result.getErrors()) {
                                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });


                } else {
                    for (String s : result.getErrors()) {
                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    public void onSuccess(final QBUser user){
        currentUser=user;
        DataHolder.setLoggedUser(currentUser);
        if (ActivityMain.chatService.isLoggedIn()) {
            resultReceived = true;
            startCallActivity(user.getLogin());
        } else {
            ActivityMain.chatService.login(user, new QBEntityCallbackImpl<QBUser>() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "onSuccess login to chat");
                    resultReceived = true;
                    SingupActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // showProgress(false);
                        }
                    });
                    DataHolder.setLoggedUser(user);
                    startCallActivity(user.getLogin());
                }
                @Override
                public void onError(List errors) {
                    resultReceived = true;

                    //showProgress(false);

                    Toast.makeText(SingupActivity.this, "Error when login", Toast.LENGTH_SHORT).show();
                    for (Object error : errors) {
                        Log.d(TAG, error.toString());
                    }
                }
            });
        }
    }
    private void startCallActivity(String login) {
        WebService.insertnewuser(DataHolder.getLoggedUser().getId());
        Intent intent = new Intent(SingupActivity.this, CallActivity.class);
        intent.putExtra("login", login);
        startActivityForResult(intent, Consts.CALL_ACTIVITY_CLOSE);
        startActivity(intent);
    }


}
