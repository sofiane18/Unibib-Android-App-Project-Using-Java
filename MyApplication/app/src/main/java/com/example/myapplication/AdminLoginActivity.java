package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

public class AdminLoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button loginButton, back;
    private String ROOT_URL = "http://192.168.1.106/";
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
        setContentView(R.layout.admin_login_activity);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        back = findViewById(R.id.admin_book_add_place_back_arrow);
        loginButton = findViewById(R.id.loginButton);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        spEditor = sp.edit();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
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

                if (i==0) {
                    String url = ROOT_URL + "Android/Unibib/v1/admin/login/loginAdmin.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject responseObj = new JSONObject(response);
                                if (responseObj.getString("error").equals("false")) {
                                    Toast.makeText(getApplicationContext(), responseObj.getString("message"), Toast.LENGTH_LONG).show();
                                    sp = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                                    spEditor = sp.edit();
                                    spEditor.putString(ACCOUNT_MODE, "admin");
                                    spEditor.putString(EMAIL, String.valueOf(email.getText()));
                                    spEditor.apply();
                                    Intent intent = new Intent(getApplicationContext(), AdminMainAppActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), responseObj.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), "You should verify your connection", Toast.LENGTH_LONG).show();
                            VerifyInternetConnectionDialog dialog = new VerifyInternetConnectionDialog();
                            dialog.show(getSupportFragmentManager(), "no internet connection dialog");
                            
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("email", String.valueOf(email.getText()));
                            params.put("password", String.valueOf(password.getText()));

                            return params;
                        }
                    };
                    Volley.newRequestQueue(AdminLoginActivity.this).add(stringRequest);
                }
            }

        });
    }
}
