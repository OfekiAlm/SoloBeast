package com.example.solobeast.ui.Auth;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solobeast.Objects.User;
import com.example.solobeast.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterAct extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextpassword;
    private String TAG = "AuthData";
    private FirebaseAuth mAuth;
    ActivityResultLauncher<Intent> activityResultLauncher;

    FloatingActionButton takePicBtn;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        init();

        takePicBtn.setOnClickListener(view -> {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activityResultLauncher.launch(i);
        });
        takePicFromCamera();
    }

    private void init(){
        editTextEmail = findViewById(R.id.editTextTextEmailAddress_Register);
        editTextpassword = findViewById(R.id.editTextTextPassword_Register);
        circleImageView = findViewById(R.id.profile_image);
        takePicBtn = findViewById(R.id.profile_pic_fab);




        TextView moveToLogin = findViewById(R.id.move_screen);

        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterAct.this, LoginAct.class));
            }
        });
    }
    // TODO: MAKE THIS CODE CLEANER, DUPLICATED CODE IN LoginAct.java
    private boolean check_validation_credentials() {
        if(editTextEmail.getText().length() == 0){
            editTextEmail.requestFocus();
            return false;
        }
        if(editTextpassword.getText().length() ==0){
            editTextpassword.requestFocus();
            return false;
        }
        return true;
    }
    private void takePicFromCamera(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        circleImageView.setImageBitmap(bitmap);
                    }
                    else if(result.getResultCode() == RESULT_CANCELED){
                        Toast.makeText(getBaseContext(),"You CANCELED the picture",Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void register(View view) {
        User my_user = new User(
            "ddd"
        );
        String userEmail = editTextEmail.getText().toString();
        String userPassword = editTextpassword.getText().toString();
        if(check_validation_credentials()){
            mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
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
                                                    Toast.makeText(getApplicationContext(), "YES", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Log.d(TAG, "AddToDatabase:failure");
                                                    Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_LONG).show();
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
    }

    private void updateUI(FirebaseUser user) {

    }

}