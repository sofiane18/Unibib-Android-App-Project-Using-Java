package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserTagsRecyclerViewAdapter extends RecyclerView.Adapter<UserTagsRecyclerViewAdapter.TagViewHolder> {

    public ArrayList<String> tagsList;
    private final Context context;
    private static final String ROOT_URL = "http://192.168.1.106/";


    public UserTagsRecyclerViewAdapter(ArrayList<String> tagsList, Context context) {
        this.tagsList = tagsList;
        this.context = context;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_main_app_tags_tag_cardview,parent,false);
        return new UserTagsRecyclerViewAdapter.TagViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        String tag = tagsList.get(position);
        holder.tag.setText(tag);
        holder.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle TagsBundle = new Bundle();
                TagsBundle.putString("tag",tag);

                Intent intent = new Intent(v.getContext(),UserMainAppTagsTagViewActivity.class);
                intent.putExtra("tags",  TagsBundle);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {

        private final Button tag;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);

            tag = itemView.findViewById(R.id.buttonTag);
            
        }

    }
}

