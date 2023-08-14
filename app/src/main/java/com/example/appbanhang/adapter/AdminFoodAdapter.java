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

import com.example.appbanhang.EditDeleteFoodActivity;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Food;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminFoodAdapter extends RecyclerView.Adapter<AdminFoodAdapter.HomeViewHolder> {
    List<Food> list;
    Context context;
    String nameCate;

    FirebaseUser user;
    DatabaseReference myCate= FirebaseDatabase.getInstance().getReference("category");

    public AdminFoodAdapter(List<Food> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<Food> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foodadmin, parent, false);
        return new HomeViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        Food food = list.get(position);
        holder.txtName.setText(food.getName());
        holder.txtDes.setText(food.getDes());
        holder.txtQuantity.setText("Số lượng: " +String.valueOf(food.getQuantity()));
        holder.txtPrice.setText(String.valueOf(decimalFormat.format(Double.parseDouble(food.getPrice())) + " đ"));
        Picasso.get().load(food.getImage())
                .into(holder.img);



        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, EditDeleteFoodActivity.class);
                i.putExtra("id", food.getId());
                i.putExtra("idCate", food.getIdCate());
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
        TextView txtName, txtPrice, txtDes, txtQuantity;
        CardView cardView;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img);
            txtName = view.findViewById(R.id.txtName);
            txtPrice = view.findViewById(R.id.txtprice);
            txtDes = view.findViewById(R.id.txtdes);
            txtQuantity = view.findViewById(R.id.txtQuantity);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}
