package com.example.appbanhang.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.model.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.HomeViewHolder>{
    private List<Food> list;
    private ItemListener itemListener;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<Food> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public FoodAdapter() {
        list=new ArrayList<>();
    }
    public Food getFood(int position){
        return list.get(position);
    }
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Food sale = list.get(position);
        holder.img.setImageResource(sale.getImg());
        holder.txtName.setText(sale.getTxtName());
        holder.txtPrice.setText(sale.getTxtPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;
        private TextView txtName, txtPrice;
        public HomeViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img);
            txtName=view.findViewById(R.id.txtName);
            txtPrice=view.findViewById(R.id.txtprice);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.onItemClick(view, getAdapterPosition());
        }
    }
    public interface ItemListener{
        void onItemClick(View view, int position);
    }

}
