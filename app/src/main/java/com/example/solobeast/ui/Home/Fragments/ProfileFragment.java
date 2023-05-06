package com.example.solobeast.ui.Home.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solobeast.Objects.User;
import com.example.solobeast.R;
import com.example.solobeast.ui.Auth.RegisterAct;
import com.example.solobeast.ui.Home.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    Bitmap imageBitmap;
    CircleImageView circleImageView;
    String userEmail;
    TextView userEmailTv, userPhoneTv;
    static String userPhoneNumber;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserDetails();
        init(view);

    }

    private void init(View view){
        circleImageView = view.findViewById(R.id.profile_circle_image);
        getImageFromFireBase();

        userEmail = getEmail();
        userEmailTv = view.findViewById(R.id.task_name_et);
        userEmailTv.setText(userEmail);

        userPhoneTv = view.findViewById(R.id.phone_textview_profile_from_fb);
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
            //Toast.makeText(getActivity(), "successfully loaded your profile credentials", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(exception -> Toast.makeText(getContext(), "fail", Toast.LENGTH_LONG).show());
    }
    public String getEmail(){
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }
    public void getUserDetails(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("phoneNumber");
        Task<DataSnapshot> task = ref.get();
        task.addOnSuccessListener(dataSnapshot -> {
            String str_p_num = dataSnapshot.getValue(String.class);
            userPhoneNumber = str_p_num;
            userPhoneTv.setText(userPhoneNumber);
            Log.d("AuthData","lol that's worked actually");
        }).addOnFailureListener(e -> {
            // Handle any errors here
            Log.d("AuthData","The opreation is not good\nCause: \n" +e);
        });

    }
}