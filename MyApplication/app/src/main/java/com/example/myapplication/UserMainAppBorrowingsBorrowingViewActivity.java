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

import com.bumptech.glide.Glide;

public class UserMainAppBorrowingsBorrowingViewActivity extends AppCompatActivity {

    private TextView ref;
    private TextView bookName;
    private TextView author;
    private TextView edition;
    private TextView userRef,userName,userProperty,userPhone,userAddress,userEmail;
    private TextView borrowingRef,date,status;
    private ImageView image;
    private Button backArrow;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.user_main_app_borrowings_borrowing_view_activity);

        ref = findViewById(R.id.admin_bookView_bookRef);
        bookName = findViewById(R.id.admin_book_add_info);
        author = findViewById(R.id.admin_bookView_bookAuthor);
        edition = findViewById(R.id.admin_bookView_bookEdition);
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

        backArrow = findViewById(R.id.admin_book_add_place_back_arrow);

        Intent intent = getIntent();
        Bundle borrowingItem = (Bundle) intent.getBundleExtra("borrowing");

        ref.setText("Ref: " + borrowingItem.getString("bookRef"));
        bookName.setText(borrowingItem.getString("title"));
        author.setText("By : " + borrowingItem.getString("author"));
        edition.setText("Edition: "+borrowingItem.getString("edition"));

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

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}