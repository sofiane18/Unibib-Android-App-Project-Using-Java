package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class AdminSignUpPersonalInfoActivity extends AppCompatActivity {
    private EditText fname,lname;
    private Button male,female,next,back;
    private String gender;
    private String ROOT_URL = "http://192.168.1.106/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.admin_sign_up_activity);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        next = findViewById(R.id.nextButton);
        back = findViewById(R.id.admin_book_add_place_back_arrow);
        gender = "male";
        male.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForColorStateLists")
            @Override
            public void onClick(View v) {
                gender = "male";
                //male.getBackground().setColorFilter(ContextCompat.getColor(v.getContext(),R.color.selected), PorterDuff.Mode.SRC_ATOP);
                male.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(v.getContext(),R.color.selected)));
                male.setTextColor(Color.parseColor("#FFFFFF"));
                //female.getBackground().setColorFilter(ContextCompat.getColor(v.getContext(),R.color.darker_red), PorterDuff.Mode.SRC_ATOP);
                female.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#450921")));
                female.setTextColor(Color.parseColor("#CFCECE"));
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "female";
                //female.getBackground().setColorFilter(ContextCompat.getColor(v.getContext(),R.color.selected), PorterDuff.Mode.SRC_ATOP);
                female.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(v.getContext(),R.color.selected)));
                female.setTextColor(Color.parseColor("#FFFFFF"));
                //male.getBackground().setColorFilter(ContextCompat.getColor(v.getContext(),R.color.darker_red), PorterDuff.Mode.SRC_ATOP);
                male.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#450921")));
                male.setTextColor(Color.parseColor("#CFCECE"));            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int i=0;
                if(String.valueOf(fname.getText()).equals("")){
                    fname.setError("It seems you missed to enter your first name");
                    fname.requestFocus();
                    i++;
                }
                if(String.valueOf(lname.getText()).equals("")){
                    lname.setError("It seems you missed to enter your last name");
                    lname.requestFocus();
                    i++;
                }


                if (i==0){
                    Bundle extra = new Bundle();
                    extra.putString("lName", String.valueOf(lname.getText()));
                    extra.putString("fName", String.valueOf(fname.getText()));
                    extra.putString("gender", gender);

                    Intent intent = new Intent(v.getContext(), AdminSignUpAdditionalPersonalInfoActivity.class);
                    intent.putExtra("adminInfo",extra);
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