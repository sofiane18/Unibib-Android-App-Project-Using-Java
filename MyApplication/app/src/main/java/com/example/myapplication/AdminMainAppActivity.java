package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainAppActivity extends AppCompatActivity implements ConfirmationDialogDeleteBook.ConfirmationDialogDeleteBookListener{
    private BottomNavigationView bottomNavigationBar;
    private View decor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        decor = getWindow().getDecorView();
        decor.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                  //  decor.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        setContentView(R.layout.admin_main_app_activity);
        BottomNavigationView bottomNavigationBar = findViewById(R.id.bottomNavigationBar);
        bottomNavigationBar.setSelectedItemId(R.id.nav_books);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                    new AdminMainAppBooksFragment()).commit();
        }
        bottomNavigationBar.setOnItemSelectedListener(navListener);
    }
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        switch(item.getItemId()){
            case R.id.nav_profile: {
                selectedFragment = new AdminMainAppProfileFragment();
            }
            break;
            case R.id.nav_demands:{
                selectedFragment = new AdminMainAppDemandsFragment();
            }break;
            case R.id.nav_borrowings:{
                selectedFragment = new AdminMainAppBorrowingsFragment();
            }
            break;
            case R.id.nav_bookshelves:{
                selectedFragment = new AdminMainAppShelvesFragment();
            }
            break;
            case R.id.nav_books:{
                selectedFragment = new AdminMainAppBooksFragment();
            }
            break;
            default: selectedFragment = new AdminMainAppBooksFragment();
            break;
        }
        int commit = getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                selectedFragment).commit();
        return true;
    };
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
           // decor.setSystemUiVisibility(hideSystemBars());
        }
    }
    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
    }
    @Override
    public void onDeleteBookYesClicked() {
        AdminMainAppBooksFragment.adminBooksRecyclerViewAdapter.onDeleteBookYesClicked();
    }
}