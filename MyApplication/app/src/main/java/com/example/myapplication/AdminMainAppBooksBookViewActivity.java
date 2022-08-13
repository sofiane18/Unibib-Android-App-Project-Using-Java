package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class AdminMainAppBooksBookViewActivity extends AppCompatActivity  implements ConfirmationDialogDeleteBook.ConfirmationDialogDeleteBookListener{
    private TextView ref;
    private TextView bookName;
    private TextView author;
    private TextView edition;
    private TextView place;
    private TextView quantity;
    private TextView description;
    private TextView tags;
    private ImageView image;
    private Button backArrow;
    private Button modify;
    private Button delete;

    private Bundle bookItem;
    private static final String ROOT_URL = "http://192.168.1.106/";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.admin_main_app_books_book_view_activity);

        ref = findViewById(R.id.admin_bookView_bookRef);
        bookName = findViewById(R.id.admin_book_add_info);
        author = findViewById(R.id.admin_bookView_bookAuthor);
        edition = findViewById(R.id.admin_bookView_bookEdition);
        place = findViewById(R.id.admin_bookView_bookPlace);
        quantity = findViewById(R.id.admin_bookView_bookQuantity);
        description = findViewById(R.id.admin_bookView_bookDescription);
        tags = findViewById(R.id.admin_bookView_bookTags);
        image = findViewById(R.id.admin_bookView_bookImage);
        modify = findViewById(R.id.admin_bookView_bookModify_button);
        delete = findViewById(R.id.admin_bookView_bookDelete_button);
        backArrow = findViewById(R.id.admin_book_add_place_back_arrow);

        Intent intent = getIntent();
        bookItem = (Bundle) intent.getBundleExtra("book");
        ref.setText("Ref: " + bookItem.getString("ref"));
        bookName.setText(bookItem.getString("title"));
        author.setText("By: "+bookItem.getString("author"));
        edition.setText("Edition: "+bookItem.getString("edition"));
        place.setText("Place: "+bookItem.getString("place"));
        quantity.setText("Quantity: "+bookItem.getInt("quantity"));
        description.setText(bookItem.getString("description"));
        tags.setText(bookItem.getString("tags"));
        Glide.with(this).load(bookItem.getString("image")).into(image);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AdminMainAppBooksBookModifyActivity.class);
                intent.putExtra("book", bookItem);
                startActivity(intent);
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationDialogDeleteBook dialog = new ConfirmationDialogDeleteBook();
                dialog.show(((FragmentActivity)v.getContext()).getSupportFragmentManager(), "delete dialog");
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onDeleteBookYesClicked(){
        String url =  ROOT_URL + "Android/Unibib/v1/admin/operations/books/deleteBook.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response", "onResponse: "+ response);
                    JSONObject responseObj = new JSONObject(response);
                    if(responseObj.getString("error").equals("false")){
                        Toast.makeText(getApplicationContext(),responseObj.getString("message"),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),AdminMainAppActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),responseObj.getString("message"),Toast.LENGTH_LONG).show();
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
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("ref",bookItem.getString("ref"));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}