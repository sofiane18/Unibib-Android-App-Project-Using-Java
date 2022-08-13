package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminMainAppShelvesShelfModifyActivity extends AppCompatActivity {
    private String ROOT_URL = "http://192.168.1.106/";
    private Bundle shelf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.admin_main_app_shelves_shelf_add_activity);

        EditText name = findViewById(R.id.adminShelfAddName);
        Spinner sides = findViewById(R.id.adminShelfAddSides);
        EditText rows = findViewById(R.id.adminShelfAddRows);
        EditText cols = findViewById(R.id.adminShelfAddCols);
        Button save = findViewById(R.id.adminShelfModifySave);
        Button cancel = findViewById(R.id.adminShelfModifyCancel);
        Button backArrow = findViewById(R.id.admin_book_add_place_back_arrow);

        shelf = new Bundle();
        Intent intent = getIntent();
        shelf = (Bundle) intent.getBundleExtra("shelf");

        name.setText(shelf.getString("name"));
        rows.setText(String.valueOf(shelf.getInt("rows")));
        cols.setText(String.valueOf(shelf.getInt("cols")));

        ArrayList<String> sidesList = new ArrayList<>();
        sidesList.add("-- Select Sides Number --");
        sidesList.add("1");
        sidesList.add("2");

        String[] sidesArray = new String[sidesList.size()];
        sidesArray = sidesList.toArray(sidesArray);

        ArrayAdapter<String> sidesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_items, sidesArray){
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
        sides.setAdapter(sidesAdapter);

        int sidesPosition = sidesAdapter.getPosition(shelf.getString("sides"));
        sides.setSelection(sidesPosition);

        sides.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int i=0;
                if(String.valueOf(name.getText()).equals("")){
                    name.setError("It seems you missed to enter the shelf's name");
                    name.requestFocus();
                    i++;
                }
                if(String.valueOf(rows.getText()).equals("")){
                    rows.setError("It seems you missed to enter the number of shelf rows");
                    rows.requestFocus();
                    i++;
                }
                if(String.valueOf(cols.getText()).equals("")){
                    cols.setError("It seems you missed to enter the number of shelf columns");
                    cols.requestFocus();
                    i++;
                }

                if (sides.getSelectedItemId()==0) {
                    TextView errorText= (TextView) sides.getSelectedView();
                    errorText.setError("You missed to enter the shelf's sides number");
                    errorText.requestFocus();
                    i++;
                }

                if (i==0){
                    String SHELVES_URL = ROOT_URL + "Android/Unibib/v1/admin/operations/shelves/modifyShelf.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, SHELVES_URL, new Response.Listener<String>() {
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

                            params.put("ref",shelf.getString("ref"));
                            params.put("name",name.getText().toString());
                            params.put("sides",sides.getSelectedItem().toString());
                            params.put("rows",rows.getText().toString());
                            params.put("cols",cols.getText().toString());

                            return params;
                        }
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

                }
            }
        });

    }
}