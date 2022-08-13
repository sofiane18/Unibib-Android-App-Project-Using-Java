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

public class AdminMainAppBorrowingsFragment extends Fragment {

    public AdminMainAppBorrowingsFragment() {
        // Required empty public constructor
    }
    private RecyclerView borrowingsRecyclerView;
    private FloatingActionButton addBookFloatingActionButton;
    private ArrayList<AdminBorrowingsBorrowingItem> borrowingsList;
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

        borrowingsList = new ArrayList<>();
        context = this.getContext();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.admin_main_app_borrowings_fragment, container, false);

        borrowingsRecyclerView = v.findViewById(R.id.borrowingsRecyclerView);
        borrowingsRecyclerView.setHasFixedSize(true);
        borrowingsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        searchButton = v.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AdminMainAppBorrowingsSearchActivity.class);
                startActivity(intent);
            }
        });

        loadBorrowings();

        addBookFloatingActionButton = v.findViewById(R.id.addBookFloatingActionButton);

        addBookFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AdminMainAppBorrowingsBorrowingAddActivity.class);
                context.startActivity(i);
            }

        });


        return v;

    }

    public void loadBorrowings(){

        String URL = ROOT_URL + "Android/Unibib/v1/admin/operations/borrowings/getBorrowingsListRecyclerView.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AdminBorrowingsBorrowingItem borrowingItem;
                            JSONArray borrowings = new JSONArray(response);

                            for(int i =0;i<borrowings.length();i++){
                                JSONObject borrowing = borrowings.getJSONObject(i);
                                AdminDemandsDemandItem demandItem;

                                String bookRef = borrowing.getString("bookRef");
                                String image = borrowing.getString("image");
                                String title = borrowing.getString("title");
                                String author = borrowing.getString("author");
                                String place = borrowing.getString("bookPlace");
                                int quantity  = borrowing.getInt("quantity");
                                String edition = borrowing.getString("edition");
                                String description = borrowing.getString("description");
                                String tags = borrowing.getString("tags");

                                String userRef,FName,LName, gender,phone,address,birthDate,email,property;
                                userRef = borrowing.getString("userRef");
                                FName = borrowing.getString("firstName");
                                LName = borrowing.getString("lastName");
                                gender = borrowing.getString("gender");
                                phone = borrowing.getString("phone");
                                address = borrowing.getString("address");
                                birthDate = borrowing.getString("birthDate");
                                email = borrowing.getString("email");
                                property = borrowing.getString("property");

                                String demandRef, demandDate,status;

                                demandRef = borrowing.getString("demandRef");
                                demandDate = borrowing.getString("demandDate");
                                status = borrowing.getString("status");

                                String ref,deliverDate;
                                int late,retrieved;

                                ref = borrowing.getString("borrowingRef");
                                deliverDate = borrowing.getString("deliverDate");
                                late = borrowing.getInt("late");
                                retrieved = borrowing.getInt("retrieved");

                                String shelfName = borrowing.getString("shelfName");
                                String shelfSide = borrowing.getString("shelfSide");
                                String shelfRow = borrowing.getString("shelfRow");
                                String shelfCol = borrowing.getString("shelfCol");

                                AdminBooksBookItem book = new AdminBooksBookItem(bookRef,image,title,author,place,quantity,edition,description,tags,shelfName,shelfCol,shelfRow,shelfSide);
                                AdminUsersUserItem user = new AdminUsersUserItem(userRef, FName, LName, gender, phone, address, birthDate, email, property);
                                demandItem = new AdminDemandsDemandItem(demandRef,book,user,demandDate,status);

                                borrowingItem = new AdminBorrowingsBorrowingItem(ref,deliverDate,late, retrieved, demandItem);
                                borrowingsList.add(borrowingItem);
                            }
                            borrowingsRecyclerView.setAdapter(new AdminBorrowingsRecyclerViewAdapter(borrowingsList,AdminMainAppBorrowingsFragment.this.context));
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
                        dialog.show(((FragmentActivity)AdminMainAppBorrowingsFragment.this.getContext()).getSupportFragmentManager(), "no internet connection dialog");


                    }
                });

        Volley.newRequestQueue(this.requireContext()).add(stringRequest);


    }

}