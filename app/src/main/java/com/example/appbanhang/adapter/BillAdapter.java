package com.example.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.DetailBillActivity;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Bill;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.HomeViewHolder>{
    private ArrayList<Bill> list;
    Context context;
    CartAdapter cartAdapter;


    public BillAdapter(ArrayList<Bill> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new BillAdapter.HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        Bill bill = list.get(position);
        holder.tvQuantity.setText(String.valueOf(bill.getQuantity()));
        holder.tvPrice.setText(String.valueOf(decimalFormat.format(Double.parseDouble(bill.getPrice())) + " đ"));
        holder.tvDate.setText(bill.getCurrentDate());
        holder.tvTime.setText(bill.getCurrentTime());
        holder.tvStateOrder.setText(bill.getStateOrder().toUpperCase());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailBillActivity.class);
                i.putExtra("id", bill.getIdBill());
                i.putExtra("idOrder", bill.getIdOrder());
                i.putExtra("address", bill.getAddress());
                i.putExtra("price", bill.getPrice());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView tvQuantity, tvPrice, tvDate, tvTime, tvStateOrder;
        CardView cardView;
        public HomeViewHolder(@NonNull View view) {
            super(view);
            tvQuantity=view.findViewById(R.id.tvQuantity);
            tvPrice=view.findViewById(R.id.tvPrice);
            tvDate=view.findViewById(R.id.tvDate);
            tvTime=view.findViewById(R.id.tvTime);
            cardView=view.findViewById(R.id.card_view);
            tvStateOrder=view.findViewById(R.id.tvStateOrder);
        }
    }
}
