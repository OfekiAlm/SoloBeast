package com.example.solobeast.Adapters;
import static com.example.solobeast.Objects.Task.*;

import com.example.solobeast.Objects.Task;
import com.example.solobeast.R;
import com.example.solobeast.ui.TaskTimerAct;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 The TaskAdapter class is an adapter for the RecyclerView widget that handles a list of Task objects.
 It provides a view for each Task item in the list, and it binds data from the Task objects to the corresponding views.
 It also listens to user interactions, such as clicking or long pressing an item in the list, and it triggers the appropriate actions.
 @author Ofek Almog
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    /**
     An instance of the RecyclerViewFunctionalities interface that provides callbacks for the adapter's actions.
     */
    private final RecyclerViewFunctionalities recyclerViewFunctionalities;

    /**
     The context of the adapter, used for inflating the item views.
     */
    Context context;

    /**
     The list of Task objects that this adapter is handling.
     */
    List<Task> taskList;

    /**
     Creates a new instance of the TaskAdapter class.
     @param context The context of the adapter.
     @param taskList The list of Task objects to be handled by the adapter.
     @param recyclerViewFunctionalities An instance of the RecyclerViewFunctionalities interface that provides callbacks for the adapter's actions.
     */
    public TaskAdapter(Context context, List<Task> taskList, RecyclerViewFunctionalities recyclerViewFunctionalities){
        this.context = context;
        this.taskList = taskList;
        this.recyclerViewFunctionalities = recyclerViewFunctionalities;
    }

    /**
     Creates a new TaskViewHolder instance by inflating the item view from the specified layout.
     @param parent The parent ViewGroup in which the item view will be contained.
     @param viewType The type of the view to be inflated.
     @return The new RewardViewHolder instance.
     */
    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_item,null);
        return new TaskViewHolder(view, recyclerViewFunctionalities);
    }

    /**
     Binds the data from the Task object at the specified position in the list to the corresponding views in the holder.
     @param holder The RewardViewHolder that holds the views to be bound.
     @param position The position of the Reward object in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.taskNameTv.setText(task.getName());
        holder.taskNameTv.setContentDescription("Task name is " + task.getName());
        holder.difficultyTv.setText(task.getDifficulty());
        holder.constraintLayout.setBackgroundColor(Color.parseColor(changeColorAsTaskDifficulty(task.getDifficulty())));
        holder.timeTv.setText(timeRepresentation(task.getTime()));
        holder.timeTv.setContentDescription(task.getTime());
    }

    /**
     Gets the number of items in the list.
     @return The number of Task objects in the list.
     */
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    /**
     Returns the list of Task objects handled by this adapter.
     @return The list of Task objects.
     */
    public List<Task> getTaskList(){return this.taskList;}

    /**
     The TaskViewHolder class represents a view holder for the RecyclerView items in the TaskAdapter.
     It holds references to the views in the item layout, and it sets listeners for user interactions.
     */
    class TaskViewHolder extends RecyclerView.ViewHolder{

        /**
         The TextViews that displays the properties of the task.
         */
        TextView taskNameTv, difficultyTv,timeTv;

        /**
         The ConstraintLayout field represents the layout of the task item in the TaskAdapter's RecyclerView.
         */
        ConstraintLayout constraintLayout;

        /**
         * Constructs a new TaskViewHolder object.
         * @param itemView The View object corresponding to the task item layout.
         * @param recyclerViewFunctionalities The interface implementing the RecyclerView functionalities.
         */
        public TaskViewHolder(@NonNull View itemView, RecyclerViewFunctionalities recyclerViewFunctionalities) {
            super(itemView);
            //find all views by id in task_item layout.
            taskNameTv = itemView.findViewById(R.id.taskName);
            difficultyTv = itemView.findViewById(R.id.diff);
            timeTv = itemView.findViewById(R.id.time);
            constraintLayout = itemView.findViewById(R.id.constraintLayout2);


            FloatingActionButton startTaskFAB = itemView.findViewById(R.id.fab_start_task);

            //Set listener for start task button.
            startTaskFAB.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                Task t = getTaskList().get(pos);
                moveToTimerScreen(t);
            });

            //Set listener for item click.
            itemView.setOnClickListener(view -> {
                int pos = getBindingAdapterPosition();
                if(checkInterfaceValid(recyclerViewFunctionalities)) recyclerViewFunctionalities.onItemClick(pos);
            });

            //Set listener for item long click.
            itemView.setOnLongClickListener(view -> {
                int pos = getBindingAdapterPosition();
                if(checkInterfaceValid(recyclerViewFunctionalities)) recyclerViewFunctionalities.onItemLongClick(pos);
                return true;
            });
        }

        /**
         * This method is used to move to the task timer screen for a given task.
         * It creates a new intent with the selected task's name, description, time, and difficulty
         * as extras and starts the TaskTimerAct activity with that intent.
         *
         * @param t The task to start the timer for.
         */
        private void moveToTimerScreen(Task t) {
            Intent i = new Intent(context, TaskTimerAct.class);
            i.putExtra("selected_task_name",t.getName());
            i.putExtra("selected_task_desc",t.getDescription());
            i.putExtra("selected_task_time",t.getTime());
            i.putExtra("selected_task_diff",t.getDifficulty());
            context.startActivity(i);
        }

        /**
         Checks if the given RecyclerViewFunctionalities instance is not null and if the current binding adapter position
         is not equal to RecyclerView.NO_POSITION.
         @param recyclerViewFunctionalities an instance of RecyclerViewFunctionalities to be checked
         @return true if the instance is not null and the current binding adapter position is valid, false otherwise
         */
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

