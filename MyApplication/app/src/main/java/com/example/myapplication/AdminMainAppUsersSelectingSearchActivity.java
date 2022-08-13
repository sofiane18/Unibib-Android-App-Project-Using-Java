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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminMainAppUsersSelectingSearchActivity extends AppCompatActivity  implements PopupMenu.OnMenuItemClickListener{

    private RecyclerView usersRecyclerView;
    private ArrayList<AdminUsersUserItem> usersList;
    private Button searchButton,searchByButton,back,selectButton,cancelButton;
    private EditText searchET;
    private FloatingActionButton addBookFloatingActionButton;
    private static final String ROOT_URL = "http://192.168.1.106/";
    private String searchBy;
    private Bundle selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.admin_main_app_users_selecting_search_activity);

        usersList = new ArrayList<>();

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setHasFixedSize(true);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchByButton = findViewById(R.id.searchBy);
        selectButton = findViewById(R.id.selectButton);
        cancelButton = findViewById(R.id.cancelButton);
        searchBy="name";
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
                usersList.clear();
                loadUsers(searchET.getText().toString(),searchBy);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersList.clear();
                loadUsers(searchET.getText().toString(),searchBy);
            }
        });

        addBookFloatingActionButton = findViewById(R.id.addBookFloatingActionButton);

        addBookFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AdminMainAppUsersUserAddActivity.class);
                startActivity(i);
            }

        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("selectedUser"));

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedUser != null) {
                    Intent intent = new Intent();
                    intent.putExtra("selectedUser", selectedUser);
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
            selectedUser = intent.getBundleExtra("selectedUser");
        }
    };

    public void loadUsers(String q,String s){

        String URL = ROOT_URL + "Android/Unibib/v1/admin/operations/users/getSearchUsers.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AdminUsersUserItem userItem;
                            JSONArray users = new JSONArray(response);

                            for(int i =0;i<users.length();i++){
                                JSONObject user = users.getJSONObject(i);

                                String userRef,FName,LName, gender,phone,address,birthDate,email,property;
                                userRef = user.getString("ref");
                                FName = user.getString("firstName");
                                LName = user.getString("lastName");
                                gender = user.getString("gender");
                                phone = user.getString("phone");
                                address = user.getString("address");
                                birthDate = user.getString("birthDate");
                                email = user.getString("email");
                                property = user.getString("property");


                                userItem = new AdminUsersUserItem(userRef,FName,LName,gender,phone,address,birthDate,email,property);
                                usersList.add(userItem);
                            }

                            usersRecyclerView.setAdapter(new AdminUsersRecyclerViewAdapter(usersList, AdminMainAppUsersSelectingSearchActivity.this));

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
        popupMenu.inflate(R.menu.admin_users_search_by_options_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.userSearchName: {searchBy = "name";
                searchByButton.setText(item.getTitle());
                return true;}
            case R.id.userSearchRef:  { searchBy = "ref";
                searchByButton.setText(item.getTitle());
                return true;}
            default:
                return false;
        }

    }

}
