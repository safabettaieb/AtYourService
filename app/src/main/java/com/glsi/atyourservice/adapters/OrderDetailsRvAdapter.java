package com.glsi.atyourservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.glsi.atyourservice.R;
import com.glsi.atyourservice.models.ProductBasketItem;

import java.util.List;

public class OrderDetailsRvAdapter extends RecyclerView.Adapter<OrderDetailsRvAdapter.MyViewHolder> {
    private List<ProductBasketItem> items;
    private Context context;
    private LayoutInflater myInflater;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private OrderDetailsRvAdapter myAdapter;
        private TextView productName;
        private TextView productQuantity;
        private TextView productPrice;
        private ImageView productImg;

        public MyViewHolder(@NonNull View itemView, OrderDetailsRvAdapter adapter) {
            super(itemView);
            myAdapter = adapter;
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImg = itemView.findViewById(R.id.productImg);
        }
    }

    public OrderDetailsRvAdapter(Context context, List<ProductBasketItem> items) {
        this.items = items;
        this.context = context;
        myInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OrderDetailsRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = myInflater.inflate(R.layout.order_details_item, parent, false);
        return new OrderDetailsRvAdapter.MyViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsRvAdapter.MyViewHolder holder, int position) {
        holder.productName.setText(items.get(position).getProduct().getName());
        holder.productQuantity.setText(String.valueOf(items.get(position).getBasketItem().getQuantity()));
        holder.productPrice.setText(String.valueOf(items.get(position).getProduct().getPrice()*items.get(position).getBasketItem().getQuantity()));

        Glide.with(context)
                .load(items.get(position).getProduct().getImg())
                .into(holder.productImg);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
