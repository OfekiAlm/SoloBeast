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

/**
 This class represents a Fragment for displaying a list of rewards. It implements the RecyclerViewFunctionalities interface
 to handle item click and long click events. It uses a RecyclerView to display the rewards list and a RewardAdapter to populate
 the RecyclerView with reward items. It also uses Firebase Realtime Database to retrieve and manipulate data related to rewards.
 @author Ofek Almog
 */
public class RewardFragment extends Fragment implements RecyclerViewFunctionalities {

    /**
     A list of rewards to be displayed in the RecyclerView.
     */
    List<Reward> rewardsList;

    /**
     An adapter for the RecyclerView.
     */
    RewardAdapter adapter;

    /**
     The RecyclerView for displaying the rewards list.
     */
    RecyclerView recyclerView;

    /**
     A reference to the Firebase Realtime Database.
     */
    DatabaseReference myRef;

    /**
     An instance of FirebaseAuth to retrieve information about the currently logged in user.
     */
    FirebaseAuth mAuth;

    /**
     Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     Initializes the RecyclerView and retrieves the user's rewards from the database.
     @param view The View returned by onCreateView.
     @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.reward_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        mAuth = FirebaseAuth.getInstance();

        // Set the reference to the Firebase Realtime Database
        myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://solobeast-android-default-rtdb.firebaseio.com/");
        myRef = myRef.child("Users/"
                + FirebaseAuth.getInstance().getCurrentUser().getUid()
                +"/Rewards");

        this.retrieveData(this);

    }

    /**
     Retrieves data from Firebase Realtime Database and populates the rewards list.
     @param recyclerViewFunctionalities An instance of RecyclerViewFunctionalities to handle RecyclerView click events.
     */
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

    /**
     Called to have the fragment instantiate its user interface view.
     Inflates the layout for this fragment and returns the inflated View object.
     @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reward, container, false);
    }

    /**
     Navigates to the DetailedRewardAct activity with the details of the selected reward.
     @param reward The selected reward to display its details.
     */
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

    /**
     This method is called when an item in the RecyclerView is clicked. It creates a new Reward object using the data
     from the clicked item and passes it to the {@link #moveToDetailedScreen(Reward)} method to start a new activity to display
     the details of the selected reward.
     @param position the position of the clicked item in the RecyclerView
    */
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

    /**
     Called when a reward item in the list is long clicked. Prompts the user to confirm if they want to delete
     the selected reward item. If the user confirms the deletion, the reward is removed from the database and the
     corresponding item is removed from the rewards list displayed on the screen.
     @param position The position of the reward item that was long clicked in the rewards list.
     @return Returns true to indicate that the long click event has been consumed and should not be further handled.
     */
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