package com.example.solobeast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterAct extends AppCompatActivity {
    EditText EmailEdt, PasswordEdt;
    Button SubmitBtn, PicBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
    private void init(){
        EmailEdt = findViewById(R.id.editTextTextEmailAddress);
        PasswordEdt = findViewById(R.id.editTextTextPassword);
        
        SubmitBtn = findViewById(R.id.submit_form);
        PicBtn = findViewById(R.id.image_button);
    }
    private void moveToHomepage(){
        register();
    }

    private void register() {
    }

}