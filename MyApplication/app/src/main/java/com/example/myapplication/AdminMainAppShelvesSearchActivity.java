package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

public class AdminMainAppShelvesSearchActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{



    private RecyclerView shelvesRecyclerView;
    private ArrayList<AdminShelvesShelfItem> shelvesList;
    private Button searchButton,searchByButton,back;
    private EditText searchET;
    private static final String ROOT_URL = "http://192.168.1.106/";
    private String searchBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.admin_main_app_shelves_search_activity);

        shelvesList = new ArrayList<AdminShelvesShelfItem>();

        shelvesRecyclerView = findViewById(R.id.shelvesRecyclerView);
        shelvesRecyclerView.setHasFixedSize(true);
        shelvesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchByButton = findViewById(R.id.searchBy);
        searchBy="Name";
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
                shelvesList.clear();
                loadShelves(searchET.getText().toString(),searchBy);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shelvesList.clear();
                loadShelves(searchET.getText().toString(),searchBy);
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


    public void loadShelves(String q,String s){

        String SHELVES_URL = ROOT_URL + "Android/Unibib/v1/admin/operations/shelves/getSearchShelves.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHELVES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AdminShelvesShelfItem shelfItem;
                            AdminBooksBookItem bookItem;
                            ArrayList<AdminBooksBookItem> booksList;
                            JSONArray shelves = new JSONArray(response);

                            for(int i =0;i<shelves.length();i++){
                                booksList = new ArrayList<>();
                                JSONObject shelf = shelves.getJSONObject(i);
                                JSONArray books = shelf.getJSONArray("books");
                                for(int j =0;j<books.length();j++) {
                                    JSONArray book = books.getJSONArray(j);
                                    JSONObject bookObj = book.getJSONObject(0);
                                    String ref = bookObj.getString("ref");
                                    String image = bookObj.getString("image");
                                    String title = bookObj.getString("title");
                                    String author = bookObj.getString("author");
                                    String place = bookObj.getString("bookPlace");
                                    int quantity = bookObj.getInt("quantity");
                                    String edition = bookObj.getString("edition");
                                    String description = bookObj.getString("description");
                                    String tags = bookObj.getString("tags");

                                    String shelfName = bookObj.getString("shelfName");
                                    String shelfSide = bookObj.getString("shelfSide");
                                    String shelfRow = bookObj.getString("shelfRow");
                                    String shelfCol = bookObj.getString("shelfCol");

                                    bookItem = new AdminBooksBookItem(ref,image,title,author,place,quantity,edition,description,tags,shelfName,shelfCol,shelfRow,shelfSide);
                                    booksList.add(bookItem);
                                }
                                String shelfRef = shelf.getString("ref");
                                String name = shelf.getString("name");
                                int sides  = shelf.getInt("sidesNum");
                                int rows  = shelf.getInt("rowNum");
                                int cols  = shelf.getInt("colNum");

                                shelfItem = new AdminShelvesShelfItem(shelfRef,name,sides,rows,cols,booksList);
                                shelvesList.add(shelfItem);
                            }

                            shelvesRecyclerView.setAdapter(new AdminShelvesRecyclerViewAdapter(shelvesList,AdminMainAppShelvesSearchActivity.this));

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
        popupMenu.inflate(R.menu.admin_shelves_search_by_options_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.name: {searchBy = "name";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.sides:  { searchBy = "sidesNum";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.rows:      { searchBy = "rowNum";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.cols:    { searchBy = "colNum";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.shelfRef: {searchBy = "ref";
                searchByButton.setText(item.getTitle());
                return true;}

            default:
                return false;
        }

    }

}
