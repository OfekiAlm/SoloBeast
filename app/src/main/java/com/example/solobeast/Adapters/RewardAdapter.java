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

/**
 The RewardAdapter class is an adapter for the RecyclerView widget that handles a list of Reward objects.
 It provides a view for each Reward item in the list, and it binds data from the Reward objects to the corresponding views.
 It also listens to user interactions, such as clicking or long pressing an item in the list, and it triggers the appropriate actions.
 @author Ofek Almog
 */
public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder>{

    /**
     An instance of the RecyclerViewFunctionalities interface that provides callbacks for the adapter's actions.
     */
    private final RecyclerViewFunctionalities recyclerViewFunctionalities;

    /**
     The context of the adapter, used for inflating the item views.
     */
    Context context;

    /**
     The list of Reward objects that this adapter is handling.
     */
    List<Reward> rewardList;

    /**
     Creates a new instance of the RewardAdapter class.
     @param context The context of the adapter.
     @param rewardList The list of Reward objects to be handled by the adapter.
     @param recyclerViewFunctionalities An instance of the RecyclerViewFunctionalities interface that provides callbacks for the adapter's actions.
     */
    public RewardAdapter(Context context, List<Reward> rewardList, RecyclerViewFunctionalities recyclerViewFunctionalities){
        this.context = context;
        this.rewardList = rewardList;
        this.recyclerViewFunctionalities = recyclerViewFunctionalities;
    }

    /**
     Creates a new RewardViewHolder instance by inflating the item view from the specified layout.
     @param parent The parent ViewGroup in which the item view will be contained.
     @param viewType The type of the view to be inflated.
     @return The new RewardViewHolder instance.
     */
    @NonNull
    @Override
    public RewardAdapter.RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.reward_item,null);
        return new RewardViewHolder(view, recyclerViewFunctionalities);
    }

    /**
     Binds the data from the Reward object at the specified position in the list to the corresponding views in the holder.
     @param holder The RewardViewHolder that holds the views to be bound.
     @param position The position of the Reward object in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull RewardAdapter.RewardViewHolder holder, int position) {
        Reward r = rewardList.get(position);

        holder.RewardNameTv.setText(r.getRewardName());
        holder.RewardDescTv.setText(r.getDescription());
        holder.xpTv.setText("XP points "+ r.getXP());
    }

    /**
     Gets the number of items in the list.
     @return The number of Reward objects in the list.
     */
    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    /**
     Returns the list of Reward objects handled by this adapter.
     @return The list of Reward objects.
     */
    public List<Reward> getRewardList(){return this.rewardList;}

    /**
     The RewardViewHolder class represents a view holder for the RecyclerView items in the RewardAdapter.
     It holds references to the views in the item layout, and it sets listeners for user interactions.
     */
    class RewardViewHolder extends RecyclerView.ViewHolder{

        /**
         The TextView's that displays the properties of the reward.
         */
        TextView RewardNameTv,RewardDescTv,xpTv;

        /**
         * The alert dialog used to confirm a reward purchase.
         */
        AlertDialog.Builder alertDialog;

        /**
         * Constructs a new RewardViewHolder object.
         * @param itemView The View object corresponding to the reward item layout.
         * @param recyclerViewFunctionalities The interface implementing the RecyclerView functionalities.
         */
        public RewardViewHolder(@NonNull View itemView, RecyclerViewFunctionalities recyclerViewFunctionalities) {
            super(itemView);

            //find all views by id in reward_item layout.
            RewardNameTv = itemView.findViewById(R.id.RewardName);
            RewardDescTv = itemView.findViewById(R.id.desc_item_holder);
            xpTv = itemView.findViewById(R.id.xp_reward_needed);
            alertDialog = new AlertDialog.Builder(context);
            FloatingActionButton startTaskFAB = itemView.findViewById(R.id.fab_buy_reward);

            //Set listener for reward purchase button.
            startTaskFAB.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                Reward r= getRewardList().get(pos);
                moveToPaymentScreen(r);
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
         * Method to confirm a reward purchase and handle the transaction.
         * @param r The Reward object to be purchased.
         */
        private void moveToPaymentScreen(Reward r) {

            alertDialog
                    .setMessage("Are you sure you want to proceed with this reward payment?")
                    .setCancelable(false)
                    .setTitle("Buy a reward");

            alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                dialogInterface.cancel();
                if (r.getXP() <= MainActivity.current_user.getCurrentXP()){
                    Toast.makeText(context,"You just bought the reward. Congrats!",Toast.LENGTH_LONG).show();
                    MainActivity.updateXPtoUserFirebase(r.getXP(),"DecreaseVal");
                }
                else {
                    Toast.makeText(context,"You don't have enough xp. 😢",Toast.LENGTH_LONG).show();

                }


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

        /**
         * Method to check if the RecyclerViewFunctionalities interface is valid and the item position is not RecyclerView.NO_POSITION.
         * @param recyclerViewFunctionalities The interface implementing the RecyclerView functionalities.
         * @return True if the interface is valid and the position is not RecyclerView.NO_POSITION, false otherwise.
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