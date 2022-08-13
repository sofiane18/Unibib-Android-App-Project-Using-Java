package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


public class AdminMainAppBooksFragment extends Fragment {

    private RecyclerView booksRecyclerView;
    private FloatingActionButton addBookFloatingActionButton;
    private ArrayList<AdminBooksBookItem> booksList;
    private Context context;
    private Button searchButton;
    public static AdminBooksRecyclerViewAdapter adminBooksRecyclerViewAdapter;
    private static final String ROOT_URL = "http://192.168.1.106/";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        booksList = new ArrayList<>();
        context = this.getContext();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.admin_main_app_books_fragment, container, false);

        booksRecyclerView = v.findViewById(R.id.booksRecyclerView);
        booksRecyclerView.setHasFixedSize(true);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        searchButton = v.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AdminMainAppBooksSearchActivity.class);
                startActivity(intent);
            }
        });
        loadBooks();
        addBookFloatingActionButton = v.findViewById(R.id.addBookFloatingActionButton);
        addBookFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AdminMainAppBooksBookAddActivityImp.class);
                context.startActivity(i);
            }

        });
        return v;
    }
    public void loadBooks(){
        String BOOKS_URL = ROOT_URL + "Android/Unibib/v1/admin/operations/books/getBooksListRecyclerView.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BOOKS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AdminBooksBookItem bookItem;
                            JSONArray books = new JSONArray(response);

                            for(int i =0;i<books.length();i++){
                                JSONObject book = books.getJSONObject(i);

                                String ref = book.getString("ref");
                                String image = book.getString("image");
                                String title = book.getString("title");
                                String author = book.getString("author");
                                String place = book.getString("bookPlace");
                                int quantity  = book.getInt("quantity");
                                String edition = book.getString("edition");
                                String description = book.getString("description");
                                String tags = book.getString("tags");

                                String shelfName = book.getString("shelfName");
                                String shelfSide = book.getString("shelfSide");
                                String shelfRow = book.getString("shelfRow");
                                String shelfCol = book.getString("shelfCol");


                                bookItem = new AdminBooksBookItem(ref,image,title,author,place,quantity,edition,description,tags,shelfName,shelfCol,shelfRow,shelfSide);
                                booksList.add(bookItem);
                            }

                            booksRecyclerView.setAdapter(adminBooksRecyclerViewAdapter = new AdminBooksRecyclerViewAdapter(booksList,AdminMainAppBooksFragment.this.context));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        //Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                        VerifyInternetConnectionDialog dialog = new VerifyInternetConnectionDialog();
                        dialog.show(((FragmentActivity)AdminMainAppBooksFragment.this.getContext()).getSupportFragmentManager(), "no internet connection dialog");

                    }
        });

        Volley.newRequestQueue(this.requireContext()).add(stringRequest);


    }
}