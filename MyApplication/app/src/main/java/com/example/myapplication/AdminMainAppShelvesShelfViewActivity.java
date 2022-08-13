package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminMainAppShelvesShelfViewActivity extends AppCompatActivity {
    private TextView name,ref,sides,cols,rows;
    private RecyclerView booksListRecyclerView;
    private ArrayList<AdminBooksBookItem> booksList;
    private Button backArrow;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.admin_main_app_shelves_shelf_view_activity);
        Intent intent = getIntent();
        Bundle shelf = intent.getBundleExtra("shelf");
        Bundle books = intent.getBundleExtra("books");

        name = findViewById(R.id.adminShelvesShelfCardViewName);
        ref = findViewById(R.id.adminShelvesShelfCardViewRef);
        sides = findViewById(R.id.adminShelvesShelfCardViewSide);
        rows = findViewById(R.id.adminShelvesShelfCardViewRow);
        cols = findViewById(R.id.adminShelvesShelfCardViewCol);
        backArrow = findViewById(R.id.admin_book_add_place_back_arrow);

        booksListRecyclerView = findViewById(R.id.booksRecyclerView);
        booksListRecyclerView.setHasFixedSize(true);
        booksListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        name.setText(shelf.getString("name"));
        ref.setText("Ref : "+shelf.getString("ref"));
        sides.setText("#Sides : "+String.valueOf(shelf.getInt("sides")));
        rows.setText("#Rows : "+String.valueOf(shelf.getInt("rows")));
        cols.setText("#Columns : "+String.valueOf(shelf.getInt("cols")));

        booksList = new ArrayList<>();
        AdminBooksBookItem bookItem;
        Bundle book = new Bundle();
        String ref,image,title,author,place,edition,description,tags;
        int quantity;
        for(int i =0;i<books.size();i++){
            book = books.getBundle(String.valueOf(i));
            ref = book.getString("ref");
            image = book.getString("image");
            title = book.getString("title");
            author = book.getString("author");
            place = book.getString("place");
            quantity  = book.getInt("quantity");
            edition = book.getString("edition");
            description = book.getString("description");
            tags = book.getString("tags");

            String shelfName = book.getString("shelfName");
            String shelfSide = book.getString("shelfSide");
            String shelfRow = book.getString("shelfRow");
            String shelfCol = book.getString("shelfCol");

            bookItem = new AdminBooksBookItem(ref,image,title,author,place,quantity,edition,description,tags,shelfName,shelfCol,shelfRow,shelfSide);
            booksList.add(bookItem);
        }

        booksListRecyclerView.setAdapter(new AdminBooksRecyclerViewAdapter(booksList,this));

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}