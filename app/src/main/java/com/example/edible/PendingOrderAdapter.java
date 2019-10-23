package com.example.edible;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {


    private Context mContext;
    private List<PendingOrderModal> mPendFoodList;

    private onItemClickListener mListener;
    public interface onItemClickListener{
        void onDoneClicked(String docPostition);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    public PendingOrderAdapter(Context mContext, List<PendingOrderModal> mPendFoodList) {
        this.mContext = mContext;
        this.mPendFoodList = mPendFoodList;
    }

    @NonNull
    @Override
    public PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_order_item,parent,false);
        return new PendingOrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderViewHolder holder, int position) {
        holder.pendingOrderFoodName.setText(mPendFoodList.get(position).getFoodName());
        holder.pendingOrderFoodPrice.setText(mPendFoodList.get(position).getFoodPrice() + ".00 ETB");
        holder.pendingOrderUsername.setText(mPendFoodList.get(position).getOrdererUsername());


    }

    @Override
    public int getItemCount() {
        return mPendFoodList.size();
    }

    class PendingOrderViewHolder extends RecyclerView.ViewHolder{

        private TextView pendingOrderFoodName,pendingOrderFoodPrice,pendingOrderUsername;
        private Button pendingOrderDoneBtn;

        public PendingOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            pendingOrderFoodName = itemView.findViewById(R.id.pending_order_food_name);
            pendingOrderFoodPrice = itemView.findViewById(R.id.pending_order_food_price);
            pendingOrderUsername = itemView.findViewById(R.id.pending_order_username);
            pendingOrderDoneBtn = itemView.findViewById(R.id.pending_order_done_btn);

            pendingOrderDoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null) {
                        String docId = mPendFoodList.get(getAdapterPosition()).docId;
                        mListener.onDoneClicked(docId);
                    }
                }
            });
        }

    }
}
