package com.example.solobeast.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solobeast.Objects.Reward;
import com.example.solobeast.R;
import com.example.solobeast.ui.Home.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder>{
    private final RecyclerViewFunctionalities recyclerViewFunctionalities;
    Context context;
    List<Reward> rewardList;
    public RewardAdapter(Context context, List<Reward> rewardList, RecyclerViewFunctionalities recyclerViewFunctionalities){
        this.context = context;
        this.rewardList = rewardList;
        this.recyclerViewFunctionalities = recyclerViewFunctionalities;
    }

    @NonNull
    @Override
    public RewardAdapter.RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // TODO: change layout;
        View view = inflater.inflate(R.layout.reward_item,null);
        return new RewardViewHolder(view, recyclerViewFunctionalities);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardAdapter.RewardViewHolder holder, int position) {
        Reward r = rewardList.get(position);


//        set holder items to UI;
//        holder."text".setText();
//        ...whatever
        holder.RewardNameTv.setText("RewardName: "+ r.getRewardName());
        holder.RewardDescTv.setText("RewardDescription: "+ r.getDescription());
        holder.xpTv.setText("xp: "+ r.getXP());
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }
    public List<Reward> getRewardList(){return this.rewardList;}
    // INNER CLASS OF VIEW HOLDER TO OBJECT.
    class RewardViewHolder extends RecyclerView.ViewHolder{
        //SET FIELDS OF TASK UI to
        TextView RewardNameTv,RewardDescTv,xpTv;
        AlertDialog.Builder alertDialog;
        public RewardViewHolder(@NonNull View itemView, RecyclerViewFunctionalities recyclerViewFunctionalities) {
            super(itemView);
            //find all views by id in task_item layout.
            RewardNameTv = itemView.findViewById(R.id.RewardName);
            RewardDescTv = itemView.findViewById(R.id.desc_item_holder);
            xpTv = itemView.findViewById(R.id.xp_reward_needed);
            alertDialog = new AlertDialog.Builder(context);
            FloatingActionButton startTaskFAB = itemView.findViewById(R.id.fab_buy_reward);
            startTaskFAB.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    Reward r= getRewardList().get(pos);
                    moveToPaymentScreen(r);
                }
            });

            itemView.setOnClickListener(view -> {
                int pos = getBindingAdapterPosition();
                if(checkInterfaceValid(recyclerViewFunctionalities)) recyclerViewFunctionalities.onItemClick(pos);
            });
            itemView.setOnLongClickListener(view -> {
                int pos = getBindingAdapterPosition();
                if(checkInterfaceValid(recyclerViewFunctionalities)) recyclerViewFunctionalities.onItemLongClick(pos);
                return true;
            });
        }

        private void moveToPaymentScreen(Reward r) {
            //Toast.makeText(context,"lol",Toast.LENGTH_LONG).show();
//            Intent i = new Intent(context, TaskTimerAct.class);
//            i.putExtra("selected_task_name",t.getName());
//            i.putExtra("selected_task_desc",t.getDescription());
//            i.putExtra("selected_task_time",t.getTime());
//            i.putExtra("selected_task_diff",t.getDifficulty());
//            context.startActivity(i);

            alertDialog
                    .setMessage("Are you sure you want to proceed with this reward payment?")
                    .setTitle("Buy a reward");

            alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                dialogInterface.cancel();
                Toast.makeText(context,"You just bought the reward. Congrats!",Toast.LENGTH_LONG).show();
                MainActivity.updateXPtoUserFirebase(r.getXP(),"DecreaseVal");

                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            });
            alertDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
                dialogInterface.cancel();
                Toast.makeText(context,"Event was cancelled successfully",Toast.LENGTH_LONG).show();
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
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