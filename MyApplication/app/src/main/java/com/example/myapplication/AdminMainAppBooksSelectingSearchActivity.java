package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminMainAppBooksSelectingSearchActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, ConfirmationDialogDeleteBook.ConfirmationDialogDeleteBookListener{

    private RecyclerView booksRecyclerView;
    private ArrayList<AdminBooksBookItem> booksList;
    private Button searchButton,searchByButton,back,selectButton,cancelButton;
    private EditText searchET;
    private static final String ROOT_URL = "http://192.168.1.106/";
    private String searchBy;
    private Bundle selectedBook;
    public static AdminBooksSelectingRecyclerViewAdapter adminBooksSelectingRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.admin_main_app_books_selecting_search_activity);

        booksList = new ArrayList<>();

        booksRecyclerView = findViewById(R.id.booksRecyclerView);
        booksRecyclerView.setHasFixedSize(true);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchByButton = findViewById(R.id.searchBy);
        selectButton = findViewById(R.id.selectButton);
        cancelButton = findViewById(R.id.cancelButton);
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


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("selectedBook"));

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedBook != null) {
                    Intent intent = new Intent();
                    intent.putExtra("selectedBook", selectedBook);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
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

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            selectedBook = intent.getBundleExtra("selectedBook");
        }
    };
    public void loadBooks(String q,String s){

        String BOOKS_URL = ROOT_URL + "Android/Unibib/v1/admin/operations/books/getSearchBooks.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BOOKS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AdminBooksBookItem bookItem;
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
                            }

                            booksRecyclerView.setAdapter(adminBooksSelectingRecyclerViewAdapter = new AdminBooksSelectingRecyclerViewAdapter(booksList,AdminMainAppBooksSelectingSearchActivity.this));

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

    @Override
    public void onDeleteBookYesClicked() {
        adminBooksSelectingRecyclerViewAdapter.onDeleteBookYesClicked();
    }
}
