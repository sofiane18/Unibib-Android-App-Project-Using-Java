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

public class AdminMainAppDemandsSearchActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private RecyclerView demandsRecyclerView;
    private ArrayList<AdminDemandsDemandItem> demandsList;
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

        setContentView(R.layout.admin_main_app_demands_search_activity);

        demandsList = new ArrayList<>();

        demandsRecyclerView = findViewById(R.id.demandsRecyclerView);
        demandsRecyclerView.setHasFixedSize(true);
        demandsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                demandsList.clear();
                loadDemands(searchET.getText().toString(),searchBy);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demandsList.clear();
                loadDemands(searchET.getText().toString(),searchBy);
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


    public void loadDemands(String q,String s){

        String DEMANDS_URL = ROOT_URL + "Android/Unibib/v1/admin/operations/demands/getSearchDemands.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DEMANDS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AdminDemandsDemandItem demandItem;
                            JSONArray demands = new JSONArray(response);

                            for(int i =0;i<demands.length();i++){
                                JSONObject demand = demands.getJSONObject(i);

                                String bookRef = demand.getString("bookRef");
                                String image = demand.getString("image");
                                String title = demand.getString("title");
                                String author = demand.getString("author");
                                String place = demand.getString("bookPlace");
                                int quantity  = demand.getInt("quantity");
                                String edition = demand.getString("edition");
                                String description = demand.getString("description");
                                String tags = demand.getString("tags");

                                String userRef,FName,LName, gender,phone,address,birthDate,email,property;
                                userRef = demand.getString("userRef");
                                FName = demand.getString("firstName");
                                LName = demand.getString("lastName");
                                gender = demand.getString("gender");
                                phone = demand.getString("phone");
                                address = demand.getString("address");
                                birthDate = demand.getString("birthDate");
                                email = demand.getString("email");
                                property = demand.getString("property");

                                String demandRef, demandDate,status;

                                demandRef = demand.getString("demandRef");
                                demandDate = demand.getString("demandDate");
                                status = demand.getString("status");

                                String shelfName = demand.getString("shelfName");
                                String shelfSide = demand.getString("shelfSide");
                                String shelfRow = demand.getString("shelfRow");
                                String shelfCol = demand.getString("shelfCol");

                                AdminBooksBookItem book = new AdminBooksBookItem(bookRef,image,title,author,place,quantity,edition,description,tags,shelfName,shelfCol,shelfRow,shelfSide);
                                AdminUsersUserItem user = new AdminUsersUserItem(userRef, FName, LName, gender, phone, address, birthDate, email, property);
                                demandItem = new AdminDemandsDemandItem(demandRef,book,user,demandDate,status);
                                demandsList.add(demandItem);
                            }

                            demandsRecyclerView.setAdapter(new AdminDemandsRecyclerViewAdapter(demandsList,AdminMainAppDemandsSearchActivity.this));

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
        popupMenu.inflate(R.menu.admin_demands_search_by_options_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.user_id: {searchBy = "userRef";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.book_id:  { searchBy = "bookRef";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.status:      { searchBy = "status";
                searchByButton.setText(item.getTitle());
                return true;}
            default:
                return false;
        }

    }
}