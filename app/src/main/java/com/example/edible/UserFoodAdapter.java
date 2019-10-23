package com.example.edible;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserFoodAdapter extends RecyclerView.Adapter<UserFoodAdapter.UserFoodViewHolder> {


    private Context mContext;
    private List<UserFoodModal> mUserFoodList;
    private String restaurantName;

    public UserFoodAdapter(Context mContext, List<UserFoodModal> mUserFoodList,String restaurantName) {
        this.mContext = mContext;
        this.mUserFoodList = mUserFoodList;
        this.restaurantName = restaurantName;
    }

    @NonNull
    @Override
    public UserFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_food_item,parent,false);
        return new UserFoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserFoodViewHolder holder, int position) {
        Glide.with(mContext).load(mUserFoodList.get(position).getFoodImageUri()).into(holder.mUserFoodImageView);
        holder.mUserFoodTitleTv.setText(mUserFoodList.get(position).getFoodName());
        holder.mUserFoodContentTv.setText(mUserFoodList.get(position).getFoodContent());
        holder.mUserFoodPrice.setText(mUserFoodList.get(position).getFoodPrice()+".00 ETB");
        final String documentId = mUserFoodList.get(position).docId;

        holder.mAddToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,OrdersActivity.class);
                intent.putExtra("order",documentId);
                intent.putExtra("restaurant",restaurantName);
                mContext.startActivity(intent);
            }
        });

        //to set space so that the fab btn won't cover item
        if (position + 1 == getItemCount()) {
            // set bottom margin to 72dp.
            setBottomMargin(holder.itemView, (int) (73 * Resources.getSystem().getDisplayMetrics().density));
        } else {
            // reset bottom margin back to zero.
            setBottomMargin(holder.itemView, 0);
        }

    }

    //method user defined for setting bottom margin last item
    public static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }
    }

    @Override
    public int getItemCount() {
        return mUserFoodList.size();
    }

    class UserFoodViewHolder extends RecyclerView.ViewHolder{

        private ImageView mUserFoodImageView;
        private TextView mUserFoodTitleTv;
        private TextView mUserFoodContentTv;
        private TextView mUserFoodPrice;
        private ImageButton mAddToCartBtn;

        public UserFoodViewHolder(@NonNull View itemView) {
            super(itemView);

            mUserFoodImageView = itemView.findViewById(R.id.user_food_iv);
            mUserFoodTitleTv = itemView.findViewById(R.id.user_food_title_tv);
            mUserFoodContentTv = itemView.findViewById(R.id.user_food_content_tv);
            mAddToCartBtn = itemView.findViewById(R.id.add_to_cart_btn);
            mUserFoodPrice = itemView.findViewById(R.id.user_food_price);

        }
    }
}
