package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminMainAppDemandsDemandViewActivity extends AppCompatActivity {

    private TextView ref;
    private TextView bookName;
    private TextView author;
    private TextView edition;
    private TextView place;
    private TextView quantity;
    private TextView userRef,userName,userProperty;
    private TextView demandRef,date,status;
    private ImageView image;
    private Button backArrow;
    private Button leftButton;
    private Button rightButton;

    private static final String ROOT_URL = "http://192.168.1.106/";



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.admin_main_app_demands_demand_view_activity);



        ref = findViewById(R.id.admin_bookView_bookRef);
        bookName = findViewById(R.id.admin_book_add_info);
        author = findViewById(R.id.admin_bookView_bookAuthor);
        edition = findViewById(R.id.admin_bookView_bookEdition);
        place = findViewById(R.id.admin_bookView_bookPlace);
        quantity = findViewById(R.id.admin_bookView_bookQuantity);
        image = findViewById(R.id.admin_bookView_bookImage);


        userRef = findViewById(R.id.userRef);
        userName = findViewById(R.id.user_name);
        userProperty = findViewById(R.id.user_property);

        demandRef = findViewById(R.id.demand_ref);
        date = findViewById(R.id.date);
        status = findViewById(R.id.status);


        leftButton = findViewById(R.id.admin_bookView_bookModify_button);
        rightButton = findViewById(R.id.admin_bookView_bookDelete_button);
        backArrow = findViewById(R.id.admin_book_add_place_back_arrow);



        Intent intent = getIntent();
        Bundle demandItem = (Bundle) intent.getBundleExtra("demand");

        ref.setText("Ref: " + demandItem.getString("bookRef"));
        bookName.setText(demandItem.getString("title"));
        author.setText("By : " + demandItem.getString("author"));
        edition.setText("Edition: "+demandItem.getString("edition"));
        place.setText("Place: "+demandItem.getString("place"));
        quantity.setText("Quantity: "+demandItem.getInt("quantity"));

        userRef.setText(demandItem.getString("userRef"));
        userName.setText(demandItem.getString("lName")+" "+demandItem.getString("fName"));
        userProperty.setText(demandItem.getString("property"));

        demandRef.setText(demandItem.getString("ref"));
        date.setText(demandItem.getString("demandDate"));
        status.setText(demandItem.getString("status"));

        Glide.with(this).load(demandItem.getString("image")).into(image);

        switch (demandItem.getString("status")){
            case "ready":{
                leftButton.setText("EXCUSE");
                rightButton.setText("APPROVE");

                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        operationButton(demandItem.getString("ref"),"excuseDemand");
                    }
                });

                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        operationButton(demandItem.getString("ref"),"approveDemand");
                    }
                });

            }
            break;
            case "approved":{
                leftButton.setText("CANCEL");
                rightButton.setText("DELIVERED");

                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        operationButton(demandItem.getString("ref"),"cancelDemand");
                    }
                });

                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        operationButton(demandItem.getString("ref"),"deliveredDemand");
                    }
                });
            }
            break;
            case "excused":
            case "canceled":
            case "delivered": {
               leftButton.setVisibility(View.GONE);
               rightButton.setVisibility(View.GONE);
            }
            break;
            default:break;
        }

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    public void operationButton(String demandRef,String op){
        String URL = ROOT_URL + "Android/Unibib/v1/admin/operations/demands/"+ op +".php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response);
                    if(responseObj.getString("error").equals("false")){
                        Toast.makeText(getApplicationContext(),responseObj.getString("message"),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),AdminMainAppDemandsFragment.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),responseObj.getString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ref",demandRef);
                return params;
            }
        };
        Volley.newRequestQueue(this.getApplicationContext()).add(stringRequest);
    }


}
