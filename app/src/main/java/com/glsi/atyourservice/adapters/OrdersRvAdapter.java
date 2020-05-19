package com.glsi.atyourservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glsi.atyourservice.R;
import com.glsi.atyourservice.models.Order;

import java.util.List;

public class OrdersRvAdapter extends RecyclerView.Adapter<OrdersRvAdapter.MyViewHolder> {
    private List<Order> items;
    private Context context;
    private OnOrderItemClickListener onOrderItemClickListener;
    private LayoutInflater myInflater;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OrdersRvAdapter myAdapter;
        private TextView orderName;
        private TextView orderId;

        public MyViewHolder(@NonNull View itemView, OrdersRvAdapter adapter) {
            super(itemView);
            myAdapter = adapter;
            orderName = itemView.findViewById(R.id.orderName);
            orderId = itemView.findViewById(R.id.orderId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myAdapter.onOrderItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    myAdapter.onOrderItemClickListener.onItemClick(itemView, position);
                }
            }
        }
    }

    public OrdersRvAdapter(Context context, List<Order> items) {
        this.items = items;
        this.context = context;
        myInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OrdersRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = myInflater.inflate(R.layout.order_item, parent, false);
        return new OrdersRvAdapter.MyViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersRvAdapter.MyViewHolder holder, int position) {
        holder.orderName.setText(items.get(position).getName());
        holder.orderId.setText(String.valueOf(items.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnOrderItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnOrderItemClickListener(OnOrderItemClickListener onOrderItemClickListener) {
        this.onOrderItemClickListener = onOrderItemClickListener;
    }
}
