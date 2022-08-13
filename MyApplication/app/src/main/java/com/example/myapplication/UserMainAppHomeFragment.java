package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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


public class UserMainAppHomeFragment extends Fragment {

    private TextView title;
    private RecyclerView booksListRecyclerView;
    private ArrayList<AdminBooksBookItem> booksList;
    private ArrayList<Integer> demandItList;
    private ArrayList<Integer> fullDemandsList;
    private SharedPreferences sp;
    private final String EMAIL = "email";
    private Button searchButton;
    private static final String ROOT_URL = "http://192.168.1.106/";
    private Context context;

    public UserMainAppHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = this.getContext();


        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.user_main_app_home_fragment, container, false);

        sp = PreferenceManager.getDefaultSharedPreferences(context);

        booksListRecyclerView = v.findViewById(R.id.booksRecyclerView);
        booksListRecyclerView.setHasFixedSize(true);
        booksListRecyclerView.setLayoutManager(new GridLayoutManager(context,3));

        searchButton = v.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),UserMainAppBooksSearchActivity.class);
                startActivity(intent);
            }
        });

        String URL = ROOT_URL + "Android/Unibib/v1/user/operations/home/getBooks.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AdminBooksBookItem bookItem;
                            booksList = new ArrayList<>();
                            demandItList = new ArrayList<>();
                            fullDemandsList = new ArrayList<>();
                            Log.d("TAG", "onResponse: "+response);
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
                                demandItList.add(book.getInt("demandingIt"));
                                fullDemandsList.add(book.getInt("fullDemands"));
                            }

                            booksListRecyclerView.setAdapter( new UserHomeBooksRecyclerViewAdapter(booksList,demandItList,fullDemandsList,context));

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
                        dialog.show(((FragmentActivity)UserMainAppHomeFragment.this.getContext()).getSupportFragmentManager(), "no internet connection dialog");
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",sp.getString(EMAIL,null));

                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);


        return v;
    }

}