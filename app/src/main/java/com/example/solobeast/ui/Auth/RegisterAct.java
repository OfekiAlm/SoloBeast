package com.example.solobeast.ui.Auth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solobeast.Objects.User;
import com.example.solobeast.R;
import com.example.solobeast.ui.Home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterAct extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextpassword, editTextPhoneNum;
    private String TAG = "AuthData";
    private FirebaseAuth mAuth;
    ActivityResultLauncher<Intent> activityResultLauncher;
    StorageReference storageReference;
    FloatingActionButton takePicBtn;
    CircleImageView circleImageView;
    Bitmap imageBitmap;
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        init();
        RegisterOnResult_and_takePicFromCamera();
        takePicBtn.setOnClickListener(view -> {

            alertDialog
                    .setMessage("Choose your way to provide image")
                    .setTitle("Provide Image");

            alertDialog.setPositiveButton("Camera", (dialogInterface, i) -> {
                dialogInterface.cancel();
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(camera);
            });
            alertDialog.setNegativeButton("Storage", (dialogInterface, i) -> {
                dialogInterface.cancel();
                Intent storage = new Intent(Intent.ACTION_PICK);
                storage.setType("image/*");

                activityResultLauncher.launch(storage);
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        });

    }

    private void init(){
        editTextEmail = findViewById(R.id.editTextTextEmailAddress_Register);
        editTextpassword = findViewById(R.id.editTextTextPassword_Register);
        circleImageView = findViewById(R.id.profile_image);
        takePicBtn = findViewById(R.id.profile_pic_fab);
        editTextPhoneNum = findViewById(R.id.editTextTextPhoneNum_Register);

        TextView moveToLogin = findViewById(R.id.move_screen);
        alertDialog = new AlertDialog.Builder(this);
        moveToLogin.setOnClickListener(view -> startActivity(new Intent(RegisterAct.this, LoginAct.class)));
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
    private void RegisterOnResult_and_takePicFromCamera(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Bitmap bitmapConvertable;
                    System.out.println(result);
                    System.out.println(result.getData());
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent bitmap = result.getData();
                        Uri uri = bitmap.getData();
                        try {
                            bitmapConvertable = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        circleImageView.setImageBitmap(bitmapConvertable);
                        imageBitmap = bitmapConvertable;
                    }
                    else if(result.getResultCode() == RESULT_CANCELED){
                        Toast.makeText(getBaseContext(),"You canceled the process",Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void register(View view) {
        String userPhone = editTextPhoneNum.getText().toString();
        User my_user = new User(
            userPhone
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
                                        .setValue(my_user).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.d(TAG, "AddToDatabase:success");
                                                Toast.makeText(getApplicationContext(), "YES", Toast.LENGTH_LONG).show();
                                            } else {
                                                Log.d(TAG, "AddToDatabase:failure");
                                                Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                uploadImageToFirebaseStorage(imageBitmap);

                                updateUI();
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
    private void uploadImageToFirebaseStorage(Bitmap bitmap){
            storageReference = FirebaseStorage.getInstance().getReference();
            //CONVERTS TO PNG FILE\\
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 5, stream);
            byte[] data = stream.toByteArray();
            //\\
            StorageReference imageRef = storageReference.child("profile-images/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
            Task<Uri> urlTask = imageRef.putBytes(data).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                if (task.isSuccessful()) {
                    Toast.makeText(this,"Image uploaded to database",Toast.LENGTH_LONG).show();
                } else {
                    // Handle failures
                    // ...
                }
                return imageRef.getDownloadUrl();
            });
    }
    private void updateUI() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}