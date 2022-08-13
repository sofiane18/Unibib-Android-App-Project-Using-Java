package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class UserMainAppDemandsFragment extends Fragment {
    public UserMainAppDemandsFragment() {
        // Required empty public constructor
    }
    private RecyclerView demandsRecyclerView;
    private ArrayList<AdminDemandsDemandItem> demandsList;
    private Context context;
    private SharedPreferences sp;
    private final String EMAIL = "email";
    private static final String ROOT_URL = "http://192.168.1.106/";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        demandsList = new ArrayList<AdminDemandsDemandItem>();
        context = this.getContext();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.user_main_app_demands_fragment, container, false);
        demandsRecyclerView = v.findViewById(R.id.demandsRecyclerView);
        demandsRecyclerView.setHasFixedSize(true);
        demandsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        Button searchButton = v.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),UserMainAppDemandsSearchActivity.class);
                startActivity(intent);
            }
        });
        loadDemands();
        return v;
    }
    public void loadDemands(){

        String DEMANDS_URL = ROOT_URL + "Android/Unibib/v1/user/operations/demands/getDemandsListRecyclerView.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DEMANDS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AdminDemandsDemandItem demandItem;
                            JSONArray demands = new JSONArray(response);

                            for(int i =0;i<demands.length();i++){
                                JSONObject demand = demands.getJSONObject(i);

                                String bookRef = demand.getString("bookRef");
                                String image = demand.getString("image");
                                String title = demand.getString("title");
                                String author = demand.getString("author");
                                String place = demand.getString("bookPlace");
                                int quantity  = demand.getInt("quantity");
                                String edition = demand.getString("edition");
                                String description = demand.getString("description");
                                String tags = demand.getString("tags");

                                String userRef,FName,LName, gender,phone,address,birthDate,email,property;
                                userRef = demand.getString("userRef");
                                FName = demand.getString("firstName");
                                LName = demand.getString("lastName");
                                gender = demand.getString("gender");
                                phone = demand.getString("phone");
                                address = demand.getString("address");
                                birthDate = demand.getString("birthDate");
                                email = demand.getString("email");
                                property = demand.getString("property");

                                String demandRef, demandDate,status;

                                demandRef = demand.getString("demandRef");
                                demandDate = demand.getString("demandDate");
                                status = demand.getString("status");

                                String shelfName = demand.getString("shelfName");
                                String shelfSide = demand.getString("shelfSide");
                                String shelfRow = demand.getString("shelfRow");
                                String shelfCol = demand.getString("shelfCol");

                                AdminBooksBookItem book = new AdminBooksBookItem(bookRef,image,title,author,place,quantity,edition,description,tags,shelfName,shelfCol,shelfRow,shelfSide);
                                AdminUsersUserItem user = new AdminUsersUserItem(userRef, FName, LName, gender, phone, address, birthDate, email, property);
                                demandItem = new AdminDemandsDemandItem(demandRef,book,user,demandDate,status);
                                demandsList.add(demandItem);
                            }

                            demandsRecyclerView.setAdapter(new UserDemandsRecyclerViewAdapter(demandsList,UserMainAppDemandsFragment.this.context));

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
                        dialog.show(((FragmentActivity)UserMainAppDemandsFragment.this.getContext()).getSupportFragmentManager(), "no internet connection dialog");


                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("email", sp.getString(EMAIL,null));

                    return params;
                }
            };

        Volley.newRequestQueue(this.requireContext()).add(stringRequest);


    }
}