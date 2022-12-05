package com.example.solobeast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   // private String username = null;
    private TextView DisplayUsername;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        navdrawer_init();
//        Bundle extras = getIntent().getExtras();
//        username = (String) extras.get("username_of_user");
        //String data = getIntent().getExtras().getString("username_of_user","User");
        //DisplayUsername.setText("Hello world " + data);

//        String[] username_split=username.split("@");
//        String username_sofi = username_split[0];
        //DisplayUsername.setText("Hello world " + username);




    }
    private void init(){
       //DisplayUsername= findViewById(R.id.display_tv);
    }
    private void navdrawer_init(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.action_nav_open, R.string.action_nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_settings:
                Log.i("MoveScreen", "MoveToSettingsScreen:Success");
                break;
            case R.id.nav_onboarding:
                Log.i("MoveScreen", "MoveToOnBoardScreen:Success");
                break;
            case R.id.nav_contact:
                Log.i("MoveScreen", "MoveToContact  Screen:Success");
                break;
            case R.id.nav_donate:
                Log.i("MoveScreen", "MoveToDonateScreen:Success");
                break;
            case R.id.nav_auth:
                Log.i("AuthData", "LogOut:Success");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}