package com.example.edible;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ManagerFoodsAdapter extends RecyclerView.Adapter<ManagerFoodsAdapter.MangerFoodViewHolder> {

    private Context mContext;
    private List<ManagerFoodsModal> mFoodList;

    public ManagerFoodsAdapter(Context mContext, List<ManagerFoodsModal> mFoodList) {
        this.mContext = mContext;
        this.mFoodList = mFoodList;
    }

    @NonNull
    @Override
    public MangerFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_food_item,parent,false);
        return new MangerFoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MangerFoodViewHolder holder, int position) {

        Glide.with(mContext).load(mFoodList.get(position).getFoodImageUri()).into(holder.managerFoodImageView);
        holder.managerFoodName.setText(mFoodList.get(position).getFoodName());
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    class MangerFoodViewHolder extends RecyclerView.ViewHolder{

        private ImageView managerFoodImageView;
        private TextView managerFoodName;
        private Button managerRemoveFoodBtn;

        public MangerFoodViewHolder(@NonNull View itemView) {
            super(itemView);

            managerFoodImageView = itemView.findViewById(R.id.order_food_imageview);
            managerFoodName = itemView.findViewById(R.id.order_food_name);
            managerRemoveFoodBtn = itemView.findViewById(R.id.order_remove_food_btn);
        }
    }
}
