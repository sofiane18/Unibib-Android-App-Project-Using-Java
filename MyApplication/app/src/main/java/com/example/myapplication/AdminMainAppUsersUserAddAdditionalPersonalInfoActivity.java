package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class AdminMainAppUsersUserAddAdditionalPersonalInfoActivity extends AppCompatActivity {
    private EditText birthDate,phone,address;
    private Button next,back;
    private String ROOT_URL = "http://192.168.1.106/";
    private Bundle extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.admin_main_app_users_user_add_additional_personal_info_activity);

        Intent intent = getIntent();
        extra = (Bundle) intent.getBundleExtra("userInfo");

        birthDate = findViewById(R.id.birthDate);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);

        next = findViewById(R.id.nextButton);
        back = findViewById(R.id.admin_book_add_place_back_arrow);
        next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int i=0;
                if(String.valueOf(birthDate.getText()).equals("")){
                    birthDate.setError("It seems you missed to enter your birthDate");
                    birthDate.requestFocus();
                    i++;
                }
                if(String.valueOf(phone.getText()).equals("")){
                    phone.setError("It seems you missed to enter your phone number");
                    phone.requestFocus();
                    i++;
                }

                if(String.valueOf(address.getText()).equals("")){
                    address.setError("It seems you missed to enter your address");
                    address.requestFocus();
                    i++;
                }


                if (i==0){
                    extra.putString("birthDate", String.valueOf(birthDate.getText()));
                    extra.putString("phone", String.valueOf(phone.getText()));
                    extra.putString("address", String.valueOf(address.getText()));

                    Intent intent = new Intent(v.getContext(), AdminMainAppUsersUserAddConnectInfoActivity.class);
                    intent.putExtra("userInfo",extra);
                    startActivity(intent);

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}