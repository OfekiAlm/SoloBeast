package com.example.solobeast.Adapters;
import com.example.solobeast.Objects.Task;
import com.example.solobeast.R;
import com.example.solobeast.ui.TaskTimerAct;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    private final RecyclerViewFunctionalities recyclerViewFunctionalities;
    Context context;
    List<Task> taskList;
    public TaskAdapter(Context context, List<Task> taskList, RecyclerViewFunctionalities recyclerViewFunctionalities){
        this.context = context;
        this.taskList = taskList;
        this.recyclerViewFunctionalities = recyclerViewFunctionalities;
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_item,null);
        return new TaskViewHolder(view, recyclerViewFunctionalities);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        //set holder items to UI;
        //holder."text".setText();
        //...whatever
        holder.taskNameTv.setText("TaskName:\n\n"+ task.getName());
        holder.difficultyTv.setText("Difficulty:\n\n"+task.getDifficulty());
        holder.timeTv.setText("Time:\n\n"+ task.getTime());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
    public List<Task> getTaskList(){return this.taskList;}
    // INNER CLASS OF VIEW HOLDER TO OBJECT.
    class TaskViewHolder extends RecyclerView.ViewHolder{
        //SET FIELDS OF TASK UI to
        TextView taskNameTv, difficultyTv,timeTv;
        public TaskViewHolder(@NonNull View itemView, RecyclerViewFunctionalities recyclerViewFunctionalities) {
            super(itemView);
            //find all views by id in task_item layout.
            taskNameTv = itemView.findViewById(R.id.taskName);
            difficultyTv = itemView.findViewById(R.id.diff);
            timeTv = itemView.findViewById(R.id.time);

            FloatingActionButton startTaskFAB = itemView.findViewById(R.id.fab_start_task);
            startTaskFAB.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    Task t = getTaskList().get(pos);
                    moveToTimerScreen(t);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getBindingAdapterPosition();
                    if(checkInterfaceValid(recyclerViewFunctionalities)) recyclerViewFunctionalities.onItemClick(pos);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = getBindingAdapterPosition();
                    if(checkInterfaceValid(recyclerViewFunctionalities)) recyclerViewFunctionalities.onItemLongClick(pos);
                    return true;
                }
            });
        }

        private void moveToTimerScreen(Task t) {
            Intent i = new Intent(context, TaskTimerAct.class);
            i.putExtra("selected_task_name",t.getName());
            i.putExtra("selected_task_desc",t.getDescription());
            i.putExtra("selected_task_time",t.getTime());
            i.putExtra("selected_task_diff",t.getDifficulty());
            context.startActivity(i);
        }

        private boolean checkInterfaceValid(RecyclerViewFunctionalities recyclerViewFunctionalities){
            if(recyclerViewFunctionalities != null){
                int pos = getBindingAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    return true;
                }
            }
            return false;
        }
    }
}

