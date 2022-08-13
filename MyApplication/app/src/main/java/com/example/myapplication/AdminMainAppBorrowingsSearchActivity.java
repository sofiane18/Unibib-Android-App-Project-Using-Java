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

public class AdminMainAppBorrowingsSearchActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private RecyclerView borrowingsRecyclerView;
    private ArrayList<AdminBorrowingsBorrowingItem> borrowingsList;
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

        setContentView(R.layout.admin_main_app_borrowings_search_activity);
        borrowingsList = new ArrayList<>();

        borrowingsRecyclerView = findViewById(R.id.borrowingsRecyclerView);
        borrowingsRecyclerView.setHasFixedSize(true);
        borrowingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchByButton = findViewById(R.id.searchBy);
        searchBy="userRef";
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
                borrowingsList.clear();
                loadBorrowings(searchET.getText().toString(),searchBy);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrowingsList.clear();
                loadBorrowings(searchET.getText().toString(),searchBy);
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


    public void loadBorrowings(String q,String s){

        String URL = ROOT_URL + "Android/Unibib/v1/admin/operations/borrowings/getSearchBorrowings.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AdminBorrowingsBorrowingItem borrowingItem;
                            JSONArray borrowings = new JSONArray(response);

                            for (int i = 0; i < borrowings.length(); i++) {
                                JSONObject borrowing = borrowings.getJSONObject(i);
                                AdminDemandsDemandItem demandItem;

                                String bookRef = borrowing.getString("bookRef");
                                String image = borrowing.getString("image");
                                String title = borrowing.getString("title");
                                String author = borrowing.getString("author");
                                String place = borrowing.getString("bookPlace");
                                int quantity = borrowing.getInt("quantity");
                                String edition = borrowing.getString("edition");
                                String description = borrowing.getString("description");
                                String tags = borrowing.getString("tags");

                                String userRef, FName, LName, gender, phone, address, birthDate, email, property;
                                userRef = borrowing.getString("userRef");
                                FName = borrowing.getString("firstName");
                                LName = borrowing.getString("lastName");
                                gender = borrowing.getString("gender");
                                phone = borrowing.getString("phone");
                                address = borrowing.getString("address");
                                birthDate = borrowing.getString("birthDate");
                                email = borrowing.getString("email");
                                property = borrowing.getString("property");

                                String demandRef, demandDate, status;

                                demandRef = borrowing.getString("demandRef");
                                demandDate = borrowing.getString("demandDate");
                                status = borrowing.getString("status");

                                String ref, deliverDate;
                                int late, retrieved;

                                ref = borrowing.getString("borrowingRef");
                                deliverDate = borrowing.getString("deliverDate");
                                late = borrowing.getInt("late");
                                retrieved = borrowing.getInt("retrieved");

                                String shelfName = borrowing.getString("shelfName");
                                String shelfSide = borrowing.getString("shelfSide");
                                String shelfRow = borrowing.getString("shelfRow");
                                String shelfCol = borrowing.getString("shelfCol");

                                AdminBooksBookItem book = new AdminBooksBookItem(bookRef,image,title,author,place,quantity,edition,description,tags,shelfName,shelfCol,shelfRow,shelfSide);
                                AdminUsersUserItem user = new AdminUsersUserItem(userRef, FName, LName, gender, phone, address, birthDate, email, property);
                                demandItem = new AdminDemandsDemandItem(demandRef, book, user, demandDate, status);

                                borrowingItem = new AdminBorrowingsBorrowingItem(ref, deliverDate, late, retrieved, demandItem);
                                borrowingsList.add(borrowingItem);
                            }


                            borrowingsRecyclerView.setAdapter(new AdminBorrowingsRecyclerViewAdapter(borrowingsList,AdminMainAppBorrowingsSearchActivity.this));
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
        popupMenu.inflate(R.menu.admin_borrowings_search_by_options_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.userID: {searchBy = "userRef";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.userName: {searchBy = "userName";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.bookID:  { searchBy = "bookRef";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.bookTitle:  { searchBy = "bookTitle";
                searchByButton.setText(item.getTitle());
                return true;}

            default:
                return false;
        }

    }
}