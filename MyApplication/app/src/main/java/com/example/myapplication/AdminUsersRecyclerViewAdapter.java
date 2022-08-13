package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminUsersRecyclerViewAdapter extends RecyclerView.Adapter<AdminUsersRecyclerViewAdapter.UserViewHolder> {

        public ArrayList<AdminUsersUserItem> usersList;
        private final Context context;
        private CardView cardViewItem = null;
        public AdminUsersRecyclerViewAdapter(ArrayList<AdminUsersUserItem> usersList, Context context) {
            this.usersList = usersList;
            this.context = context;
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_main_app_borrowings_users_user_cardview,parent,false);



            return new UserViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull AdminUsersRecyclerViewAdapter.UserViewHolder holder, int position) {

            AdminUsersUserItem userItem = usersList.get(position);

            holder.userName.setText(userItem.getLName()+" "+userItem.getFName());
            holder.userRef.setText("ID : "+userItem.getRef());
            holder.birthDate.setText("BirthDate : "+userItem.getBirthDate());
            holder.property.setText("Property : "+userItem.getProperty());
            holder.email.setText("Email : "+userItem.getEmail());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cardViewItem!=null){
                        cardViewItem.setBackgroundColor(ContextCompat.getColor(context,R.color.unselected));
                    }
                    cardViewItem = holder.userCardView;
                    holder.userCardView.setBackgroundColor(ContextCompat.getColor(context,R.color.selected));

                    Bundle userBundle = new Bundle();
                    userBundle.putString("ref",userItem.getRef());
                    userBundle.putString("name",userItem.getLName()+" "+userItem.getFName());
                    userBundle.putString("birthDate","BirthDate : "+userItem.getBirthDate());
                    userBundle.putString("property","Property : "+userItem.getProperty());
                    userBundle.putString("email","Email : "+userItem.getEmail());

                    Intent intent = new Intent("selectedUser");
                    intent.putExtra("selectedUser",userBundle);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return usersList.size();
        }

        public static class UserViewHolder extends RecyclerView.ViewHolder{

            private final TextView userName;
            private final TextView userRef;
            private final TextView birthDate;
            private final TextView property;
            private final TextView email;
            private final CardView userCardView;


            public UserViewHolder(@NonNull View itemView) {
                super(itemView);

                userName = itemView.findViewById(R.id.adminUsersUserCardViewName);
                userRef = itemView.findViewById(R.id.adminUsersUserCardViewID);
                birthDate = itemView.findViewById(R.id.adminUsersUserCardViewBirthDate);
                property = itemView.findViewById(R.id.adminUsersUserCardViewProperty);
                email = itemView.findViewById(R.id.adminUsersUserCardViewEmail);
                userCardView = itemView.findViewById(R.id.userCardView);


            }

        }
    }
