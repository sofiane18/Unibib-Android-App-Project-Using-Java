package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminBooksRecyclerViewAdapter extends RecyclerView.Adapter<AdminBooksRecyclerViewAdapter.BookViewHolder> implements ConfirmationDialogDeleteBook.ConfirmationDialogDeleteBookListener{
    public ArrayList<AdminBooksBookItem> booksList;
    private final Context context;
    private AdminBooksBookItem bookItemP;
    private View itemViewP;
    private static final String ROOT_URL = "http://192.168.1.106/";
    public AdminBooksRecyclerViewAdapter(ArrayList<AdminBooksBookItem> booksList, Context context)  {
        this.booksList = booksList;
        this.context = context;
    }
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_main_app_books_book_cardview,parent,false);


        return new BookViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        AdminBooksBookItem bookItem = booksList.get(position);

        holder.title.setText(bookItem.getTitle());
        holder.author.setText("By: " + bookItem.getAuthor());
        holder.place.setText("Place: " + bookItem.getPlace());
        holder.quantity.setText("Quantity: "+ bookItem.getQuantity());
        Glide.with(context).load(bookItem.getImage()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bookBundle = new Bundle();
                bookBundle.putString("ref",bookItem.getRef());
                bookBundle.putString("title",bookItem.getTitle());
                bookBundle.putString("image",bookItem.getImage());
                bookBundle.putString("author",bookItem.getAuthor());
                bookBundle.putString("edition",bookItem.getEdition());
                bookBundle.putString("description",bookItem.getDescription());
                bookBundle.putString("tags",bookItem.getTags());
                bookBundle.putInt("quantity",bookItem.getQuantity());
                bookBundle.putString("place",bookItem.getPlace());
                bookBundle.putString("shelfName",bookItem.getShelfName());
                bookBundle.putString("shelfSide",bookItem.getShelfSide());
                bookBundle.putString("shelfRow",bookItem.getShelfRow());
                bookBundle.putString("shelfCol",bookItem.getShelfCol());

                Intent intent = new Intent(v.getContext(),AdminMainAppBooksBookViewActivity.class);
                intent.putExtra("book",  bookBundle);
                context.startActivity(intent);

            }
        });


        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                popupMenu.inflate(R.menu.admin_books_book_card_view_options_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.modifyBookItem: {
                                Bundle bookBundle = new Bundle();
                                bookBundle.putString("ref", bookItem.getRef());
                                bookBundle.putString("title", bookItem.getTitle());
                                bookBundle.putString("image", bookItem.getImage());
                                bookBundle.putString("author", bookItem.getAuthor());
                                bookBundle.putString("edition", bookItem.getEdition());
                                bookBundle.putString("description", bookItem.getDescription());
                                bookBundle.putString("tags", bookItem.getTags());
                                bookBundle.putInt("quantity", bookItem.getQuantity());
                                bookBundle.putString("place", bookItem.getPlace());
                                bookBundle.putString("shelfName", bookItem.getShelfName());
                                bookBundle.putString("shelfSide", bookItem.getShelfSide());
                                bookBundle.putString("shelfRow", bookItem.getShelfRow());
                                bookBundle.putString("shelfCol", bookItem.getShelfCol());

                                Intent intent = new Intent(v.getContext(), AdminMainAppBooksBookModifyActivity.class);
                                intent.putExtra("book", bookBundle);
                                context.startActivity(intent);
                            }
                                return true;

                            case R.id.deleteBookItem:{
                                ConfirmationDialogDeleteBook dialog = new ConfirmationDialogDeleteBook();
                                dialog.show(((FragmentActivity)v.getContext()).getSupportFragmentManager(), "delete dialog");
                                bookItemP = bookItem;
                                itemViewP = holder.itemView;
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
        return booksList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder{

        private final TextView title;
        private final TextView author;
        private final TextView place;
        private final TextView quantity;
        private final ImageView image;
        private final ImageButton option;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.adminBooksBookCardViewBookTitle);
            author = itemView.findViewById(R.id.adminBooksBookCardViewAuthorName);
            place = itemView.findViewById(R.id.adminBooksBookCardViewRef);
            quantity = itemView.findViewById(R.id.adminBooksBookCardViewQuantity);
            image = itemView.findViewById(R.id.adminBooksBookCardViewImage);
            option = itemView.findViewById(R.id.adminBooksBookCardViewOptionsButton);

        }

    }

    @Override
    public void onDeleteBookYesClicked(){

        String url =  ROOT_URL + "Android/Unibib/v1/admin/operations/books/deleteBook.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response);
                    if(responseObj.getString("error").equals("false")){
                        Toast.makeText(itemViewP.getContext(),responseObj.getString("message"),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(itemViewP.getContext(),AdminMainAppActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }else{
                        Toast.makeText(itemViewP.getContext(),responseObj.getString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(itemViewP.getContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("ref",bookItemP.getRef());
                return params;
            }
        };
        Volley.newRequestQueue(itemViewP.getContext()).add(stringRequest);
    }

}
