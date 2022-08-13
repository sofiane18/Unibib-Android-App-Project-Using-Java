package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserMainAppProfileFragment extends Fragment {
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private TextView name,email,ref,account,birthDate,gender,phone,address;
    private Button logout;
    private final String EMAIL = "email";
    private final String ACCOUNT_MODE = "mode";
    private static final String ROOT_URL = "http://192.168.1.106/";

    public UserMainAppProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_main_app_profile_fragment, container, false);

        sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        address = v.findViewById(R.id.address);
        phone = v.findViewById(R.id.phone);
        ref = v.findViewById(R.id.ref);
        gender = v.findViewById(R.id.gender);
        account = v.findViewById(R.id.account);
        birthDate = v.findViewById(R.id.birthDate);
        logout = v.findViewById(R.id.logOut);

        email.setText("Email : "+sp.getString(EMAIL,""));
        account.setText("Account : "+sp.getString(ACCOUNT_MODE,""));


        String URL = ROOT_URL + "Android/Unibib/v1/user/operations/profile/getUserProfileInfo.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray profiles = new JSONArray(response);
                            for(int i =0;i<profiles.length();i++){
                                JSONObject profile = profiles.getJSONObject(i);

                                ref.setText("ID : "+profile.getString("ref"));
                                gender.setText("gender : "+profile.getString("gender"));
                                birthDate.setText("BirthDate : "+profile.getString("birthDate"));
                                name.setText(""+profile.getString("lastName")+" "+profile.getString("firstName"));
                                address.setText("Address : "+profile.getString("address"));
                                phone.setText("Phone : "+profile.getString("phone"));


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        //Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                        VerifyInternetConnectionDialog dialog = new VerifyInternetConnectionDialog();
                        dialog.show(((FragmentActivity)UserMainAppProfileFragment.this.getContext()).getSupportFragmentManager(), "no internet connection dialog");
                        



                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("email",sp.getString(EMAIL,null));


                return params;
            }
        };

        Volley.newRequestQueue(this.requireContext()).add(stringRequest);






        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spEditor = sp.edit();
                spEditor.clear();
                spEditor.commit();
                Intent intent = new Intent(v.getContext(), SplashPageActivity.class);
                startActivity(intent);
                Toast.makeText(v.getContext(),"Log Out Successfully",Toast.LENGTH_LONG);
                getActivity().finish();

            }
        });
        return v;
    }
}