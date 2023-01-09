package com.example.solobeast.ui.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.example.solobeast.ui.DetailedTaskAct;
import com.example.solobeast.ui.Home.Fragments.HomeFragment;
import com.example.solobeast.Objects.Task;
import com.example.solobeast.ui.Home.Fragments.ProfileFragment;
import com.example.solobeast.R;
import com.example.solobeast.ui.Home.Fragments.RewardFragment;
import com.example.solobeast.databinding.ActivityMainBinding;
import com.example.solobeast.ui.Auth.LoginAct;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FloatingActionButton addBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setSelectedItemId(R.id.homescreen);
        binding.bottomNavigationView.setBackground(null);
        addBtn = (FloatingActionButton) binding.addButtonFab.findViewById(R.id.add_button_fab);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DetailedTaskAct.class);
                i.putExtra("from_intent","Add");
                startActivity(i);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homescreen:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.reward:
                    replaceFragment(new RewardFragment());
                    break;
            }
            return true;
        });


        init();
        navdrawer_init();



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
                sign_out();
                startActivity(new Intent(this, LoginAct.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public void sign_out() {
        mAuth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(this,LoginAct.class));
        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}