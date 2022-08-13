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


public class AdminMainAppShelvesFragment extends Fragment {

        private RecyclerView shelvesRecyclerView;
        private FloatingActionButton addShelfFloatingActionButton;
        private ArrayList<AdminShelvesShelfItem> shelvesList;
        private Context context;
        private Button searchButton;
        private static final String ROOT_URL = "http://192.168.1.106/";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            shelvesList = new ArrayList<>();
            context = this.getContext();
            // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.admin_main_app_shelves_fragment, container, false);

            shelvesRecyclerView = v.findViewById(R.id.shelvesRecyclerView);
            shelvesRecyclerView.setHasFixedSize(true);
            shelvesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

            searchButton = v.findViewById(R.id.searchButton);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),AdminMainAppShelvesSearchActivity.class);
                    startActivity(intent);
                }
            });

            loadShelves();

            addShelfFloatingActionButton = v.findViewById(R.id.addBookFloatingActionButton);

            addShelfFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), AdminMainAppShelvesShelfAddActivity.class);
                    context.startActivity(i);
                }

            });


            return v;

        }

        public void loadShelves(){

            String SHELVES_URL = ROOT_URL + "Android/Unibib/v1/admin/operations/shelves/getShelvesListRecyclerView.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SHELVES_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                AdminShelvesShelfItem shelfItem;
                                AdminBooksBookItem bookItem;
                                ArrayList<AdminBooksBookItem> booksList;
                                JSONArray shelves = new JSONArray(response);

                                for(int i =0;i<shelves.length();i++){
                                    booksList = new ArrayList<>();
                                    JSONObject shelf = shelves.getJSONObject(i);
                                    JSONArray books = shelf.getJSONArray("books");
                                    for(int j =0;j<books.length();j++) {
                                        JSONArray book = books.getJSONArray(j);
                                        JSONObject bookObj = book.getJSONObject(0);
                                        String ref = bookObj.getString("ref");
                                        String image = bookObj.getString("image");
                                        String title = bookObj.getString("title");
                                        String author = bookObj.getString("author");
                                        String place = bookObj.getString("bookPlace");
                                        int quantity = bookObj.getInt("quantity");
                                        String edition = bookObj.getString("edition");
                                        String description = bookObj.getString("description");
                                        String tags = bookObj.getString("tags");

                                        String shelfName = bookObj.getString("shelfName");
                                        String shelfSide = bookObj.getString("shelfSide");
                                        String shelfRow = bookObj.getString("shelfRow");
                                        String shelfCol = bookObj.getString("shelfCol");

                                        bookItem = new AdminBooksBookItem(ref,image,title,author,place,quantity,edition,description,tags,shelfName,shelfCol,shelfRow,shelfSide);
                                        booksList.add(bookItem);
                                    }
                                    String shelfRef = shelf.getString("ref");
                                    String name = shelf.getString("name");
                                    int sides  = shelf.getInt("sidesNum");
                                    int rows  = shelf.getInt("rowNum");
                                    int cols  = shelf.getInt("colNum");

                                    shelfItem = new AdminShelvesShelfItem(shelfRef,name,sides,rows,cols,booksList);
                                    shelvesList.add(shelfItem);
                                }

                                shelvesRecyclerView.setAdapter(new AdminShelvesRecyclerViewAdapter(shelvesList,AdminMainAppShelvesFragment.this.context));

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
                        dialog.show(((FragmentActivity)AdminMainAppShelvesFragment.this.getContext()).getSupportFragmentManager(), "no internet connection dialog");


                        }
                    });

            Volley.newRequestQueue(this.requireContext()).add(stringRequest);


        }

    }
