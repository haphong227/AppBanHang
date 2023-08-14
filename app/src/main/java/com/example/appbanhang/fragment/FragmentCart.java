package com.example.appbanhang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.AdminFoodActivity;
import com.example.appbanhang.PaymentActivity;
import com.example.appbanhang.R;
import com.example.appbanhang.adapter.CartAdapter;
import com.example.appbanhang.model.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class FragmentCart extends Fragment implements View.OnClickListener{
    TextView tvTong;
    RecyclerView recyclerView_cart;
    Button btBuy;
    CartAdapter cartAdapter;
    ArrayList<Cart> cartArrayList;
    private FirebaseUser auth;
    private DatabaseReference myRef;
    double total=0;
    String TAG="Order";
    private String saveCurDate, saveCurTime, randomKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView_cart = view.findViewById(R.id.recycleView_cart);
        tvTong = view.findViewById(R.id.tvTong);
        btBuy = view.findViewById(R.id.btBuy);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat curDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurDate = curDate.format(c.getTime());
        SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
        saveCurTime = curTime.format(c.getTime());
        randomKey = saveCurDate + "-" + saveCurTime;

        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_cart.setLayoutManager(linearLayoutManager);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Cart/" + auth.getUid());
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalAmount = 0;
                cartArrayList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Cart cart = data.getValue(Cart.class);
                    cartArrayList.add(cart);
                    totalAmount += Double.parseDouble(cart.getTotalPrice());
                }
                total=totalAmount;
                cartAdapter = new CartAdapter(cartArrayList, getContext());
                recyclerView_cart.setAdapter(cartAdapter);
                recyclerView_cart.setHasFixedSize(true);
                tvTong.setText("Tổng tiền:" + String.valueOf(decimalFormat.format(totalAmount)) + " đ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();

            }
        });

        btBuy.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == btBuy){
            if (total > 0) {
//                myRef = FirebaseDatabase.getInstance().getReference("Order/" + auth.getUid());
//                HashMap<String,Object> order=new HashMap<>();
//                order.put("idOrder", TAG+randomKey);
//                order.put("listItem", cartArrayList);
//                order.put("price", total);
//                order.put("user", auth.getEmail());
//
//                myRef.child(TAG+randomKey).updateChildren(order)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Log.i("order", "okkkk");
//                            }
//                        });
                Intent i = new Intent(getContext(), PaymentActivity.class);
                i.putExtra("idOrder",TAG+randomKey );
                startActivity(i);
            } else {
                Toast.makeText(getContext(), "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
