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
import com.glsi.atyourservice.models.Product;

import java.util.List;

public class ProductsRvAdapter extends RecyclerView.Adapter<ProductsRvAdapter.MyViewHolder> {
    private List<Product> items;
    private Context context;
    private OnProductItemClickListener onProductItemClickListener;
    private LayoutInflater myInflater;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProductsRvAdapter myAdapter;
        private TextView productName;
        private TextView productPrice;
        private ImageView productImg;

        public MyViewHolder(@NonNull View itemView, ProductsRvAdapter adapter) {
            super(itemView);
            myAdapter = adapter;
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImg = itemView.findViewById(R.id.productImg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myAdapter.onProductItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    myAdapter.onProductItemClickListener.onItemClick(itemView, position);
                }
            }
        }
    }

    public ProductsRvAdapter(Context context, List<Product> items) {
        this.items = items;
        this.context = context;
        myInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProductsRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = myInflater.inflate(R.layout.product_item, parent, false);
        return new ProductsRvAdapter.MyViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsRvAdapter.MyViewHolder holder, int position) {
        holder.productName.setText(items.get(position).getName());
        holder.productPrice.setText(String.valueOf(items.get(position).getPrice()));

        Glide.with(context)
                .load(items.get(position).getImg())
                .into(holder.productImg);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnProductItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnProductItemClickListener(OnProductItemClickListener onProductItemClickListener) {
        this.onProductItemClickListener = onProductItemClickListener;
    }
}
