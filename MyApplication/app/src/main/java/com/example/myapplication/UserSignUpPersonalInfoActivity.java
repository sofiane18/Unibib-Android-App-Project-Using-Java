package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class UserSignUpPersonalInfoActivity extends AppCompatActivity {
    private EditText fname,lname;
    private Button male,female,next,back;
    private String gender;
    private Spinner property;
    private String ROOT_URL = "http://192.168.1.106/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.user_sign_up_personal_info_activity);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        next = findViewById(R.id.nextButton);
        property = findViewById(R.id.property);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ArrayList<String> propertyList = new ArrayList<>();
        propertyList.add("-- Select A Property --");
        propertyList.add("Professor");
        propertyList.add("Student");
        propertyList.add("Other");
        String[] propertyArray = new String[propertyList.size()];
        propertyArray = propertyList.toArray(propertyArray);
        ArrayAdapter<String> propertyAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, propertyArray){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.GRAY);
                } else {
                    textview.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        property.setAdapter(propertyAdapter);
        property.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
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

                if (property.getSelectedItemId()==0) {
                    TextView errorText= (TextView) property.getSelectedView();
                    errorText.setError("You missed to enter your property");
                    errorText.requestFocus();
                    i++;
                }


                if (i==0){
                    Bundle extra = new Bundle();
                    extra.putString("lName", String.valueOf(lname.getText()));
                    extra.putString("fName", String.valueOf(fname.getText()));
                    extra.putString("gender", gender);
                    extra.putString("property",property.getSelectedItem().toString());


                    Intent intent = new Intent(v.getContext(), UserSignUpAdditionalPersonalInfoActivity.class);
                    intent.putExtra("userInfo",extra);
                    startActivity(intent);

                }
            }
        });


    }
}