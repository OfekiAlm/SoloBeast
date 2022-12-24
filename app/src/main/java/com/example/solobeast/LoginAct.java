package com.example.solobeast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAct extends AppCompatActivity {
    EditText EmailEdt,PasswordEdt;
    Button Submit_btn;
    TextView moveToRegister;
    FirebaseAuth mAuth;
    TextInputEditText username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        sign_out();
        init();
    }

    private void sign_out() {
        mAuth.signOut();
    }

    private void init(){
        username = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        //moveToRegister = findViewById(R.id.move_screen);
//
//        moveToRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginAct.this, RegisterAct.class));
//            }
//        });
    }
    private void check_validation_credentials() {
        if(username.getText().toString() == null){
            username.requestFocus();
        }
        if(password.getText().toString() == null){
            password.requestFocus();
        }
    }

    public void login(View view){
        check_validation_credentials();

        Log.d("AuthData","Username: "+ username.getText().toString() +"\n"+"Password: "+ password.getText().toString());
        mAuth.signInWithEmailAndPassword(username.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("AuthData","signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }else{
                    Log.w("AuthData", "signInWithCredential:failure", task.getException());
                }
            }
        });
    }
    private void move_screen(String email){
        Intent i = new Intent(LoginAct.this, MainActivity.class);
        String Identifier  = "username_of_user";
        i.putExtra(email,Identifier);
        startActivity(i);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser currentUser) {

        Log.d("AuthData","Email is " + currentUser.getEmail());
        move_screen(currentUser.getEmail());
    }
}