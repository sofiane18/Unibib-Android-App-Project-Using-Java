package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
public class AdminMainAppBooksBookAddActivityFiles extends AppCompatActivity {
    private Button selectImageButton,selectPdfButton,backButton,addButton;
    private ImageView image;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private Bundle bookInfo;
    private ActivityResultLauncher<String> getImage,getPDF;
    private TextView selectedPdfName;
    private Bitmap bitmap;
    private static final String ROOT_URL = "http://192.168.1.106/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        setContentView(R.layout.admin_main_app_books_book_add_activity_files);

        image = findViewById(R.id.imageView4);
        selectImageButton = findViewById(R.id.select_image);
        selectPdfButton = findViewById(R.id.select_pdf);
        selectedPdfName = findViewById(R.id.selectedPdfName);
        backButton = findViewById(R.id.admin_book_add_place_back_arrow);
        addButton = findViewById(R.id.adminBookAddAddButton);

        Intent intent = getIntent();
        bookInfo = intent.getBundleExtra("bookInfo");

        getImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result != null) {
                    image.setImageURI(result);
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(result);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        getPDF = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onActivityResult(Uri result) {
                if(result != null) {
                    selectedPdfName.setText("PDF file selected : " + getFileName(result));

                }
            }
        });


        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AdminMainAppBooksBookAddActivityFiles.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION);
                }else{
                    selectImage();
                }
            }
        });

        selectPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AdminMainAppBooksBookAddActivityFiles.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION);
                }else{
                    selectPDF();
                }
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Adding the book in progress...",Toast.LENGTH_SHORT).show();
                String urlAddBook =  ROOT_URL + "Android/Unibib/v1/admin/operations/books/addBook.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAddBook, new Response.Listener<String>() {
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), "You should verify your connection", Toast.LENGTH_LONG).show();
                            VerifyInternetConnectionDialog dialog = new VerifyInternetConnectionDialog();
                            dialog.show(getSupportFragmentManager(), "no internet connection dialog");
                    }
                }){
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError{
                        Map<String,String> params = new HashMap<>();

                        params.put("title",bookInfo.getString("title"));
                        params.put("author",bookInfo.getString("author"));
                        params.put("description",bookInfo.getString("description"));
                        params.put("edition",bookInfo.getString("edition"));
                        params.put("tags",bookInfo.getString("tags"));
                        params.put("releaseY",bookInfo.getString("releaseYear"));
                        params.put("quantity",String.valueOf(bookInfo.getInt("quantity")));
                        params.put("shelf",bookInfo.getString("name"));
                        params.put("side",bookInfo.getString("side"));
                        params.put("row",bookInfo.getString("row"));
                        params.put("col",bookInfo.getString("col"));
                        String imageData = "";
                        if(bitmap!=null){
                            imageData = ImageToString(bitmap);
                        }
                        params.put("image",imageData);
                        params.put("pdf","");


                        return params;
                    }
                };

                Volley.newRequestQueue(AdminMainAppBooksBookAddActivityFiles.this).add(stringRequest);



            }
        });




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    private void selectImage(){

            getImage.launch("image/*");

    }
    public void selectPDF(){
        getPDF.launch("application/pdf");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    public String ImageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}