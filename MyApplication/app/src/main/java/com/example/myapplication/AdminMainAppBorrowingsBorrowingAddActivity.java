package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminMainAppBorrowingsBorrowingAddActivity extends AppCompatActivity {
    private Button selectBookButton,selectUserButton,backButton,addButton;
    private Bundle selectedBook,selectedUser;
    private ConstraintLayout bookCard,userCard;
    private TextView title;
    private TextView author;
    private TextView place;
    private TextView quantity;
    private ImageView image;

    private TextView userRef;
    private TextView userName;
    private TextView birthDate;
    private TextView property;
    private TextView email;
    private static final String ROOT_URL = "http://192.168.1.106/";

    ActivityResultLauncher<Intent> arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result != null && result.getResultCode()==RESULT_OK){
                if(result.getData() != null && result.getData().getBundleExtra("selectedBook") != null){
                    selectedBook = result.getData().getBundleExtra("selectedBook");
                    showBookCard();
                }
                if(result.getData() != null && result.getData().getBundleExtra("selectedUser") != null){
                    selectedUser = result.getData().getBundleExtra("selectedUser");
                    showUserCard();
                }

            }
        }
    });

    @SuppressLint("SetTextI18n")
    private void showBookCard() {
        title = findViewById(R.id.adminBooksBookCardViewBookTitle);
        author = findViewById(R.id.adminBooksBookCardViewAuthorName);
        place = findViewById(R.id.adminBooksBookCardViewRef);
        quantity = findViewById(R.id.adminBooksBookCardViewQuantity);
        image = findViewById(R.id.adminBooksBookCardViewImage);
        title.setText(selectedBook.getString("title"));
        author.setText("By: "+selectedBook.getString("author"));
        place.setText("Place: "+selectedBook.getString("place"));
        quantity.setText("Quantity: "+selectedBook.getInt("quantity"));
        Glide.with(this).load(selectedBook.getString("image")).into(image);
        bookCard.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void showUserCard() {
        userName = findViewById(R.id.adminUsersUserCardViewName);
        userRef = findViewById(R.id.adminUsersUserCardViewID);
        birthDate = findViewById(R.id.adminUsersUserCardViewBirthDate);
        property = findViewById(R.id.adminUsersUserCardViewProperty);
        email = findViewById(R.id.adminUsersUserCardViewEmail);

        userName.setText(selectedUser.getString("name"));
        userRef.setText("ID : "+selectedUser.getString("ref"));
        birthDate.setText(selectedUser.getString("birthDate"));
        property.setText(selectedUser.getString("property"));
        email.setText(selectedUser.getString("email"));
        userCard.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.admin_main_app_borrowings_borrowing_add_activity);
        bookCard = findViewById(R.id.bookCard);
        bookCard.setVisibility(View.GONE);
        userCard = findViewById(R.id.userCard);
        userCard.setVisibility(View.GONE);
        selectUserButton = findViewById(R.id.select_user);
        selectBookButton = findViewById(R.id.select_book);
        backButton = findViewById(R.id.admin_book_add_place_back_arrow);
        addButton = findViewById(R.id.adminBookAddAddButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        selectBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainAppBorrowingsBorrowingAddActivity.this,AdminMainAppBooksSelectingSearchActivity.class);
                arl.launch(intent);
            }
        });
        selectUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainAppBorrowingsBorrowingAddActivity.this,AdminMainAppUsersSelectingSearchActivity.class);
                arl.launch(intent);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=0;
                if(selectedBook.getString("ref") == null){
                    selectBookButton.setError("It seems you missed to select a book");
                    selectBookButton.requestFocus();
                    i++;
                }
                if(selectedUser.getString("ref") == null){
                    selectUserButton.setError("It seems you missed to select a user");
                    selectUserButton.requestFocus();
                    i++;
                }


                if (i==0){
                    String URL = ROOT_URL + "Android/Unibib/v1/admin/operations/borrowings/addBorrowing.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject responseObj = new JSONObject(response);
                                if(responseObj.getString("error").equals("false")){
                                    Toast.makeText(getApplicationContext(),responseObj.getString("message"),Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(),AdminMainAppActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(),responseObj.getString("message"),Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("bookRef",selectedBook.getString("ref"));
                            params.put("userRef",selectedUser.getString("ref"));

                            return params;
                        }
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

                }
            }

        });
    }
}