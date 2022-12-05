package com.example.solobeast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
   // private String username = null;
    private TextView DisplayUsername;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
//        Bundle extras = getIntent().getExtras();
//        username = (String) extras.get("username_of_user");
        //String data = getIntent().getExtras().getString("username_of_user","User");
        //DisplayUsername.setText("Hello world " + data);

//        String[] username_split=username.split("@");
//        String username_sofi = username_split[0];
        //DisplayUsername.setText("Hello world " + username);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"your icon was clicked",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void init(){
       //DisplayUsername= findViewById(R.id.display_tv);
    }
}