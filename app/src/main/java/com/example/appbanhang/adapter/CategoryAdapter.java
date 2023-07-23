package com.example.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.AdminFoodActivity;
import com.example.appbanhang.ListFoodActivity;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.HomeViewHolder> {
    private List<Category> list;
    Context context;

    public CategoryAdapter(List<Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new HomeViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Category category = list.get(position);
        holder.tv.setText(category.getName());
        Picasso.get().load(category.getImage())
                .into(holder.img);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ListFoodActivity.class);
                i.putExtra("id", category.getId());
                i.putExtra("name", category.getName());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv;
        CardView cardView;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img);
            tv = view.findViewById(R.id.tvName);
            cardView = view.findViewById(R.id.cardView);
        }

    }

}
