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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context mContext;
    private List<OrderModal> mOrderList;

    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onRemoveClicked(String docPostition);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }
    public OrderAdapter(Context mContext, List<OrderModal> mOrderList) {
        this.mContext = mContext;
        this.mOrderList = mOrderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        Glide.with(mContext).load(mOrderList.get(position).getFoodImageUri()).into(holder.orderFoodImageview);
        holder.orderFoodNameTv.setText(mOrderList.get(position).getFoodName());
        holder.orderFoodPriceTv.setText(mOrderList.get(position).getFoodPrice() + ".00 ETB");
        holder.processedTv.setText(mOrderList.get(position).getProcessed());


    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{

        private ImageView orderFoodImageview;
        private TextView  orderFoodNameTv;
        private TextView orderFoodPriceTv;
        private Button  orderRemoveFoodBtn;
        private TextView processedTv;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            orderFoodImageview = itemView.findViewById(R.id.order_food_imageview);
            orderFoodNameTv = itemView.findViewById(R.id.order_food_name);
            orderFoodPriceTv = itemView.findViewById(R.id.order_food_price);
            orderRemoveFoodBtn = itemView.findViewById(R.id.order_remove_food_btn);
            processedTv = itemView.findViewById(R.id.processed_tv);

            orderRemoveFoodBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        String docId = mOrderList.get(getAdapterPosition()).docId;
                        mListener.onRemoveClicked(docId);
                    }
                }
            });
        }
    }
}
