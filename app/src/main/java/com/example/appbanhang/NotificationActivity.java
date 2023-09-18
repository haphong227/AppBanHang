package com.example.appbanhang;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.adapter.NotificationAdapter;
import com.example.appbanhang.model.Bill;
import com.example.appbanhang.model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titlePage;
    RecyclerView rcv_noti;
    NotificationAdapter adapter;
//    BillAdapter billAdapter;
    DatabaseReference myNoti;
    private FirebaseUser auth;
    ArrayList<Notification> notificationArrayList;
    ArrayList<Bill> billArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
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

        LinearLayoutManager manager = new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL,false);
        rcv_noti.setLayoutManager(manager);

        auth = FirebaseAuth.getInstance().getCurrentUser();
//        myNoti = FirebaseDatabase.getInstance().getReference("Bill/" + auth.getUid());
//        myNoti.child("").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                billArrayList = new ArrayList<>();
//                for (DataSnapshot data : snapshot.getChildren()) {
//                    Bill bill = data.getValue(Bill.class);
//                    billArrayList.add(bill);
//
//                }
//                adapter = new NotificationAdapter(billArrayList, NotificationActivity.this);
//                rcv_noti.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        myNoti = FirebaseDatabase.getInstance().getReference("Notification/"+auth.getUid());
        myNoti.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationArrayList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Notification notification = data.getValue(Notification.class);
                    notificationArrayList.add(notification);

                }
                adapter = new NotificationAdapter(notificationArrayList, NotificationActivity.this);
                rcv_noti.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        titlePage = findViewById(R.id.toolbar_title);
        rcv_noti = findViewById(R.id.rcv_noti);
    }
}