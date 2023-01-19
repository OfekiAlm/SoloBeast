package com.example.solobeast.ui.Home.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.solobeast.R;
import com.example.solobeast.ui.Auth.RegisterAct;
import com.example.solobeast.ui.Home.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    Bitmap imageBitmap;
    CircleImageView circleImageView;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        getImageFromFireBase();
    }

    private void init(View view){
        circleImageView = view.findViewById(R.id.profile_circle_image);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void getImageFromFireBase(){
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference().child("profile-images/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            imageBitmap  = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if(imageBitmap !=null)
                circleImageView.setImageBitmap(imageBitmap);
            Toast.makeText(getContext(), "success", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(exception -> Toast.makeText(getContext(), "fail", Toast.LENGTH_LONG).show());
    }
}