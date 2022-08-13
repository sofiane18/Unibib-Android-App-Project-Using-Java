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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class UserMainAppTagsFragment extends Fragment {

    public UserMainAppTagsFragment() {
        // Required empty public constructor
    }
    private RecyclerView tagsRecyclerView;
    private ArrayList<String> tagsList;
    private Context context;
    private Button searchButton;
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

        tagsList = new ArrayList<>();
        context = this.getContext();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.user_main_app_tags_fragment, container, false);

        tagsRecyclerView = v.findViewById(R.id.tagsRecyclerView);
        tagsRecyclerView.setHasFixedSize(true);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        sp = PreferenceManager.getDefaultSharedPreferences(context);

        searchButton = v.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),UserMainAppTagsSearchActivity.class);
                startActivity(intent);
            }
        });

        loadTags();


        return v;

    }

    public void loadTags(){

        String URL = ROOT_URL + "Android/Unibib/v1/user/operations/tags/getTagsListRecyclerView.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray tags = new JSONArray(response);
                            for(int i =0;i<tags.length();i++){
                                tagsList.add(tags.getString(i));
                            }
                            tagsRecyclerView.setAdapter(new UserTagsRecyclerViewAdapter(tagsList, context));
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
                        dialog.show(((FragmentActivity)UserMainAppTagsFragment.this.getContext()).getSupportFragmentManager(), "no internet connection dialog");


                    }
                });

        Volley.newRequestQueue(this.requireContext()).add(stringRequest);


    }


}