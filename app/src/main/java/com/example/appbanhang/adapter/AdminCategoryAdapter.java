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
import com.example.appbanhang.EditDeleteCategoryActivity;
import com.example.appbanhang.EditDeleteFoodActivity;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.HomeViewHolder> {
    private List<Category> list;
    Context context;

    public AdminCategoryAdapter(List<Category> list, Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoryadmin, parent, false);
        return new HomeViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Category category = list.get(position);
        holder.tvName.setText(category.getName());
        Picasso.get().load(category.getImage())
                .into(holder.img);
        holder.tvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AdminFoodActivity.class);
                i.putExtra("id", category.getId());
                i.putExtra("name", category.getName());
                context.startActivity(i);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditDeleteCategoryActivity.class);
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
        ImageView img, edit;
        TextView tvName, tvList;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img);
            edit = view.findViewById(R.id.edit);
            tvName = view.findViewById(R.id.tvName);
            tvList = view.findViewById(R.id.tvList);
        }

    }

}
