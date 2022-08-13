package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class UserHomeBooksRecyclerViewAdapter extends RecyclerView.Adapter<UserHomeBooksRecyclerViewAdapter.BookViewHolder>{

    public ArrayList<AdminBooksBookItem> booksList;
    public ArrayList<Integer> demandItList;
    public ArrayList<Integer> fullDemandsList;
    private final Context context;
    private SharedPreferences sp;
    private final String EMAIL = "email";
    private View itemViewP;
    private static final String ROOT_URL = "http://192.168.1.106/";



    public UserHomeBooksRecyclerViewAdapter(ArrayList<AdminBooksBookItem> booksList,ArrayList<Integer> demandItList, ArrayList<Integer> fullDemandsList, Context context)  {
        this.booksList = booksList;
        this.demandItList = demandItList;
        this.fullDemandsList = fullDemandsList;
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);

    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_main_app_home_book_cardview,parent,false);



        return new BookViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        AdminBooksBookItem bookItem = booksList.get(position);
        int demandItItem = demandItList.get(position);
        int fullDemandsItem = fullDemandsList.get(position);

        holder.title.setText(bookItem.getTitle());

        if(demandItItem==0 && fullDemandsItem==0) {
            holder.demandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Demanding the book in progress...", Toast.LENGTH_SHORT).show();
                    String urlAddBook = ROOT_URL + "Android/Unibib/v1/user/operations/demands/demandBook.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAddBook, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject responseObj = new JSONObject(response);
                                if (responseObj.getString("error").equals("false")) {
                                    Toast.makeText(context, responseObj.getString("message"), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, UserMainAppActivity.class);
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                } else {
                                    Toast.makeText(context, responseObj.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("bookRef", bookItem.getRef());
                            params.put("userEmail", sp.getString(EMAIL, null));
                            return params;
                        }
                    };

                    Volley.newRequestQueue(context).add(stringRequest);


                }
            });
        }else{
            holder.demandButton.setVisibility(View.GONE);
        }

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
                bookBundle.putInt("demandIt",demandItItem);
                bookBundle.putInt("fullDemands",fullDemandsItem);

                Intent intent = new Intent(v.getContext(),UserMainAppBooksBookViewActivity.class);
                intent.putExtra("book",  bookBundle);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder{

        private final TextView title;

        private final ImageView image;
        private final Button demandButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.adminBooksBookCardViewBookTitle);

            image = itemView.findViewById(R.id.adminBooksBookCardViewImage);
            demandButton = itemView.findViewById(R.id.buttonBorrow);

        }

    }

}
