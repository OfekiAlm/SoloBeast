package com.example.solobeast.Adapters;
import com.example.solobeast.Objects.Task;
import com.example.solobeast.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    Context context;
    List<Task> taskList;
    public TaskAdapter(Context context, List<Task> taskList){
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_item,null);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        //set holder items to UI;
        //holder."text".setText();
        //...whatever
        holder.taskNameTv.setText("TaskName: "+ task.getName());
        holder.difficultyTv.setText("Difficulty: "+task.getDifficulty());
        holder.timeTv.setText("Time: "+ task.getTime());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // INNER CLASS OF VIEW HOLDER TO OBJECT.
    class TaskViewHolder extends RecyclerView.ViewHolder{
        //SET FIELDS OF TASK UI to
        TextView taskNameTv, difficultyTv,timeTv;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            //find all views by id in task_item layout.
            taskNameTv = itemView.findViewById(R.id.taskName);
            difficultyTv = itemView.findViewById(R.id.diff);
            timeTv = itemView.findViewById(R.id.time);
        }
    }
}

