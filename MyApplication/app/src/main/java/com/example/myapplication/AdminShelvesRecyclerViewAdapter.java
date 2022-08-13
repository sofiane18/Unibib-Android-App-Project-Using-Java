package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminShelvesRecyclerViewAdapter extends RecyclerView.Adapter<AdminShelvesRecyclerViewAdapter.ShelfViewHolder>{


    public ArrayList<AdminShelvesShelfItem> shelvesList;
    private final Context context;

    public AdminShelvesRecyclerViewAdapter(ArrayList<AdminShelvesShelfItem> shelvesList, Context context) {
        this.shelvesList = shelvesList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminShelvesRecyclerViewAdapter.ShelfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_main_app_shelves_shelf_cardview,parent,false);



        return new AdminShelvesRecyclerViewAdapter.ShelfViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdminShelvesRecyclerViewAdapter.ShelfViewHolder holder, int position) {

        AdminShelvesShelfItem shelfItem = shelvesList.get(position);

        holder.name.setText(shelfItem.getName());
        holder.ref.setText("Ref : " + shelfItem.getRef());
        holder.sides.setText("Sides : " + shelfItem.getSides());
        holder.rows.setText("Rows : "+ shelfItem.getRows());
        holder.cols.setText("Columns : "+ shelfItem.getCols());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle shelfBundle = new Bundle();
                Bundle books = new Bundle();
                shelfBundle.putString("ref",shelfItem.getRef());
                shelfBundle.putString("name",shelfItem.getName());
                shelfBundle.putInt("sides",shelfItem.getSides());
                shelfBundle.putInt("rows",shelfItem.getRows());
                shelfBundle.putInt("cols",shelfItem.getCols());

                int i=0;
                for (AdminBooksBookItem shelfItem : shelfItem.getBooksList()) {
                    Bundle book = new Bundle();
                    book.putString("ref",shelfItem.getRef());
                    book.putString("title",shelfItem.getTitle());
                    book.putString("image",shelfItem.getImage());
                    book.putString("author",shelfItem.getAuthor());
                    book.putString("edition",shelfItem.getEdition());
                    book.putString("description",shelfItem.getDescription());
                    book.putString("tags",shelfItem.getTags());
                    book.putInt("quantity",shelfItem.getQuantity());
                    book.putString("place",shelfItem.getPlace());
                    books.putBundle(String.valueOf(i),book);
                    i++;
                }



                Intent intent = new Intent(v.getContext(),AdminMainAppShelvesShelfViewActivity.class);
                intent.putExtra("shelf",  shelfBundle);
                intent.putExtra("books",  books);
                context.startActivity(intent);


        }});

        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                popupMenu.inflate(R.menu.admin_shelves_shelf_card_view_options_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.modifyBookItem: {
                                Bundle ShelfBundle = new Bundle();
                                ShelfBundle.putString("ref", shelfItem.getRef());
                                ShelfBundle.putString("name", shelfItem.getName());
                                ShelfBundle.putInt("sides", shelfItem.getSides());
                                ShelfBundle.putInt("rows", shelfItem.getRows());
                                ShelfBundle.putInt("cols", shelfItem.getCols());

                                Intent intent = new Intent(v.getContext(), AdminMainAppShelvesShelfModifyActivity.class);
                                intent.putExtra("shelf", ShelfBundle);
                                context.startActivity(intent);
                            }
                            return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return shelvesList.size();
    }

    public static class ShelfViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView ref;
        private final TextView sides;
        private final TextView rows;
        private final TextView cols;
        private final ImageButton option;

        public ShelfViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.adminShelvesShelfCardViewName);
            ref = itemView.findViewById(R.id.adminShelvesShelfCardViewRef);
            sides = itemView.findViewById(R.id.adminShelvesShelfCardViewSide);
            rows = itemView.findViewById(R.id.adminShelvesShelfCardViewRow);
            cols = itemView.findViewById(R.id.adminShelvesShelfCardViewCol);
            option = itemView.findViewById(R.id.adminShelvesShelfCardViewOptionsButton);
        }

    }
}
