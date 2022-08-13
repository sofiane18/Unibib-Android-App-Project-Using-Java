package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDemandsRecyclerViewAdapter  extends RecyclerView.Adapter<UserDemandsRecyclerViewAdapter.DemandViewHolder>  {

    public ArrayList<AdminDemandsDemandItem> demandsList;
    private final Context context;
    private static final String ROOT_URL = "http://192.168.1.106/";

    private void operationButton(String demandRef,String op){
        String URL = ROOT_URL + "Android/Unibib/v1/admin/operations/demands/"+ op +".php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response);
                    if(responseObj.getString("error").equals("false")){
                        Toast.makeText(context, responseObj.getString("message"),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context,UserMainAppActivity.class);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,responseObj.getString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                        VerifyInternetConnectionDialog dialog = new VerifyInternetConnectionDialog();
                        dialog.show(((FragmentActivity)context).getSupportFragmentManager(), "no internet connection dialog");
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ref",demandRef);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public UserDemandsRecyclerViewAdapter(ArrayList<AdminDemandsDemandItem> demandsList, Context context) {
        this.demandsList = demandsList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserDemandsRecyclerViewAdapter.DemandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_main_app_demands_demand_cardview,parent,false);



        return new UserDemandsRecyclerViewAdapter.DemandViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserDemandsRecyclerViewAdapter.DemandViewHolder holder, int position) {

        AdminDemandsDemandItem demandItem = demandsList.get(position);

        holder.title.setText(demandItem.bookItem.getTitle());
        holder.place.setText("By: " + demandItem.bookItem.getAuthor());
        holder.quantity.setText("Editon: "+ demandItem.bookItem.getEdition());
        holder.date.setText("Date : "+ demandItem.getDemandDate());
        Glide.with(context).load(demandItem.bookItem.getImage()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle demandBundle = new Bundle();
                demandBundle.putString("bookRef",demandItem.bookItem.getRef());
                demandBundle.putString("title",demandItem.bookItem.getTitle());
                demandBundle.putString("image",demandItem.bookItem.getImage());
                demandBundle.putString("author",demandItem.bookItem.getAuthor());
                demandBundle.putString("edition",demandItem.bookItem.getEdition());
                demandBundle.putInt("quantity",demandItem.bookItem.getQuantity());
                demandBundle.putString("place",demandItem.bookItem.getPlace());

                demandBundle.putString("userRef",demandItem.userItem.getRef());
                demandBundle.putString("fName",demandItem.userItem.getFName());
                demandBundle.putString("lName",demandItem.userItem.getLName());
                demandBundle.putString("property",demandItem.userItem.getProperty());

                demandBundle.putString("demandDate",demandItem.getDemandDate());
                demandBundle.putString("status",demandItem.getStatus());
                demandBundle.putString("ref",demandItem.getRef());


                Intent intent = new Intent(v.getContext(),UserMainAppDemandsDemandViewActivity.class);
                intent.putExtra("demand",  demandBundle);
                context.startActivity(intent);

            }
        });

        switch (demandItem.getStatus()){
            case "ready":{
                holder.status.setText("READY");
                holder.status.setTextColor(Color.parseColor("#1A5B97"));
                holder.leftButton.setText("CANCEL");
                holder.leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        operationButton(demandItem.getRef(),"cancelDemand");
                    }
                });
            }
            break;
            case "approved":{
                holder.leftButton.setText("CANCEL");
                holder.status.setText("APPROVED");
                holder.status.setTextColor(Color.parseColor("#0DCA59"));

                holder.leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        operationButton(demandItem.getRef(),"cancelDemand");
                    }
                });


            }
            break;
            case "excused":{
                holder.status.setText("EXCUSED");
                holder.status.setTextColor(Color.parseColor("#DA760B"));
                holder.leftButton.setVisibility(View.GONE);

            }
            break;
            case "canceled":{
                holder.status.setText("CANCELED");
                holder.status.setTextColor(Color.parseColor("#BA0438"));
                holder.leftButton.setVisibility(View.GONE);
            }
            break;
            case "delivered": {
                holder.status.setText("DELIVERED");
                holder.status.setTextColor(Color.parseColor("#0DCA59"));
                holder.leftButton.setVisibility(View.GONE);
            }
            break;
            default:break;
        }



    }

    @Override
    public int getItemCount() {
        return demandsList.size();
    }

    public static class DemandViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView place;
        private final TextView quantity;
        private final TextView date;
        private final TextView status;
        private final ImageView image;
        private final Button leftButton;

        public DemandViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.adminBooksBookCardViewBookTitle);
            place = itemView.findViewById(R.id.adminBooksBookCardViewRef);
            quantity = itemView.findViewById(R.id.adminBooksBookCardViewQuantity);
            image = itemView.findViewById(R.id.adminBooksBookCardViewImage);
            date = itemView.findViewById(R.id.adminDemandsDemandCardViewDate);
            status = itemView.findViewById(R.id.status);
            leftButton = itemView.findViewById(R.id.left_button);

        }



    }
}
