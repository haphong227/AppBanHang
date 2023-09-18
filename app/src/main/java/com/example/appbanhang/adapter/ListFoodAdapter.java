package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.model.Cart;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListFoodAdapter extends RecyclerView.Adapter<ListFoodAdapter.HomeViewHolder> {
    private List<Cart> list;
    Context context;
    private DatabaseReference myRef;
    private FirebaseUser auth;

    public ListFoodAdapter(List<Cart> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, parent, false);
        return new HomeViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        Cart cart = list.get(position);
        holder.tvName.setText(cart.getProductName());
        holder.tvQuantity.setText(cart.getTotalQuantity() + "x   ");
        holder.tvPrice.setText(String.valueOf(decimalFormat.format(Double.parseDouble(cart.getTotalPrice())))+"đ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvPrice;
        public HomeViewHolder(@NonNull View view) {
            super(view);
             tvName = view.findViewById(R.id.tvName);
             tvQuantity = view.findViewById(R.id.tvQuantity);
             tvPrice = view.findViewById(R.id.tvPrice);
        }

    }

}
