package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserMainAppTagsTagViewActivity extends AppCompatActivity {
    private TextView title;
    private RecyclerView booksListRecyclerView;
    private ArrayList<AdminBooksBookItem> booksList;
    private ArrayList<Integer> demandItList;
    private ArrayList<Integer> fullDemandsList;
    private Button backArrow;
    private SharedPreferences sp;
    private final String EMAIL = "email";
    private static final String ROOT_URL = "http://192.168.1.106/";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.user_main_app_tags_tag_view_activity);

        Intent intent = getIntent();
        Bundle tag = intent.getBundleExtra("tags");

        sp = PreferenceManager.getDefaultSharedPreferences(this);


        title = findViewById(R.id.tagTitle);

        backArrow = findViewById(R.id.admin_book_add_place_back_arrow);

        booksListRecyclerView = findViewById(R.id.booksRecyclerView);
        booksListRecyclerView.setHasFixedSize(true);
        booksListRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        title.setText(tag.getString("tag"));


        String URL = ROOT_URL + "Android/Unibib/v1/user/operations/tags/getTagBooks.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AdminBooksBookItem bookItem;
                            booksList = new ArrayList<>();
                            demandItList = new ArrayList<>();
                            fullDemandsList = new ArrayList<>();
                            Log.d("TAG", "onResponse: "+response);
                            JSONArray books = new JSONArray(response);

                            for(int i =0;i<books.length();i++){
                                JSONObject book = books.getJSONObject(i);

                                String ref = book.getString("ref");
                                String image = book.getString("image");
                                String title = book.getString("title");
                                String author = book.getString("author");
                                String place = book.getString("bookPlace");
                                int quantity  = book.getInt("quantity");
                                String edition = book.getString("edition");
                                String description = book.getString("description");
                                String tags = book.getString("tags");

                                String shelfName = book.getString("shelfName");
                                String shelfSide = book.getString("shelfSide");
                                String shelfRow = book.getString("shelfRow");
                                String shelfCol = book.getString("shelfCol");

                                bookItem = new AdminBooksBookItem(ref,image,title,author,place,quantity,edition,description,tags,shelfName,shelfCol,shelfRow,shelfSide);
                                booksList.add(bookItem);
                                demandItList.add(book.getInt("demandingIt"));
                                fullDemandsList.add(book.getInt("fullDemands"));
                            }

                            booksListRecyclerView.setAdapter( new UserTagsBooksRecyclerViewAdapter(booksList,demandItList,fullDemandsList,UserMainAppTagsTagViewActivity.this));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), "You should verify your connection", Toast.LENGTH_LONG).show();
                            VerifyInternetConnectionDialog dialog = new VerifyInternetConnectionDialog();
                            dialog.show(getSupportFragmentManager(), "no internet connection dialog");
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tag",tag.getString("tag"));
                params.put("email",sp.getString(EMAIL,null));

                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}