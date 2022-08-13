package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminMainAppBooksBookAddActivityPlace extends AppCompatActivity {
    private ArrayList<String> shelvesStr,sides,rows,cols;
    private Spinner shelvesSpinner,sidesSpinner,rowsSpinner,colsSpinner;
    private EditText quantity;
    private ArrayList<Shelf> shelvesList;
    private RequestQueue queue;
    private Button backArrow,nextButton;
    private Bundle bookInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.admin_main_app_books_book_add_activity_place);
        Intent intent = getIntent();
        bookInfo = (Bundle) intent.getBundleExtra("bookInfo");
        quantity = findViewById(R.id.adminBookAddQuantity);
        nextButton = findViewById(R.id.adminBookAddAddButton);
        backArrow = findViewById(R.id.admin_book_add_place_back_arrow);
        shelvesSpinner = findViewById(R.id.adminBookAddShelfName);
        sidesSpinner = findViewById(R.id.adminBookAddSide);
        rowsSpinner = findViewById(R.id.adminBookAddRow);
        colsSpinner = findViewById(R.id.adminBookAddCol);
        shelvesList = new ArrayList<Shelf>();
        String url = "http://192.168.1.106/Android/Unibib/v1/admin/operations/books/getShelves.php";
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Shelf shelfItem;
                        ArrayList<Shelf> shelfList = new ArrayList<Shelf>();
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
        Volley.newRequestQueue(this.getBaseContext()).add(stringRequest);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int i=0;
                if(String.valueOf(quantity.getText()).equals("")){
                    quantity.setError("It seems you missed to enter the book's quantity");
                    quantity.requestFocus();
                    i++;
                }
                if(shelvesSpinner.getSelectedItem() != null) {
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
                        bookInfo.putInt("quantity", Integer.valueOf(String.valueOf(quantity.getText())));
                        bookInfo.putString("name", shelvesSpinner.getSelectedItem().toString());
                        bookInfo.putString("row", rowsSpinner.getSelectedItem().toString());
                        bookInfo.putString("side", sidesSpinner.getSelectedItem().toString());
                        bookInfo.putString("col", colsSpinner.getSelectedItem().toString());

                        Intent intent = new Intent(v.getContext(), AdminMainAppBooksBookAddActivityFiles.class);
                        intent.putExtra("bookInfo", bookInfo);
                        startActivity(intent);
                    }
                }else{
                    //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "You should verify your connection", Toast.LENGTH_LONG).show();
                    VerifyInternetConnectionDialog dialog = new VerifyInternetConnectionDialog();
                    dialog.show(getSupportFragmentManager(), "no internet connection dialog");
                }
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
}