package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminMainAppBooksBookModifyActivity extends AppCompatActivity implements ConfirmationDialogUpdateBook.ConfirmationDialogUpdateBookListener{

    private Button backArrow;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText editionEditText;
    private EditText releaseYearEditText;
    private EditText tagsEditText;
    private EditText descriptionEditText;
    private Button saveButton;
    private Button cancelButton;
    private ArrayList<String> shelvesStr,sides,rows,cols;
    private Spinner shelvesSpinner,sidesSpinner,rowsSpinner,colsSpinner;
    private EditText quantity;
    private ArrayList<Shelf> shelvesList;
    private RequestQueue queue;
    private Bundle bookInfo;
    private Button selectImageButton,selectPdfButton;
    private ImageView image;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private ActivityResultLauncher<String> getImage,getPDF;
    private TextView selectedPdfName;
    private Bitmap bitmap;
    private static final String ROOT_URL = "http://192.168.1.106/";
    private Bundle extra;
    private ArrayList<Shelf> shelfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.admin_main_app_books_book_modify_activity);


        backArrow = findViewById(R.id.admin_book_add_place_back_arrow);
        saveButton = findViewById(R.id.adminBookModifySave);
        cancelButton = findViewById(R.id.adminBookModifyCancel);

        titleEditText = findViewById(R.id.adminBookAddQuantity);
        authorEditText = findViewById(R.id.adminBookAddCol);
        editionEditText = findViewById(R.id.adminBookAddEdition);
        releaseYearEditText = findViewById(R.id.adminShelfAddSides);
        tagsEditText = findViewById(R.id.adminBookAddTags);
        descriptionEditText = findViewById(R.id.adminBookAddDesc);


        quantity = findViewById(R.id.adminBookAddQuantity2);
        shelvesSpinner = findViewById(R.id.adminBookAddShelfName2);
        sidesSpinner = findViewById(R.id.adminBookAddSide2);
        rowsSpinner = findViewById(R.id.adminBookAddRow2);
        colsSpinner = findViewById(R.id.adminBookAddCol2);

        shelvesList = new ArrayList<Shelf>();

        image = findViewById(R.id.imageView4);
        selectImageButton = findViewById(R.id.select_image);
        selectPdfButton = findViewById(R.id.select_pdf);
        selectedPdfName = findViewById(R.id.selectedPdfName);

        Intent intent = getIntent();
        bookInfo = (Bundle) intent.getBundleExtra("book");

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleEditText.setText(bookInfo.getString("title"));
        authorEditText.setText(bookInfo.getString("author"));
        editionEditText.setText(bookInfo.getString("edition"));
        releaseYearEditText.setText(bookInfo.getString("releaseYear"));
        quantity.setText(String.valueOf(bookInfo.getInt("quantity")));
        tagsEditText.setText(bookInfo.getString("description"));
        descriptionEditText.setText(bookInfo.getString("tags"));
        Glide.with(this).load(bookInfo.getString("image")).into(image);
        extra = new Bundle();
        shelvesList = new ArrayList<Shelf>();
        String url = "http://192.168.1.106/Android/Unibib/v1/admin/operations/books/getShelves.php";
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Shelf shelfItem;
                        shelfList = new ArrayList<Shelf>();
                        try {
                            JSONArray shelves = new JSONArray(response);
                            for(int i =0;i<shelves.length();i++){
                                JSONObject shelf = shelves.getJSONObject(i);
                                String ref = shelf.getString("ref");
                                String name = shelf.getString("name");
                                int sides  = shelf.getInt("sidesNum");
                                int rows  = shelf.getInt("rowNum");
                                int cols  = shelf.getInt("colNum");


                                shelfItem = new Shelf(ref,name,sides,rows,cols);

                                shelfList.add(shelfItem);

                            }
                            shelvesList = shelfList;

                            shelvesStr = new ArrayList<String>();
                            shelvesStr.add("-- Select shelf of the book --");
                            sides = new ArrayList<String>();
                            sides.add("-- Select shelf's side --");
                            rows = new ArrayList<String>();
                            rows.add("-- Select shelf's row --");
                            cols = new ArrayList<String>();
                            cols.add("-- Select shelf's column --");

                            for (int i=0;i<shelvesList.size();i++) {
                                Shelf item = shelvesList.get(i);
                                shelvesStr.add(item.getName());
                            }

                            String[] shelvesArray = new String[shelvesStr.size()];
                            shelvesArray = shelvesStr.toArray(shelvesArray);

                            ArrayAdapter<String> shelvesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, shelvesArray){
                                @Override
                                public boolean isEnabled(int position) {
                                    return position != 0;
                                }

                                @Override
                                public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                    View view = super.getDropDownView(position, convertView, parent);
                                    TextView textview = (TextView) view;
                                    if (position == 0) {
                                        textview.setTextColor(Color.GRAY);
                                    } else {
                                        textview.setTextColor(Color.WHITE);
                                    }
                                    return view;
                                }
                            };
                            shelvesSpinner.setAdapter(shelvesAdapter);


                            shelvesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedItemText = (String) parent.getItemAtPosition(position);
                                    if (position > 0) {
                                        sides.clear();
                                        rows.clear();
                                        cols.clear();
                                        sides.add("-- Select shelf's side --");
                                        rows.add("-- Select shelf's row --");
                                        cols.add("-- Select shelf's column --");

                                        Shelf item = shelvesList.get(position-1);
                                        sides.add("Front");
                                        if(item.getSides()==2){
                                            sides.add("Back");
                                        }
                                        String[] sidesArr = new String[sides.size()];
                                        sidesArr = sides.toArray(sidesArr);
                                        ArrayAdapter<String> sidesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, sidesArr){
                                            @Override
                                            public boolean isEnabled(int position) {
                                                return position != 0;
                                            }

                                            @Override
                                            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                                View view = super.getDropDownView(position, convertView, parent);
                                                TextView textview = (TextView) view;
                                                if (position == 0) {
                                                    textview.setTextColor(Color.GRAY);
                                                } else {
                                                    textview.setTextColor(Color.WHITE);
                                                }
                                                return view;
                                            }
                                        };
                                        sidesSpinner.setAdapter(sidesAdapter);

                                        for(int i=1;i<=item.getRows();i++){
                                            rows.add(String.valueOf(i));
                                        }
                                        String[] rowsArr = new String[rows.size()];
                                        rowsArr = rows.toArray(rowsArr);
                                        ArrayAdapter<String> rowsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, rowsArr){
                                            @Override
                                            public boolean isEnabled(int position) {
                                                return position != 0;
                                            }

                                            @Override
                                            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                                View view = super.getDropDownView(position, convertView, parent);
                                                TextView textview = (TextView) view;
                                                if (position == 0) {
                                                    textview.setTextColor(Color.GRAY);
                                                } else {
                                                    textview.setTextColor(Color.WHITE);
                                                }
                                                return view;
                                            }
                                        };
                                        rowsSpinner.setAdapter(rowsAdapter);

                                        for(int i=1;i<=item.getCols();i++){
                                            cols.add(String.valueOf(i));
                                        }
                                        String[] colsArr = new String[cols.size()];
                                        colsArr = cols.toArray(colsArr);
                                        ArrayAdapter<String> colsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, colsArr){
                                            @Override
                                            public boolean isEnabled(int position) {
                                                return position != 0;
                                            }

                                            @Override
                                            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                                View view = super.getDropDownView(position, convertView, parent);
                                                TextView textview = (TextView) view;
                                                if (position == 0) {
                                                    textview.setTextColor(Color.GRAY);
                                                } else {
                                                    textview.setTextColor(Color.WHITE);
                                                }
                                                return view;
                                            }
                                        };
                                        colsSpinner.setAdapter(colsAdapter);


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            for(int h=0;h<shelvesSpinner.getAdapter().getCount();h++){
                                if(shelvesSpinner.getItemAtPosition(h).toString().equals(bookInfo.getString("shelfName"))){
                                    int shelvesPosition = shelvesAdapter.getPosition(bookInfo.getString("shelfName"));
                                    shelvesSpinner.setSelection(shelvesPosition);
                                    Shelf item = shelvesList.get((int)shelvesSpinner.getSelectedItemId()-1);
                                    sides.add("Front");
                                    if(item.getSides()==2){
                                        sides.add("Back");
                                    }
                                    String[] sidesArr = new String[sides.size()];
                                    sidesArr = sides.toArray(sidesArr);
                                    ArrayAdapter<String> sidesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, sidesArr){
                                        @Override
                                        public boolean isEnabled(int position) {
                                            return position != 0;
                                        }

                                        @Override
                                        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                            View view = super.getDropDownView(position, convertView, parent);
                                            TextView textview = (TextView) view;
                                            if (position == 0) {
                                                textview.setTextColor(Color.GRAY);
                                            } else {
                                                textview.setTextColor(Color.WHITE);
                                            }
                                            return view;
                                        }
                                    };
                                    sidesSpinner.setAdapter(sidesAdapter);

                                    int sidesPosition = sidesAdapter.getPosition(bookInfo.getString("shelfSide"));
                                    sidesSpinner.setSelection(sidesPosition);

                                    for(int i=1;i<=item.getRows();i++){
                                        rows.add(String.valueOf(i));
                                    }
                                    String[] rowsArr = new String[rows.size()];
                                    rowsArr = rows.toArray(rowsArr);
                                    ArrayAdapter<String> rowsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, rowsArr){
                                        @Override
                                        public boolean isEnabled(int position) {
                                            return position != 0;
                                        }

                                        @Override
                                        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                            View view = super.getDropDownView(position, convertView, parent);
                                            TextView textview = (TextView) view;
                                            if (position == 0) {
                                                textview.setTextColor(Color.GRAY);
                                            } else {
                                                textview.setTextColor(Color.WHITE);
                                            }
                                            return view;
                                        }
                                    };
                                    rowsSpinner.setAdapter(rowsAdapter);

                                    int rowsPosition = rowsAdapter.getPosition(bookInfo.getString("shelfRow"));
                                    rowsSpinner.setSelection(rowsPosition);


                                    for(int i=1;i<=item.getCols();i++){
                                        cols.add(String.valueOf(i));
                                    }
                                    String[] colsArr = new String[cols.size()];
                                    colsArr = cols.toArray(colsArr);
                                    ArrayAdapter<String> colsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, colsArr){
                                        @Override
                                        public boolean isEnabled(int position) {
                                            return position != 0;
                                        }

                                        @Override
                                        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                            View view = super.getDropDownView(position, convertView, parent);
                                            TextView textview = (TextView) view;
                                            if (position == 0) {
                                                textview.setTextColor(Color.GRAY);
                                            } else {
                                                textview.setTextColor(Color.WHITE);
                                            }
                                            return view;
                                        }
                                    };
                                    colsSpinner.setAdapter(colsAdapter);

                                    int colsPosition = colsAdapter.getPosition(bookInfo.getString("shelfCol"));
                                    colsSpinner.setSelection(colsPosition);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "You should verify your connection", Toast.LENGTH_LONG).show();
                        VerifyInternetConnectionDialog dialog = new VerifyInternetConnectionDialog();
                        dialog.show(getSupportFragmentManager(), "no internet connection dialog");
                    }
                });
        Volley.newRequestQueue(AdminMainAppBooksBookModifyActivity.this).add(stringRequest);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int i=0;
                if(shelvesSpinner.getSelectedItem() != null) {
                    if (String.valueOf(titleEditText.getText()).equals("")) {
                        titleEditText.setError("It seems you missed to enter the title");
                        titleEditText.requestFocus();
                        i++;
                    }
                    if (String.valueOf(authorEditText.getText()).equals("")) {
                        authorEditText.setError("It seems you missed to enter the author name");
                        authorEditText.requestFocus();
                        i++;
                    }
                    if (String.valueOf(editionEditText.getText()).equals("")) {
                        editionEditText.setError("It seems you missed to enter the edition");
                        editionEditText.requestFocus();

                        i++;
                    }
                    if (String.valueOf(quantity.getText()).equals("")) {
                        quantity.setError("It seems you missed to enter the book's quantity");
                        quantity.requestFocus();
                        i++;
                    }

                    if (shelvesSpinner.getSelectedItemId() == 0) {
                        TextView errorText = (TextView) shelvesSpinner.getSelectedView();
                        errorText.setError("You miss to enter the shelf name");
                        errorText.requestFocus();
                        i++;
                    } else {
                        if (sidesSpinner.getSelectedItemId() == 0) {
                            TextView errorText = (TextView) sidesSpinner.getSelectedView();
                            errorText.setError("You miss to enter the shelf side");
                            errorText.requestFocus();
                            i++;
                        }
                        if (rowsSpinner.getSelectedItemId() == 0) {
                            TextView errorText = (TextView) rowsSpinner.getSelectedView();
                            errorText.setError("You miss to enter the shelf row");
                            errorText.requestFocus();
                            i++;
                        }
                        if (colsSpinner.getSelectedItemId() == 0) {
                            TextView errorText = (TextView) colsSpinner.getSelectedView();
                            errorText.setError("You miss to enter the shelf column");
                            errorText.requestFocus();
                            i++;
                        }
                    }

                    if (i == 0) {
                        ConfirmationDialogUpdateBook dialog = new ConfirmationDialogUpdateBook();
                        dialog.show(getSupportFragmentManager(), "update dialog");
                    }
                }else{
                    //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "You should verify your connection", Toast.LENGTH_LONG).show();
                    VerifyInternetConnectionDialog dialog = new VerifyInternetConnectionDialog();
                    dialog.show(getSupportFragmentManager(), "no internet connection dialog");
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public class Shelf{
        private String ref,name;
        private int sides,rows,cols;

        public Shelf(String ref, String name, int sides, int rows, int cols) {
            this.ref = ref;
            this.name = name;
            this.sides = sides;
            this.rows = rows;
            this.cols = cols;
        }

        public String getRef() {
            return ref;
        }

        public String getName() {
            return name;
        }

        public int getSides() {
            return sides;
        }

        public int getRows() {
            return rows;
        }

        public int getCols() {
            return cols;
        }
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
    @Override
    public void onUpdateBookYesClicked(){
        extra.putString("ref", bookInfo.getString("ref"));
        extra.putString("title", String.valueOf(titleEditText.getText()));
        extra.putString("author", String.valueOf(authorEditText.getText()));
        extra.putString("edition", String.valueOf(editionEditText.getText()));
        extra.putString("releaseYear", String.valueOf(releaseYearEditText.getText()));
        extra.putString("tags", String.valueOf(tagsEditText.getText()));
        extra.putString("description", String.valueOf(descriptionEditText.getText()));
        extra.putInt("quantity", Integer.parseInt(String.valueOf(quantity.getText())));
        extra.putString("shelfName", shelvesSpinner.getSelectedItem().toString());
        extra.putString("shelfRow", rowsSpinner.getSelectedItem().toString());
        extra.putString("shelfSide", sidesSpinner.getSelectedItem().toString());
        extra.putString("shelfCol", colsSpinner.getSelectedItem().toString());

        Toast.makeText(getApplicationContext(), "Updating the book in progress...", Toast.LENGTH_SHORT).show();
        String url = ROOT_URL + "Android/Unibib/v1/admin/operations/books/modifyBook.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response);
                    if (responseObj.getString("error").equals("false")) {
                        Toast.makeText(getApplicationContext(), responseObj.getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), AdminMainAppActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), responseObj.getString("message"), Toast.LENGTH_LONG).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("ref", extra.getString("ref"));
                params.put("title", extra.getString("title"));
                params.put("author", extra.getString("author"));
                params.put("description", extra.getString("description"));
                params.put("edition", extra.getString("edition"));
                params.put("tags", extra.getString("tags"));
                params.put("releaseY", extra.getString("releaseYear"));
                params.put("quantity", String.valueOf(extra.getInt("quantity")));
                params.put("shelf", extra.getString("shelfName"));
                params.put("side", extra.getString("shelfSide"));
                params.put("row", extra.getString("shelfRow"));
                params.put("col", extra.getString("shelfCol"));
                String imageData = "";
                if (bitmap != null) {
                    imageData = ImageToString(bitmap);
                }
                params.put("image", imageData);
                params.put("pdf", "");


                return params;
            }
        };

        Volley.newRequestQueue(AdminMainAppBooksBookModifyActivity.this).add(stringRequest);
    }
}