package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserBorrowingsRecyclerViewAdapter extends RecyclerView.Adapter<UserBorrowingsRecyclerViewAdapter.BorrowingViewHolder> {

    public ArrayList<AdminBorrowingsBorrowingItem> borrowingsList;
    private final Context context;
    private static final String ROOT_URL = "http://192.168.1.106/";


    public UserBorrowingsRecyclerViewAdapter(ArrayList<AdminBorrowingsBorrowingItem> borrowingsList, Context context) {
        this.borrowingsList = borrowingsList;
        this.context = context;
    }

    @NonNull
    @Override
    public BorrowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_main_app_borrowings_borrowing_cardview,parent,false);
        return new UserBorrowingsRecyclerViewAdapter.BorrowingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BorrowingViewHolder holder, int position) {
        AdminBorrowingsBorrowingItem borrowingItem = borrowingsList.get(position);

        holder.title.setText(borrowingItem.getDemandItem().bookItem.getTitle());
        holder.bookRef.setText("BookID : " + borrowingItem.getDemandItem().bookItem.getRef());
        holder.id.setText("UserID : "+ borrowingItem.getDemandItem().userItem.getRef());
        holder.date.setText("Date : "+ borrowingItem.getDeliverDate());
        Glide.with(context).load(borrowingItem.getDemandItem().bookItem.getImage()).into(holder.image);

        if(borrowingItem.getRetrieved() == 1){
            holder.status.setText("RETRIEVED");
            holder.status.setTextColor(Color.parseColor("#0DCA59"));
        }else{
            if(borrowingItem.getLate() == 0){
                holder.status.setText("FINE");
                holder.status.setTextColor(Color.parseColor("#1A5B97"));
            }else{
                holder.info_indicator.setImageResource(R.drawable.ic_baseline_info_24);
                holder.status.setText("LATE");
                holder.status.setTextColor(Color.parseColor("#BA0438"));
            }
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


                Intent intent = new Intent(v.getContext(),UserMainAppBorrowingsBorrowingViewActivity.class);
                intent.putExtra("borrowing",  borrowingBundle);
                context.startActivity(intent);

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
        private final TextView id;
        private final TextView date;
        private final TextView status;
        private final ImageView image;
        private final ImageView info_indicator;


        public BorrowingViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.adminBooksBookCardViewBookTitle);
            bookRef = itemView.findViewById(R.id.adminBooksBookCardViewRef);
            image = itemView.findViewById(R.id.adminBooksBookCardViewImage);
            id = itemView.findViewById(R.id.adminDemandsDemandCardViewId);
            date = itemView.findViewById(R.id.adminDemandsDemandCardViewDate);
            info_indicator = itemView.findViewById(R.id.info_indicator);
            status = itemView.findViewById(R.id.status);


        }

    }
}

