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

public class BasketRvAdapter extends RecyclerView.Adapter<BasketRvAdapter.MyViewHolder> {
    private List<ProductBasketItem> items;
    private Context context;
    private OnBasketItemClickListener onBasketItemClickListener;
    private LayoutInflater myInflater;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private BasketRvAdapter myAdapter;
        private TextView productName;
        private TextView productQuantity;
        private ImageView btnDelete;
        private ImageView btnEdit;
        private ImageView productImg;

        public MyViewHolder(@NonNull final View itemView, BasketRvAdapter adapter) {
            super(itemView);
            myAdapter = adapter;
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            productImg = itemView.findViewById(R.id.productImg);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myAdapter.onBasketItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            myAdapter.onBasketItemClickListener.onItemDelete(itemView, position);
                        }
                    }
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myAdapter.onBasketItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            myAdapter.onBasketItemClickListener.onItemEdit(itemView, position);
                        }
                    }
                }
            });

        }

    }

    public BasketRvAdapter(Context context, List<ProductBasketItem> items) {
        this.items = items;
        this.context = context;
        myInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BasketRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = myInflater.inflate(R.layout.basket_item, parent, false);
        return new BasketRvAdapter.MyViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketRvAdapter.MyViewHolder holder, int position) {
        holder.productName.setText(items.get(position).getProduct().getName());
        holder.productQuantity.setText(String.valueOf(items.get(position).getBasketItem().getQuantity()));

        Glide.with(context)
                .load(items.get(position).getProduct().getImg())
                .into(holder.productImg);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnBasketItemClickListener {
        void onItemDelete(View v, int position);
        void onItemEdit(View v, int position);
    }

    public void setOnBasketItemClickListener(OnBasketItemClickListener onBasketItemClickListener) {
        this.onBasketItemClickListener = onBasketItemClickListener;
    }
}
