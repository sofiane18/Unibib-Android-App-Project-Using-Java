package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

public class UserMainAppBooksSearchActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    private ArrayList<AdminBooksBookItem> booksList;
    private Button searchButton,searchByButton,back;
    private EditText searchET;
    private static final String ROOT_URL = "http://192.168.1.106/";
    private String searchBy;
    public static AdminBooksRecyclerViewAdapter adminBooksRecyclerViewAdapter;
    private TextView title;
    private RecyclerView booksListRecyclerView;
    private ArrayList<Integer> demandItList;
    private ArrayList<Integer> fullDemandsList;
    private SharedPreferences sp;
    private final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.user_main_app_books_search_activity);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        booksList = new ArrayList<>();

        booksListRecyclerView = findViewById(R.id.booksRecyclerView);
        booksListRecyclerView.setHasFixedSize(true);
        booksListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchByButton = findViewById(R.id.searchBy);
        searchBy="title";
        searchByButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        searchET = findViewById(R.id.searchEditText);
        searchET.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                booksList.clear();
                loadBooks(searchET.getText().toString(),searchBy);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booksList.clear();
                loadBooks(searchET.getText().toString(),searchBy);
            }
        });

        back = findViewById(R.id.admin_book_add_place_back_arrow2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void loadBooks(String q,String s){

        String BOOKS_URL = ROOT_URL + "Android/Unibib/v1/user/operations/home/getSearchBooks.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BOOKS_URL,
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

                            booksListRecyclerView.setAdapter( new UserSearchBooksRecyclerViewAdapter(booksList,demandItList,fullDemandsList,UserMainAppBooksSearchActivity.this));

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
                params.put("searchKey",q);
                params.put("searchBy",s);
                params.put("email",sp.getString(EMAIL,null));

                return params;
            }

        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }

    public void showPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
        popupMenu.inflate(R.menu.admin_books_search_by_options_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.title: {searchBy = "title";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.author:  { searchBy = "author";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.id:      { searchBy = "ref";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.tags:    { searchBy = "tags";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.releaseY: {searchBy = "releaseY";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.description: {searchBy = "description";
                searchByButton.setText(item.getTitle());
                return true;}
            default:
                return false;
        }

    }
}