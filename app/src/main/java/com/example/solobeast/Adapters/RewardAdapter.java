package com.example.solobeast.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solobeast.Objects.Reward;
import com.example.solobeast.R;

import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder>{

    Context context;
    List<Reward> rewardList;
    public RewardAdapter(Context context, List<Reward> taskArrayList){
        this.context = context;
        this.rewardList = taskArrayList;
    }

    @NonNull
    @Override
    public RewardAdapter.RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reward_item,null);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardAdapter.RewardViewHolder holder, int position) {
        Reward reward = rewardList.get(position);
        //set holder items to UI;
        //holder."text".setText();
        //...whatever
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    // INNER CLASS OF VIEW HOLDER TO OBJECT.
    class RewardViewHolder extends RecyclerView.ViewHolder{
        //SET FIELDS OF TASK UI to
        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            //find all views by id in task_item layout.
        }
    }
}