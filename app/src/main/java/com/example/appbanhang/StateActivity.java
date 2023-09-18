package com.example.appbanhang;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.adapter.ListFoodAdapter;
import com.example.appbanhang.model.Bill;
import com.example.appbanhang.notification.StateNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class StateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Toolbar toolbar;
    TextView titlePage, tvAddress, tvidBill, tvDate, tvPrice, tvEmail, tvName, tvPhone;
    Spinner spinner;
    Button btAccept;
    RecyclerView recyclerView;
    ListFoodAdapter cartAdapter;
    DatabaseReference myRef, myNoti;
    String address, idBill, idOrder, idUser, price, date, name, stateOrder, randomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
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

        idBill = getIntent().getStringExtra("idBill");
        idOrder = getIntent().getStringExtra("idOrder");
        idUser = getIntent().getStringExtra("idUser");

        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        LinearLayoutManager manager = new LinearLayoutManager(StateActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        myRef = FirebaseDatabase.getInstance().getReference("Bill/" + idUser);
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    Bill bill = data.getValue(Bill.class);
                    if (bill.getIdOrder().equalsIgnoreCase(idOrder)) {
                        name = bill.getName();
                        address = bill.getAddress();
                        price = bill.getPrice();
                        date = bill.getCurrentTime() + " " + bill.getCurrentDate();
                        idBill = bill.getIdBill();
                        cartAdapter = new ListFoodAdapter(bill.getCartArrayList(), StateActivity.this);
                    }
                }
                tvName.setText(name);
                tvAddress.setText(address);
                tvPrice.setText(String.valueOf(decimalFormat.format(Double.parseDouble(price))) + " đ");
                tvidBill.setText(idBill);
                tvDate.setText(date);

                recyclerView.setAdapter(cartAdapter);
                recyclerView.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner.setOnItemSelectedListener(this);
        btAccept.setOnClickListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        stateOrder = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if (view == btAccept) {
            HashMap<String, Object> bill = new HashMap<>();
            bill.put("stateOrder", stateOrder);
            myRef.child(idBill).updateChildren(bill).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(StateActivity.this,
                                "Cập nhật thành công trạng thái đơn hàng!", Toast.LENGTH_SHORT).show();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                        Intent intent = new Intent(StateActivity.this,
                                StateNotification.class);
                        intent.putExtra("myAction", "mDoNotify");
                        intent.putExtra("Name", name);
                        intent.putExtra("Description", stateOrder.toLowerCase());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(StateActivity.this,
                                0, intent, 0);
                        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                        addNotification();
                        startActivity(new Intent(StateActivity.this, AdminNotificationActivity.class));
                    }
                }
            });
        }
    }

    private void addNotification() {
        String saveCurrentTime, savecurrentDate;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat curDate = new SimpleDateFormat("dd-MM-yyyy");
        savecurrentDate = curDate.format(c.getTime());
        SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = curTime.format(c.getTime());
        randomKey = savecurrentDate + "-" + saveCurrentTime;

        HashMap<String, Object> notification = new HashMap<>();
        notification.put("idNoti", "Noti" + randomKey);
        notification.put("idBill", idBill);
        notification.put("name", name);
        notification.put("stateOrder", stateOrder);
        notification.put("time", saveCurrentTime + " " + savecurrentDate);

        myNoti = FirebaseDatabase.getInstance().getReference("Notification/" + idUser);
        myNoti.child("Noti" + randomKey).updateChildren(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("push notification");
                    finish();
                }
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        titlePage = findViewById(R.id.toolbar_title);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvPrice = findViewById(R.id.tvPrice);
        tvidBill = findViewById(R.id.tvidBill);
        tvDate = findViewById(R.id.tvDate);
        recyclerView = findViewById(R.id.recycleView_food);
        spinner = findViewById(R.id.stateOrder);
        btAccept = findViewById(R.id.btAccept);
    }
}