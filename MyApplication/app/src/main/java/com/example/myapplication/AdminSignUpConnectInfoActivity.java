package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminSignUpConnectInfoActivity extends AppCompatActivity {
    private EditText email,password,invitationCode;
    private Button next,back;
    private String ROOT_URL = "http://192.168.1.106/";
    private Bundle extra;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private final String FNAME = "fName";
    private final String LNAME = "lName";
    private final String EMAIL = "email";
    private final String ACCOUNT_MODE = "mode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        extra = (Bundle) intent.getBundleExtra("adminInfo");
        setContentView(R.layout.admin_sign_up_connect_info_activity);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        invitationCode = findViewById(R.id.invitationCode);
        next = findViewById(R.id.nextButton);
        back = findViewById(R.id.admin_book_add_place_back_arrow);
        next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int i=0;
                if(String.valueOf(email.getText()).equals("")){
                    email.setError("It seems you missed to enter your email");
                    email.requestFocus();
                    i++;
                }
                if(String.valueOf(password.getText()).equals("")){
                    password.setError("It seems you missed to enter your password");
                    password.requestFocus();
                    i++;
                }

                if(String.valueOf(invitationCode.getText()).equals("")){
                    invitationCode.setError("It seems you missed to enter your invitation code");
                    invitationCode.requestFocus();
                    i++;
                }


                if (i==0){
                    extra.putString("email", String.valueOf(email.getText()));
                    extra.putString("password", String.valueOf(password.getText()));
                    extra.putString("invitationCode", String.valueOf(invitationCode.getText()));

                    Toast.makeText(getApplicationContext(),"Registration in progress...",Toast.LENGTH_SHORT).show();
                    String urlAddBook =  ROOT_URL + "Android/Unibib/v1/admin/registration/registerAdminConnectInfo.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAddBook, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject responseObj = new JSONObject(response);
                                if(responseObj.getString("error").equals("false")){
                                    Toast.makeText(getApplicationContext(),responseObj.getString("message"),Toast.LENGTH_LONG).show();
                                    sp = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                                    spEditor = sp.edit();
                                    spEditor.putString(ACCOUNT_MODE,"admin");
                                    spEditor.putString(EMAIL,String.valueOf(email.getText()));
                                    spEditor.putString(FNAME,extra.getString("fName"));
                                    spEditor.putString(LNAME,extra.getString("lName"));
                                    spEditor.apply();
                                    Intent intent = new Intent(getApplicationContext(),AdminMainAppActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(),responseObj.getString("message"),Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(),"You should verify your connection",Toast.LENGTH_LONG).show();
                            VerifyInternetConnectionDialog dialog = new VerifyInternetConnectionDialog();
                            dialog.show(getSupportFragmentManager(), "no internet connection dialog");
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();

                            params.put("fName",extra.getString("fName"));
                            params.put("lName",extra.getString("lName"));
                            params.put("gender",extra.getString("gender"));
                            params.put("birthDate",extra.getString("birthDate"));
                            params.put("phone",extra.getString("phone"));
                            params.put("address",extra.getString("address"));
                            params.put("email",String.valueOf(email.getText()));
                            params.put("password",String.valueOf(password.getText()));
                            params.put("invitationCode",String.valueOf(invitationCode.getText()));

                            return params;
                        }
                    };

                    Volley.newRequestQueue(AdminSignUpConnectInfoActivity.this).add(stringRequest);

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