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

public class HomeFragment extends Fragment implements RecyclerViewFunctionalities {
    List<Task> tasksList;
    TaskAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        mAuth = FirebaseAuth.getInstance();
        //REFERENCE\\
        myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://solobeast-android-default-rtdb.firebaseio.com/");
        myRef = myRef.child("Users/"
                + FirebaseAuth.getInstance().getCurrentUser().getUid()
                +"/Tasks");

        //\\
        this.retrieveData(this);
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }




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
    private void updateTask(){

    }

}