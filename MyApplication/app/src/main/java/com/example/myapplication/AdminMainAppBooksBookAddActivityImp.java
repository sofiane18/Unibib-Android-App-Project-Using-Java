package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class AdminMainAppBooksBookAddActivityImp extends AppCompatActivity {
    private Button backArrow;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText editionEditText;
    private EditText releaseYearEditText;
    private EditText tagsEditText;
    private EditText descriptionEditText;
    private Button nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.admin_main_app_books_book_add_activity_imp);
        backArrow = findViewById(R.id.admin_book_add_place_back_arrow);
        nextButton = findViewById(R.id.adminBookAddAddButton);
        titleEditText = findViewById(R.id.adminBookAddQuantity);
        authorEditText = findViewById(R.id.adminBookAddCol);
        editionEditText = findViewById(R.id.adminBookAddEdition);
        releaseYearEditText = findViewById(R.id.adminShelfAddSides);
        tagsEditText = findViewById(R.id.adminBookAddTags);
        descriptionEditText = findViewById(R.id.adminBookAddDesc);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int i=0;
                if (String.valueOf(titleEditText.getText()).equals("")) {
                    titleEditText.setError("It seems you missed to enter the title");
                    titleEditText.requestFocus();
                    i++;
                }
                if (String.valueOf(authorEditText.getText()).equals("")) {
                    authorEditText.setError("It seems you missed to enter the author name");
                    authorEditText.requestFocus();
                    i++;
                }
                if (String.valueOf(editionEditText.getText()).equals("")) {
                    editionEditText.setError("It seems you missed to enter the edition");
                    editionEditText.requestFocus();

                    i++;
                }
                if(i==0){
                    Bundle extra = new Bundle();
                    extra.putString("title", String.valueOf(titleEditText.getText()));
                    extra.putString("author", String.valueOf(authorEditText.getText()));
                    extra.putString("edition", String.valueOf(editionEditText.getText()));
                    extra.putString("releaseYear", String.valueOf(releaseYearEditText.getText()));
                    extra.putString("tags", String.valueOf(tagsEditText.getText()));
                    extra.putString("description", String.valueOf(descriptionEditText.getText()));

                    Intent intent = new Intent(v.getContext(), AdminMainAppBooksBookAddActivityPlace.class);
                    intent.putExtra("bookInfo",extra);
                    startActivity(intent);
                }
            }
        });
    }
}