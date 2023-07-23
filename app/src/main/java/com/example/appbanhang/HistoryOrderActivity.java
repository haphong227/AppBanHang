package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.appbanhang.adapter.BillAdapter;
import com.example.appbanhang.adapter.CartAdapter;
import com.example.appbanhang.model.Bill;
import com.example.appbanhang.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryOrderActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titlePage;
    RecyclerView recyclerView_bill;
    BillAdapter billAdapter;
    ArrayList<Bill> billArrayList = new ArrayList<>();
    private FirebaseUser auth;
    private DatabaseReference myBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        toolbar = findViewById(R.id.toolbar);
        titlePage = findViewById(R.id.toolbar_title);
        recyclerView_bill = findViewById(R.id.recycleView_bill);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(HistoryOrderActivity.this,MainActivity.class));
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoryOrderActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView_bill.setLayoutManager(linearLayoutManager);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        myBill= FirebaseDatabase.getInstance().getReference("Bill/"+auth.getUid());
        myBill.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Bill bill = data.getValue(Bill.class);
                    billArrayList.add(bill);
                }
                billAdapter = new BillAdapter(billArrayList, HistoryOrderActivity.this);
                recyclerView_bill.setAdapter(billAdapter);
                recyclerView_bill.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}