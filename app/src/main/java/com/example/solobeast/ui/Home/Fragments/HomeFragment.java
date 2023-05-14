package com.example.solobeast.ui.Home.Fragments;

import android.app.Activity;
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
import com.example.solobeast.Adapters.TaskAdapter;
import com.example.solobeast.Objects.Task;
import com.example.solobeast.R;
import com.example.solobeast.ui.DetailedTaskAct;
import com.example.solobeast.ui.Home.MainActivity;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 A fragment for displaying and managing the user's task list. The 'HomePage'
 Implements the RecyclerViewFunctionalities interface to handle RecyclerView actions.
 @author Ofek Almog
 */
public class HomeFragment extends Fragment implements RecyclerViewFunctionalities {

    /** List of tasks */
    List<Task> tasksList;

    /** Adapter for the RecyclerView */
    TaskAdapter adapter;

    /** RecyclerView for displaying the tasks */
    RecyclerView recyclerView;

    /** Reference to the Firebase Realtime Database */
    DatabaseReference myRef;

    /** Firebase Authentication instance */
    FirebaseAuth mAuth;

    /**
     Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     Initializes the RecyclerView and retrieves the user's tasks from the database.
     @param view The View returned by onCreateView.
     @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        mAuth = FirebaseAuth.getInstance();

        // Set the reference to the Firebase Realtime Database
        myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://solobeast-android-default-rtdb.firebaseio.com/");
        myRef = myRef.child("Users/"
                + FirebaseAuth.getInstance().getCurrentUser().getUid()
                +"/Tasks");

        //\\
        this.retrieveData(this);
    }

    /**
     Retrieves the user's tasks from the Firebase Realtime Database and updates the RecyclerView.
     @param recyclerViewFunctionalities An instance of the RecyclerViewFunctionalities interface.
     */
    private void retrieveData(RecyclerViewFunctionalities recyclerViewFunctionalities) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasksList = new ArrayList<Task>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Task t = data.getValue(Task.class);
                    tasksList.add(t);
                }
                adapter = new TaskAdapter(getContext(),tasksList,recyclerViewFunctionalities);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     Method to move to the detailed screen of a selected task.
     @param task The task to be shown in the detailed screen.
     */
    private void moveToDetailedScreen(Task task){
        Log.d("MoveScreen","Moving to detailed screen");
        Intent i = new Intent(getActivity(), DetailedTaskAct.class);
        Log.d("ObjectValues",task.toString());
        i.putExtra("selected_task_name",task.getName());
        i.putExtra("selected_task_time",task.getTime());
        i.putExtra("selected_task_desc",task.getDescription());
        i.putExtra("selected_task_diff",task.getDifficulty());
        i.putExtra("selected_task_key",task.getKey());
        i.putExtra("from_intent","Edit");
        startActivity(i);
    }

    /**
     Handles the action when an item in the RecyclerView is clicked.
     Starts the DetailedTaskAct activity with the information of the selected task.
     @param position The position of the selected item in the RecyclerView.
     */
    @Override
    public void onItemClick(int position) {
        Task t = new Task(
                tasksList.get(position).getName(),
                tasksList.get(position).getTime(),
                tasksList.get(position).getDescription(),
                tasksList.get(position).getDifficulty(),
                tasksList.get(position).getKey()
        );
        moveToDetailedScreen(t);
    }

    /**
     Handles the long-click event of an item in the RecyclerView list.
     Shows an AlertDialog with a confirmation message for task deletion, and deletes the task from the database
     and updates the adapter on "Yes" button click.
     @param position the position of the item in the RecyclerView list.
     @return true if the event was consumed, false otherwise.
     */
    @Override
    public boolean onItemLongClick(int position) {
        AlertDialog.Builder alertDialog;
        alertDialog = new AlertDialog.Builder(getContext());

        alertDialog
                .setMessage("Are you sure you want to delete this task?")
                .setTitle("Delete Task");

        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(
                    "Users/"
                            + FirebaseAuth.getInstance().getCurrentUser().getUid()
                            +"/Tasks"
            );
            myRef = myRef.child(tasksList.get(position).getKey());
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