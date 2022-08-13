package com.example.myapplication;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;

public class SplashPageActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private final String EMAIL = "email";
    private final String ACCOUNT_MODE = "mode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.splash_activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        },2500);
    }
    private void startApp(){
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.getString(EMAIL,null) != null){
            switch (sp.getString(ACCOUNT_MODE, null)) {
                case "admin": {
                    Intent intent = new Intent(getApplicationContext(), AdminMainAppActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "user": {
                    Intent intent = new Intent(getApplicationContext(), UserMainAppActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        }else{
            Intent intent = new Intent(getApplicationContext() ,UserConnectingPortViewActivity.class);
            startActivity(intent);
            finish();
        }
    }
}