package com.example.chris.nearbykos2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import control.Utils;
import session.SessionManager;

public class WelcomeActivity extends AppCompatActivity {

    private Handler handler;
    private SharedPreferences shared;
    private SessionManager session;
    private String userId;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        session = new SessionManager(WelcomeActivity.this);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(session.isLoggedIn()==false){
                    Intent intent=new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else if (session.isLoggedIn()==true){
                    Intent intent=new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },500);
    }

    @Override
    protected void onDestroy() {
        Utils.freeMemory();
        super.onDestroy();
        Utils.trimCache(this);
    }
}
