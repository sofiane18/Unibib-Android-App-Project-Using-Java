package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

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

public class AdminMainAppBorrowingsBorrowingViewActivity extends AppCompatActivity {

    private TextView ref;
    private TextView bookName;
    private TextView author;
    private TextView edition;
    private TextView place;
    private TextView quantity;
    private TextView userRef,userName,userProperty,userPhone,userAddress,userEmail;
    private TextView borrowingRef,date,status;
    private ImageView image;
    private Button backArrow;
    private Button retrieve;
    private String ROOT_URL = "http://192.168.1.106/";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.admin_main_app_borrowings_borrowing_view_activity);




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
        userAddress = findViewById(R.id.user_address);
        userEmail = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.user_phone);

        borrowingRef = findViewById(R.id.borrowing_ref);
        date = findViewById(R.id.date);
        status = findViewById(R.id.status);


        retrieve = findViewById(R.id.retrieveBook);
        backArrow = findViewById(R.id.admin_book_add_place_back_arrow);



        Intent intent = getIntent();
        Bundle borrowingItem = (Bundle) intent.getBundleExtra("borrowing");

        ref.setText("Ref: " + borrowingItem.getString("bookRef"));
        bookName.setText(borrowingItem.getString("title"));
        author.setText("By : " + borrowingItem.getString("author"));
        edition.setText("Edition: "+borrowingItem.getString("edition"));
        place.setText("Place: "+borrowingItem.getString("place"));
        quantity.setText("Quantity: "+borrowingItem.getInt("quantity"));

        userRef.setText(borrowingItem.getString("userRef"));
        userName.setText(borrowingItem.getString("lName")+" "+borrowingItem.getString("fName"));
        userProperty.setText(borrowingItem.getString("property"));
        userAddress.setText(borrowingItem.getString("address"));
        userEmail.setText(borrowingItem.getString("email"));
        userPhone.setText(borrowingItem.getString("phone"));

        borrowingRef.setText(borrowingItem.getString("ref"));
        date.setText(borrowingItem.getString("deliverDate"));
        status.setText(borrowingItem.getString("status"));
        Glide.with(this).load(borrowingItem.getString("image")).into(image);
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = ROOT_URL + "Android/Unibib/v1/admin/operations/borrowings/retrieveBorrowing.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            if(responseObj.getString("error").equals("false")){
                                Toast.makeText(getApplicationContext(), responseObj.getString("message"),Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),AdminMainAppActivity.class);
                                getApplicationContext().startActivity(intent);
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
                        //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        VerifyInternetConnectionDialog dialog = new VerifyInternetConnectionDialog();
                        dialog.show(((FragmentActivity)v.getContext()).getSupportFragmentManager(), "no internet connection dialog");
                    }
                }){
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("ref",borrowingItem.getString("ref"));
                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
            }

        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}