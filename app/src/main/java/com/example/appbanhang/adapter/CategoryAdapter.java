package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.FoodViewHolder> {
    private List<Category> list;
    private ItemListenerFood itemListener;

    public CategoryAdapter() {
        list = new ArrayList<>();
    }
    public void setItemListener(ItemListenerFood itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public Category getItem(int position){
        return list.get(position);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Category food=list.get(position);
        holder.img.setImageResource(food.getImg());
        holder.tv.setText(food.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;
        private TextView tv;
        public FoodViewHolder(@NonNull View view) {
            super(view);
            img=view.findViewById(R.id.img);
            tv=view.findViewById(R.id.tvName);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            itemListener.onItemClick(view, getAdapterPosition() );
        }
    }
    public interface ItemListenerFood{
        void onItemClick(View view, int position);
    }
}
