package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

public class AdminBorrowingsRecyclerViewAdapter extends RecyclerView.Adapter<AdminBorrowingsRecyclerViewAdapter.BorrowingViewHolder> {

    public ArrayList<AdminBorrowingsBorrowingItem> borrowingsList;
    private final Context context;
    private static final String ROOT_URL = "http://192.168.1.106/";


    public AdminBorrowingsRecyclerViewAdapter(ArrayList<AdminBorrowingsBorrowingItem> borrowingsList, Context context) {
        this.borrowingsList = borrowingsList;
        this.context = context;
    }

    @NonNull
    @Override
    public BorrowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_main_app_borrowings_borrowing_cardview,parent,false);
        return new AdminBorrowingsRecyclerViewAdapter.BorrowingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BorrowingViewHolder holder, int position) {
        AdminBorrowingsBorrowingItem borrowingItem = borrowingsList.get(position);

        holder.title.setText(borrowingItem.getDemandItem().bookItem.getTitle());
        holder.bookRef.setText("BookID : " + borrowingItem.getDemandItem().bookItem.getRef());
        holder.phone.setText("Phone : "+ borrowingItem.getDemandItem().userItem.getPhone());
        holder.email.setText("Email : "+ borrowingItem.getDemandItem().userItem.getEmail());
        holder.name.setText("" + borrowingItem.getDemandItem().userItem.getLName() + " "+ borrowingItem.getDemandItem().userItem.getFName());
        holder.id.setText("UserID : "+ borrowingItem.getDemandItem().userItem.getRef());
        holder.date.setText("Date : "+ borrowingItem.getDeliverDate());
        Glide.with(context).load(borrowingItem.getDemandItem().bookItem.getImage()).into(holder.image);
        if(borrowingItem.getLate() == 1){
            holder.info_indicator.setImageResource(R.drawable.ic_baseline_info_24);
        }

        if(borrowingItem.getRetrieved() == 1){
            holder.retrivButton.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle borrowingBundle = new Bundle();
                borrowingBundle.putString("bookRef",borrowingItem.getDemandItem().bookItem.getRef());
                borrowingBundle.putString("title",borrowingItem.getDemandItem().bookItem.getTitle());
                borrowingBundle.putString("image",borrowingItem.getDemandItem().bookItem.getImage());
                borrowingBundle.putString("author",borrowingItem.getDemandItem().bookItem.getAuthor());
                borrowingBundle.putString("edition",borrowingItem.getDemandItem().bookItem.getEdition());
                borrowingBundle.putInt("quantity",borrowingItem.getDemandItem().bookItem.getQuantity());
                borrowingBundle.putString("place",borrowingItem.getDemandItem().bookItem.getPlace());

                borrowingBundle.putString("userRef",borrowingItem.getDemandItem().userItem.getRef());
                borrowingBundle.putString("fName",borrowingItem.getDemandItem().userItem.getFName());
                borrowingBundle.putString("lName",borrowingItem.getDemandItem().userItem.getLName());
                borrowingBundle.putString("property",borrowingItem.getDemandItem().userItem.getProperty());
                borrowingBundle.putString("email",borrowingItem.getDemandItem().userItem.getEmail());
                borrowingBundle.putString("phone",borrowingItem.getDemandItem().userItem.getPhone());
                borrowingBundle.putString("address",borrowingItem.getDemandItem().userItem.getAddress());

                borrowingBundle.putString("deliverDate",borrowingItem.getDeliverDate());
                if(borrowingItem.getLate() == 1){
                    borrowingBundle.putString("status","Late");
                }else{
                    borrowingBundle.putString("status","Fine");
                }
                borrowingBundle.putString("ref",borrowingItem.getRef());
                borrowingBundle.putInt("retrieved",borrowingItem.getRetrieved());


                Intent intent = new Intent(v.getContext(),AdminMainAppBorrowingsBorrowingViewActivity.class);
                intent.putExtra("borrowing",  borrowingBundle);
                context.startActivity(intent);

            }
        });


        holder.retrivButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = ROOT_URL + "Android/Unibib/v1/admin/operations/borrowings/retrieveBorrowing.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            if(responseObj.getString("error").equals("false")){
                                Toast.makeText(context, responseObj.getString("message"),Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context,AdminMainAppActivity.class);
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
                        dialog.show(((FragmentActivity)v.getContext()).getSupportFragmentManager(), "no internet connection dialog");
                    }
                }){
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("ref",borrowingItem.getRef());
                        return params;
                    }
                };
                Volley.newRequestQueue(context).add(stringRequest);
            }

        });



    }

    @Override
    public int getItemCount() {
        return borrowingsList.size();
    }

    public static class BorrowingViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView bookRef;
        private final TextView phone;
        private final TextView email;
        private final TextView name;
        private final TextView id;
        private final TextView date;
        private final ImageView image;
        private final ImageView info_indicator;
        private final Button retrivButton;

        public BorrowingViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.adminBooksBookCardViewBookTitle);
            bookRef = itemView.findViewById(R.id.adminBooksBookCardViewRef);
            phone = itemView.findViewById(R.id.adminBooksBookCardViewPhone);
            email = itemView.findViewById(R.id.adminBooksBookCardViewEmail);
            image = itemView.findViewById(R.id.adminBooksBookCardViewImage);
            name = itemView.findViewById(R.id.adminDemandsDemandCardViewName);
            id = itemView.findViewById(R.id.adminDemandsDemandCardViewId);
            date = itemView.findViewById(R.id.adminDemandsDemandCardViewDate);
            info_indicator = itemView.findViewById(R.id.info_indicator);
            retrivButton = itemView.findViewById(R.id.right_button);



        }

    }
}

