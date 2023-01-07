package com.example.solobeast.ui.Auth;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.solobeast.ui.Home.MainActivity;
import com.example.solobeast.R;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.material.textfield.TextInputEditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class LoginAct extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextInputEditText editTextEmail, editTextpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        init();
    }



    private void init(){
        editTextEmail = (TextInputEditText) findViewById(R.id.editTextTextEmailAddress_Register);
        editTextpassword = (TextInputEditText) findViewById(R.id.editTextTextPassword_Register);
        TextView moveToRegister = findViewById(R.id.move_screen);

        moveToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAct.this, RegisterAct.class));
            }
        });
    }

    // TODO: MAKE THIS CODE CLEANER, DUPLICATED CODE IN LoginAct.java
    private boolean check_validation_credentials() {
        if(editTextEmail.getText().length() ==0){
            editTextEmail.requestFocus();
            return false;
        }
        if(editTextpassword.getText().length() ==0){
            editTextpassword.requestFocus();
            return false;
        }
        return true;
    }

    public void login(View view){
        boolean validation_credentials_are_valid= check_validation_credentials();
        if(validation_credentials_are_valid) {
            Log.d("AuthData", "Username: " + editTextEmail.getText().toString() + "\n" + "Password: " + editTextpassword.getText().toString());
            try {
                mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("AuthData", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w("AuthData", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), task.getResult().toString(), Toast.LENGTH_LONG);
                        }
                    }
                });
            } catch (RuntimeExecutionException e) {
                if (e.getCause() instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(getApplicationContext(),"Auth falied try to sign in again please",Toast.LENGTH_LONG);
                }
            }

        }
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