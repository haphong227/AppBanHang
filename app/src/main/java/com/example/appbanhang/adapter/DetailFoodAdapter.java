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

import com.example.appbanhang.ListFoodActivity;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Food;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailFoodAdapter extends RecyclerView.Adapter<DetailFoodAdapter.HomeViewHolder> {
    List<Food> list;
    Context context;
    FirebaseUser user;

    public DetailFoodAdapter(List<Food> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        Food food = list.get(position);
        holder.txtName.setText(food.getName());
        holder.txtPrice.setText(String.valueOf(decimalFormat.format(Double.parseDouble(food.getPrice())) + " đ"));
        Picasso.get().load(food.getImage())
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, txtPrice;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img);
            txtName = view.findViewById(R.id.txtName);
            txtPrice = view.findViewById(R.id.txtprice);
        }
    }
}
