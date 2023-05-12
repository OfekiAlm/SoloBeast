package com.example.solobeast.ui.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.solobeast.Extras.GuiderDialog;
import com.example.solobeast.Extras.Receivers.AirplaneModeReceiver;
import com.example.solobeast.Objects.User;
import com.example.solobeast.ui.DetailedRewardAct;
import com.example.solobeast.ui.DetailedTaskAct;
import com.example.solobeast.ui.Home.Fragments.HomeFragment;
import com.example.solobeast.ui.Home.Fragments.ProfileFragment;
import com.example.solobeast.R;
import com.example.solobeast.ui.Home.Fragments.RewardFragment;
import com.example.solobeast.databinding.ActivityMainBinding;
import com.example.solobeast.ui.Auth.LoginAct;
import com.example.solobeast.ui.MailContactAct;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FirebaseCallback{
    ActivityMainBinding binding;
    AirplaneModeReceiver airplaneModeReceiver;
    public static User current_user;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FloatingActionButton addBtn;
    FirebaseAuth mAuth;
    TextView xpDisplayTv;

    @Override
    public void onUserDetailsReceived() {
        xpDisplayTv.setText(""+ current_user.getCurrentXP());
    }

    enum Fragments{
        HOME,
        REWARDS,
        PROFILE
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        current_user = new User();
        getUserDetails(this);
        init();

        GuiderDialog guiderDialog = new GuiderDialog(this,"MainActivity","Hello there,\nYou're new here. Let me guide you through the pages of the app, What's behind me is the first and the main screen. You can add tasks at the plus button at the bottom of the screen and also navigate to another screens.");
        guiderDialog.startDialog();
        airplaneModeReceiver = new AirplaneModeReceiver();
//        if(AirplaneModeReceiver.isAirplaneMode){
//
//        }

        addBtn.setOnClickListener(view -> {
            //Fragment f = this.getFragmentManager().findFragmentById();
            if(determineFragment() == Fragments.HOME){
                Intent i = new Intent(getApplicationContext(), DetailedTaskAct.class);
                i.putExtra("from_intent","Add");
                startActivity(i);
            }
            if(determineFragment() == Fragments.REWARDS){
                Intent i = new Intent(getApplicationContext(), DetailedRewardAct.class);
                i.putExtra("from_intent","Add");
                startActivity(i);
            }
            if (determineFragment() == Fragments.PROFILE){
                Toast.makeText(this,"",Toast.LENGTH_LONG).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homescreen:
                    //finish();
                    replaceFragment(new HomeFragment(),Fragments.HOME);
                    addBtn.setVisibility(View.VISIBLE);
                    break;
                case R.id.profile:
                    //finish();
                    replaceFragment(new ProfileFragment(),Fragments.PROFILE);
                    addBtn.setVisibility(View.GONE);
                    break;
                case R.id.reward:
                    //finish();
                    replaceFragment(new RewardFragment(),Fragments.REWARDS);
                    addBtn.setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        });
        navdrawer_init();
    }
    private void init(){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment(),Fragments.HOME);
        binding.bottomNavigationView.setSelectedItemId(R.id.homescreen);
        binding.bottomNavigationView.setBackground(null);
        addBtn = (FloatingActionButton) binding.addButtonFab.findViewById(R.id.add_button_fab);
        xpDisplayTv = findViewById(R.id.xp_main_display);
        xpDisplayTv.setText("XP");
        binding.xpMainDisplay.setContentDescription("You have "+current_user.getCurrentXP()+" xp");
        //xpDisplayTv.setOnLongClickListener(this);
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
                //Intent moveToSettings = new Intent(this, SettingsActivity.class);
                //startActivity(moveToSettings);
                Toast.makeText(this,"Default settings is enabled in the current version",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_onboarding:
                Log.i("MoveScreen", "MoveToOnBoardScreen:Success");
                Toast.makeText(this, "Welcome! every new user gets instruction in every screen ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_contact:
                Log.i("MoveScreen", "MoveToContact  Screen:Success");
                Intent moveToMailContact = new Intent(this, MailContactAct.class);
                startActivity(moveToMailContact);
                break;
            case R.id.nav_donate:
                Log.i("MoveScreen", "MoveToDonateScreen:Success");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/donate/?hosted_button_id=G7LXD4YAGLL5Y"));
                startActivity(browserIntent);
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
    private void replaceFragment(Fragment theInstance, Fragments enumVersion) {
        String onWhichFragment = "";
        int theFrag = enumVersion.ordinal();
        switch (theFrag){
            case 0:
                onWhichFragment = "HOME";
                break;
            case 1:
                onWhichFragment = "REWARDS";
                break;
            case 2:
                onWhichFragment = "PROFILE";
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, theInstance,onWhichFragment);
        fragmentTransaction.commit();
    }

    public Fragments determineFragment(){
        Fragment rewardsFrag = (Fragment)getSupportFragmentManager().findFragmentByTag("REWARDS");
        Fragment profileFrag = (Fragment)getSupportFragmentManager().findFragmentByTag("PROFILE");
        if(rewardsFrag != null && rewardsFrag.isVisible()){
            return Fragments.REWARDS;
        }
        else if(profileFrag != null && profileFrag.isVisible()){
            return Fragments.PROFILE;
        }
        return Fragments.HOME;
    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(airplaneModeReceiver, new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(airplaneModeReceiver);
    }

    public static void getUserDetails(FirebaseCallback callback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        Task<DataSnapshot> task = ref.get();
        task.addOnSuccessListener(dataSnapshot -> {
            User user_fb = dataSnapshot.getValue(User.class);
            current_user.setCurrentXP(user_fb.getCurrentXP());
            current_user.setPhoneNumber(user_fb.getPhoneNumber());
            Log.d("AuthData","got all user details" + current_user.getCurrentXP());
            callback.onUserDetailsReceived();
        }).addOnFailureListener(e -> {
            // Handle any errors here
            Log.d("AuthData","The opreation is not good\nCause: \n" +e);
        });
    }
    /**
     * Updates the current XP value of the current user in Firebase database.
     *
     * @param xpToOperate the amount of XP to add or subtract from the current XP value.
     * @param operation the operation to perform on the current XP value: "IncreaseVal" to add XP or any other string to subtract XP.
     */
    public static void updateXPtoUserFirebase(int xpToOperate, String operation) {
        //set value in firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("currentXP");

        int current = MainActivity.current_user.getCurrentXP();
        int CurrentValue;
        if(operation.equals("IncreaseVal")){
            CurrentValue = current + xpToOperate;
        }
        else {
            CurrentValue = current - xpToOperate;
        }

        ref.setValue(CurrentValue);
        MainActivity.current_user.setCurrentXP(CurrentValue);
    }
}