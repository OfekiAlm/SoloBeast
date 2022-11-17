package com.example.solobeast;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginAct extends AppCompatActivity {
    EditText EmailEdt,PasswordEdt;
    Button Submit_btn;
    TextView moveToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        EmailEdt = findViewById(R.id.editTextTextEmailAddress);
        PasswordEdt = findViewById(R.id.editTextTextPassword);
        moveToRegister = findViewById(R.id.move_screen);
        moveToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAct.this, RegisterAct.class));
            }
        });
    }
    private void check_credentials(){
        check_validation_credentials();
    }

    private void check_validation_credentials() {
    }

    private void login(View view){
        check_credentials();

        move_screen();
    }
    private void move_screen(){

    }
}