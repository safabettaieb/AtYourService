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
import com.glsi.atyourservice.models.Category;

import java.util.List;


public class CategoriesRvAdapter extends RecyclerView.Adapter<CategoriesRvAdapter.MyViewHolder> {
    private List<Category> items;
    private Context context;
    private int layout;
    private OnCategoryItemClickListener onCategoryItemClickListener;
    private LayoutInflater myInflater;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CategoriesRvAdapter myAdapter;
        private TextView categoryId;
        private TextView categoryName;
        private ImageView categoryImg;

        public MyViewHolder(@NonNull View itemView, CategoriesRvAdapter adapter) {
            super(itemView);
            myAdapter = adapter;
            categoryId = itemView.findViewById(R.id.categoryId);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryImg = itemView.findViewById(R.id.categoryImg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myAdapter.onCategoryItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    myAdapter.onCategoryItemClickListener.onItemClick(itemView, position);
                }
            }
        }
    }

    public CategoriesRvAdapter(Context context, List<Category> items, int layout) {
        this.items = items;
        this.context = context;
        this.layout = layout;
        myInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = myInflater.inflate(this.layout, parent, false);
        return new MyViewHolder(v, this);
    }
     //liaison entre viewholder : v => (onCreateViewHolder) et items :list => chq element de items
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.categoryId.setText(items.get(position).getId());
        holder.categoryName.setText(items.get(position).getName());

        Glide.with(context)
                .load(items.get(position).getImg())
                .into(holder.categoryImg);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnCategoryItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnCategoryItemClickListener(OnCategoryItemClickListener onCategoryItemClickListener) {
        this.onCategoryItemClickListener = onCategoryItemClickListener;
    }
}
