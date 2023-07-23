package com.example.appbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.AdminFoodActivity;
import com.example.appbanhang.DetailFoodActivity;
import com.example.appbanhang.EditDeleteCategoryActivity;
import com.example.appbanhang.ListFoodActivity;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Cart;
import com.example.appbanhang.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.HomeViewHolder> {
    private List<Cart> list;
    Context context;
    private DatabaseReference myRef;
    private FirebaseUser auth;

    public CartAdapter(List<Cart> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new HomeViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        Cart cart = list.get(position);
        holder.product_name.setText(cart.getProductName());
        holder.quantity.setText(cart.getTotalQuantity());
        holder.product_price.setText(String.valueOf(decimalFormat.format(Double.parseDouble(cart.getProductPrice()))));
        holder.sub_total.setText(String.valueOf(decimalFormat.format(Double.parseDouble(cart.getTotalPrice()))));
        Picasso.get().load(cart.getProductImg())
                .into(holder.img);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        myRef= FirebaseDatabase.getInstance().getReference("Cart/"+auth.getUid());
        holder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xóa món ăn");
                builder.setMessage("Bạn có chắc muốn xóa món ăn khỏi giỏ hàng?");
                builder.setIcon(R.drawable.remove);
                builder.setNegativeButton("Bỏ qua",null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.child(cart.getProductName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, list.size());
                                Toast.makeText(context.getApplicationContext(),
                                        "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView product_name, quantity, product_price, sub_total;
        Button clear;
        CardView cardView;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.product_img);
            product_name = view.findViewById(R.id.product_name);
            quantity = view.findViewById(R.id.quantity);
            product_price = view.findViewById(R.id.product_price);
            sub_total = view.findViewById(R.id.sub_total);
            clear = view.findViewById(R.id.btClear);
            cardView = view.findViewById(R.id.cardView);
        }

    }

}
