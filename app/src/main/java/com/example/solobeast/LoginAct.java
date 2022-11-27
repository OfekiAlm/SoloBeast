package com.example.solobeast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private void check_validation_credentials() {
        if(EmailEdt.getText().toString() == null){
            EmailEdt.requestFocus();
        }
        if(PasswordEdt.getText().toString() == null){
            PasswordEdt.requestFocus();
        }
    }

    public void login(View view){
        check_validation_credentials();


        mAuth.signInWithEmailAndPassword(EmailEdt.getText().toString(),PasswordEdt.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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