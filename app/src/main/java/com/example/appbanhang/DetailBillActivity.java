package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.appbanhang.adapter.BillAdapter;
import com.example.appbanhang.adapter.CartAdapter;
import com.example.appbanhang.model.Bill;
import com.example.appbanhang.model.Cart;
import com.example.appbanhang.model.Order;
import com.example.appbanhang.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DetailBillActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titlePage, tvAddress, tvidBill, tvDate, tvPrice;
    RecyclerView recyclerView;
    ArrayList<Cart> cartArrayList= new ArrayList<>();
    CartAdapter cartAdapter;
    DatabaseReference myRef;
    FirebaseUser auth;
    String address, idBill, idOrder, price, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);
        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        idBill = getIntent().getStringExtra("id");
        idOrder = getIntent().getStringExtra("idOrder");
        address = getIntent().getStringExtra("address");
        price = getIntent().getStringExtra("price");

        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        tvAddress.setText(address);
        tvPrice.setText(String.valueOf(decimalFormat.format(Double.parseDouble(price)) + " đ"));
        tvidBill.setText(idBill);

        LinearLayoutManager manager=new LinearLayoutManager(DetailBillActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        auth=FirebaseAuth.getInstance().getCurrentUser();
        myRef=FirebaseDatabase.getInstance().getReference("Bill/"+auth.getUid());
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Bill bill = data.getValue(Bill.class);
                    if (bill.getIdOrder().equalsIgnoreCase(idOrder)) {
                        date=bill.getCurrentDate()+" "+bill.getCurrentTime();
                        cartAdapter = new CartAdapter(bill.getCartArrayList(), DetailBillActivity.this);
                    }
                }
                tvDate.setText(date);
                recyclerView.setAdapter(cartAdapter);
                recyclerView.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        toolbar=findViewById(R.id.toolbar);
        titlePage=findViewById(R.id.toolbar_title);
        tvAddress=findViewById(R.id.tvAddress);
        tvPrice=findViewById(R.id.tvPrice);
        tvidBill=findViewById(R.id.idBill);
        tvDate=findViewById(R.id.tvTime);
        recyclerView=findViewById(R.id.recycleView_food);
    }
}