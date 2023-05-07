package com.example.solobeast.ui.Home.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.solobeast.Adapters.RecyclerViewFunctionalities;
import com.example.solobeast.Adapters.RewardAdapter;
import com.example.solobeast.Objects.Reward;
import com.example.solobeast.R;
import com.example.solobeast.ui.DetailedRewardAct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RewardFragment extends Fragment implements RecyclerViewFunctionalities {
    List<Reward> rewardsList;
    RewardAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference myRef;
    FirebaseAuth mAuth;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.reward_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        mAuth = FirebaseAuth.getInstance();
        //REFERENCE\\
        myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://solobeast-android-default-rtdb.firebaseio.com/");
        myRef = myRef.child("Users/"
                + FirebaseAuth.getInstance().getCurrentUser().getUid()
                +"/Rewards");

        this.retrieveData(this);

    }

    private void retrieveData(RecyclerViewFunctionalities recyclerViewFunctionalities) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rewardsList = new ArrayList<Reward>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Reward r = data.getValue(Reward.class);
                    rewardsList.add(r);
                }
                adapter = new RewardAdapter(getContext(),rewardsList,recyclerViewFunctionalities);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("AuthData","retrieveData - on reward is cancelled");
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reward, container, false);
    }
    private void moveToDetailedScreen(Reward reward){
        Log.d("MoveScreen","Moving to detailed screen");
        Intent i = new Intent(getActivity(), DetailedRewardAct.class);
        Log.d("ObjectValues",reward.toString());
        i.putExtra("selected_reward_name",reward.getRewardName());
        i.putExtra("selected_reward_desc",reward.getDescription());
        i.putExtra("selected_reward_xp",reward.getXP());
        i.putExtra("selected_reward_key",reward.getKey());
        i.putExtra("from_intent","Edit");
        startActivity(i);
    }
    @Override
    public void onItemClick(int position) {
        Reward t = new Reward(
                rewardsList.get(position).getRewardName(),
                rewardsList.get(position).getDescription(),
                rewardsList.get(position).getXP(),
                rewardsList.get(position).getKey()
        );
        moveToDetailedScreen(t);
    }

    @Override
    public boolean onItemLongClick(int position) {
        AlertDialog.Builder alertDialog;
        alertDialog = new AlertDialog.Builder(getContext());

        alertDialog
                .setMessage("Are you sure you want to delete this reward?")
                .setTitle("Delete Reward");

        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(
                    "Users/"
                            + FirebaseAuth.getInstance().getCurrentUser().getUid()
                            +"/Rewards"
            );
            myRef = myRef.child(rewardsList.get(position).getKey());
            myRef.removeValue();
            adapter.notifyItemRemoved(position);

        });
        alertDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.cancel();
            Toast.makeText(getContext(),"Event was cancelled successfully",Toast.LENGTH_LONG).show();
        });
        AlertDialog alert = alertDialog.create();
        alert.show();

        return true;
        //REMOVE FROM DB.
    }
}