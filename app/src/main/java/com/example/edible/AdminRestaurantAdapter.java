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

public class AdminRestaurantAdapter extends RecyclerView.Adapter<AdminRestaurantAdapter.AdminRestaurantViewHolder> {

    private Context mContext;
    private List<AdminRestaurantsModal> adminRestaurantsModalList;


    public AdminRestaurantAdapter(Context mContext, List<AdminRestaurantsModal> adminRestaurantsModalList) {
        this.mContext = mContext;
        this.adminRestaurantsModalList = adminRestaurantsModalList;
    }

    @NonNull
    @Override
    public AdminRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item,parent,false);
        return new AdminRestaurantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRestaurantViewHolder holder, int position) {
        holder.adminRestaurantName.setText(adminRestaurantsModalList.get(position).getRestaurantName());
        String imageUrl = adminRestaurantsModalList.get(position).getAdminRestaurantImageUrl();
        holder.setImage(imageUrl);

    }

    @Override
    public int getItemCount() {
        return adminRestaurantsModalList.size();
    }

    class AdminRestaurantViewHolder extends RecyclerView.ViewHolder{
        private ImageView adminRestaurantImage;
        private TextView adminRestaurantName;
        private Button adminRemoveRestaurantButton;
        public AdminRestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            adminRestaurantImage = itemView.findViewById(R.id.order_food_imageview);
            adminRestaurantName = itemView.findViewById(R.id.order_food_name);
            adminRemoveRestaurantButton = itemView.findViewById(R.id.order_remove_food_btn);
        }

        public void setImage(String downloadUri){
            Glide.with(mContext).load(downloadUri).into(adminRestaurantImage) ;
        }
    }
}
