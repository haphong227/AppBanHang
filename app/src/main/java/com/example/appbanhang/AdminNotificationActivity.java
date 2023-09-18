package com.example.appbanhang;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.adapter.AdminNotificationAdapter;
import com.example.appbanhang.model.Bill;
import com.example.appbanhang.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminNotificationActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView titlePage;
    RecyclerView rcv_noti;
    AdminNotificationAdapter adapter;
    DatabaseReference myNoti, myUser;
    private FirebaseUser auth;
    ArrayList<Bill> notificationArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);
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

        LinearLayoutManager manager = new LinearLayoutManager(AdminNotificationActivity.this, RecyclerView.VERTICAL,false);
        rcv_noti.setLayoutManager(manager);

        getIdUser();
    }

    private void getIdUser() {
        auth = FirebaseAuth.getInstance().getCurrentUser();
        myUser = FirebaseDatabase.getInstance().getReference("User");
        myUser.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    String idUser = user.getToken();
                    addNoti(idUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addNoti(String idUser) {
        myNoti = FirebaseDatabase.getInstance().getReference("Bill/" +idUser);
        myNoti.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Bill bill = data.getValue(Bill.class);

                    notificationArrayList.add(bill);

                }
                adapter = new AdminNotificationAdapter(notificationArrayList, AdminNotificationActivity.this);
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