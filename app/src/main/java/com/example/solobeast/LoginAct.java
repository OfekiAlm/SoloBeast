package com.example.solobeast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginAct extends AppCompatActivity {
    EditText EmailEdt,PasswordEdt;
    Button Submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    private void init(){
        EmailEdt = findViewById(R.id.editTextTextEmailAddress);
        PasswordEdt = findViewById(R.id.editTextTextPassword);
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