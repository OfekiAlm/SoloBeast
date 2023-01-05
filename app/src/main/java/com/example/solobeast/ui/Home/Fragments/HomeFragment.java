package com.example.solobeast.ui.Home.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.solobeast.Adapters.RecyclerViewFunctionalities;
import com.example.solobeast.Adapters.TaskAdapter;
import com.example.solobeast.Objects.Task;
import com.example.solobeast.R;
import com.example.solobeast.ui.DetailedTaskAct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewFunctionalities {
    List<Task> tasksList;
    TaskAdapter adapter;
    RecyclerView recyclerView;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        tasksList = new ArrayList<Task>();
        Task t1 = new Task("Run","00:10","lkjb","HARD");
        t1.setTime(t1.timeRepresentation());
        tasksList.add(t1);
        Task t2 = new Task("Learn Math","01:20","asdasd","HARD");
        t2.setTime(t2.timeRepresentation());
        tasksList.add(t2);
        Task t3 = new Task("Do app dev","24:00","ddd","HARD");
        t3.setTime(t3.timeRepresentation());
        tasksList.add(t3);
        Task t4 = new Task("Overthink","30:10","ddda","HARD");
        t4.setTime(t4.timeRepresentation());
        tasksList.add(t4);


        adapter = new TaskAdapter(getContext(),tasksList,this);
        recyclerView.setAdapter(adapter);


        FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Tasks").setValue(tasksList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("AuthData", "CreateTreeTask:success");
                            Toast.makeText(getContext(), "YES", Toast.LENGTH_LONG);
                        } else {
                            Log.d("AuthData", "CreateTreeTask:failure");
                            Toast.makeText(getContext(), "NO", Toast.LENGTH_LONG);
                        }
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
        i.putExtra("selected_task_desc",task.getDesc());
        i.putExtra("selected_task_diff",task.getDifficulty());
        startActivity(i);
    }

    @Override
    public void onItemClick(int position) {
        Task t = new Task(
                tasksList.get(position).getName(),
                tasksList.get(position).getTime(),
                tasksList.get(position).getDesc(),
                tasksList.get(position).getDifficulty()
        );
        moveToDetailedScreen(t);
    }

    @Override
    public void onItemLongClick(int position) {
        tasksList.remove(position);
        adapter.notifyItemRemoved(position);

        //REMOVE FROM DB.
    }
}