package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.model.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.HomeViewHolder>{
    private ArrayList<Notification> list;
    private Context context;

    public NotificationAdapter(ArrayList<Notification> list, Context context) {
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
        Notification notification = list.get(position);
        String stateOrder = notification.getStateOrder();
        if (stateOrder.equalsIgnoreCase("Đã xác nhận")){
            holder.notification.setText("Đơn hàng đã xác nhận\n" +
                    "Đơn hàng "+notification.getIdBill()+" đã được xác nhận. Vui lòng click vào thông báo để xem kiểm tra chi tiết đơn hàng." );
        } else if (stateOrder.equalsIgnoreCase("Đang giao hàng")) {
            holder.notification.setText("Bạn có đơn hàng đang trên đường giao\n" +
                    "Đơn hàng "+notification.getIdBill()+ " đang trong quá trình vận chuyển." );
        }else {
            holder.notification.setText("Giao hàng thành công\n" +
                    "Đơn hàng "+notification.getIdBill()+" đã giao thành công đến bạn.");
        }
        holder.tvTime.setText(notification.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, DetailBillActivity.class);
//                i.putExtra("id", bill.getIdBill());
//                i.putExtra("idOrder", bill.getIdOrder());
//                i.putExtra("address", bill.getAddress());
//                i.putExtra("price", bill.getPrice());
//                context.startActivity(i);
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
