package com.example.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.StateActivity;
import com.example.appbanhang.model.Bill;

import java.util.ArrayList;

public class AdminNotificationAdapter extends RecyclerView.Adapter<AdminNotificationAdapter.HomeViewHolder>{
    private ArrayList<Bill> list;
    private Context context;

    public AdminNotificationAdapter(ArrayList<Bill> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Bill bill = list.get(position);
        holder.notification.setText(bill.getName() +" đã đặt đơn hàng "+ bill.getIdBill());
        holder.tvTime.setText(bill.getCurrentTime()+" "+bill.getCurrentDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, StateActivity.class);
                i.putExtra("idBill", bill.getIdBill());
                i.putExtra("idOrder", bill.getIdOrder());
                i.putExtra("idUser", bill.getIdUser());
//                i.putExtra("price", bill.getPrice());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView notification, tvTime;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            notification = itemView.findViewById(R.id.notification);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
