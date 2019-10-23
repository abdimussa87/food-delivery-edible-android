package com.example.edible;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MainActivityViewHolder> {

    Context mContext;
    private List<MainActivityModal> mainActivityModalList;

    public MainActivityAdapter(Context mContext, List<MainActivityModal> mainActivityModalList) {
        this.mContext = mContext;
        this.mainActivityModalList = mainActivityModalList;
    }

    @NonNull
    @Override
    public MainActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_restaurant_item,parent,false);
        return new MainActivityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityViewHolder holder, int position) {
        Glide.with(mContext).load(mainActivityModalList.get(position).getAdminRestaurantImageUrl()).into(holder.mainActivityRestaurantImageView);
        holder.mainActivityLocatedAtTv.setText(mainActivityModalList.get(position).getRestaurantLocation());
        holder.mainActivityRestaurantDescriptionTv.setText(mainActivityModalList.get(position).getRestaurantDescription());
        holder.mainActivityRestaurantNameTv.setText(mainActivityModalList.get(position).getRestaurantName());

        final String docID = mainActivityModalList.get(position).docId;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,UserFoodActivity.class);
                intent.putExtra("docId",docID);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainActivityModalList.size();
    }

    class MainActivityViewHolder extends RecyclerView.ViewHolder{
        private ImageView mainActivityRestaurantImageView;
        private TextView mainActivityRatingTv;
        private TextView mainActivityRestaurantNameTv;
        private TextView mainActivityRestaurantDescriptionTv;
        private TextView mainActivityLocatedAtTv;

        public MainActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            mainActivityRestaurantImageView = itemView.findViewById(R.id.main_activity_restaurant_iv);
            mainActivityRatingTv = itemView.findViewById(R.id.main_activity_rating_tv);
            mainActivityRestaurantNameTv = itemView.findViewById(R.id.main_activity_restaurant_name_tv);
            mainActivityRestaurantDescriptionTv = itemView.findViewById(R.id.main_activity_restaurant_desc_tv);
            mainActivityLocatedAtTv = itemView.findViewById(R.id.main_activity_located_at_tv);
        }
    }
}
