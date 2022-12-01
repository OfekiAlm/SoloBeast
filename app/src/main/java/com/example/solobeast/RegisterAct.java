package com.example.solobeast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterAct extends AppCompatActivity {
    EditText EmailEdt, PasswordEdt;
    Button SubmitBtn, PicBtn;
    private String TAG = "AuthData";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        init();
    }
    private void init(){
        EmailEdt = findViewById(R.id.editTextTextEmailAddress2);
        PasswordEdt = findViewById(R.id.editTextTextPassword2);
        
        SubmitBtn = findViewById(R.id.submit_form);
        PicBtn = findViewById(R.id.image_button);
    }
//    private void moveToHomepage(){
//        register();
//    }

    public void register(View view) {
        User my_user = new User(
            "ddd"
        );

        mAuth.createUserWithEmailAndPassword(EmailEdt.getText().toString(), PasswordEdt.getText().toString())
                .addOnCompleteListener(RegisterAct.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(my_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "AddToDatabase:success");
                                                Toast.makeText(getApplicationContext(), "YES", Toast.LENGTH_LONG);
                                            } else {
                                                Log.d(TAG, "AddToDatabase:failure");
                                                Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_LONG);
                                            }
                                        }
                                    });

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

    }

}