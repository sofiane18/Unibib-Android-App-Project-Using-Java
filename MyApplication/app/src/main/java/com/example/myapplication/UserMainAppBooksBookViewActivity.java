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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserMainAppBooksBookViewActivity extends AppCompatActivity{
    private TextView ref;
    private TextView bookName;
    private TextView author;
    private TextView edition;
    private TextView description;
    private TextView tags;
    private ImageView image;
    private Button backArrow;
    private Button demandButton;
    private SharedPreferences sp;
    private final String EMAIL = "email";
    private Bundle bookItem;
    private static final String ROOT_URL = "http://192.168.1.106/";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.user_main_app_books_book_view_activity);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        ref = findViewById(R.id.admin_bookView_bookRef);
        bookName = findViewById(R.id.admin_book_add_info);
        author = findViewById(R.id.admin_bookView_bookAuthor);
        edition = findViewById(R.id.admin_bookView_bookEdition);
        description = findViewById(R.id.admin_bookView_bookDescription);
        tags = findViewById(R.id.admin_bookView_bookTags);
        image = findViewById(R.id.admin_bookView_bookImage);
        demandButton = findViewById(R.id.demandButton);
        backArrow = findViewById(R.id.admin_book_add_place_back_arrow);
        Intent intent = getIntent();
        bookItem = (Bundle) intent.getBundleExtra("book");
        ref.setText("Ref: " + bookItem.getString("ref"));
        bookName.setText(bookItem.getString("title"));
        author.setText("By: "+bookItem.getString("author"));
        edition.setText("Edition: "+bookItem.getString("edition"));
        description.setText(bookItem.getString("description"));
        tags.setText(bookItem.getString("tags"));
        Glide.with(this).load(bookItem.getString("image")).into(image);
        if(bookItem.getInt("demandIt")==0 && bookItem.getInt("fullDemands")==0) {
            demandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Demanding the book in progress...", Toast.LENGTH_SHORT).show();
                    String urlAddBook = ROOT_URL + "Android/Unibib/v1/user/operations/demands/demandBook.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAddBook, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject responseObj = new JSONObject(response);
                                if (responseObj.getString("error").equals("false")) {
                                    Toast.makeText(getApplicationContext(), responseObj.getString("message"), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), UserMainAppActivity.class);
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

                            params.put("bookRef", bookItem.getString("ref"));
                            params.put("userEmail", sp.getString(EMAIL, null));
                            return params;
                        }
                    };
                    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
                }
            });
        }else{
            demandButton.setVisibility(View.GONE);
        }
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}