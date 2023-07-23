package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.adapter.CartAdapter;
import com.example.appbanhang.model.Cart;
import com.example.appbanhang.model.User;
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

public class PaymentActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titlePage, tvAddress, tvTong;
    RecyclerView recyclerView_cart;
    Button btPay;

    CartAdapter cartAdapter;
    ArrayList<Cart> cartArrayList;
    private FirebaseUser auth;
    private DatabaseReference myCart, myUser, myBill;
    String address, email, nameProduct;
    double tong = 0;
    int tongSl=0;
    private static final String TAG = "Bill";
    String randomKey="";
    String idOrder;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(PayActivity.this,MainActivity.class));
                finish();
            }
        });

        idOrder= getIntent().getStringExtra("idOrder");;

        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView_cart.setLayoutManager(linearLayoutManager);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        myCart = FirebaseDatabase.getInstance().getReference("Cart/"+auth.getUid());
        myCart.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalAmount = 0;
                int sl=0;
                cartArrayList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Cart cart = data.getValue(Cart.class);
                    cartArrayList.add(cart);
                    nameProduct=cart.getProductName();
                    sl+=Integer.parseInt(cart.getTotalQuantity());
                    totalAmount += Double.parseDouble(cart.getTotalPrice());
                }
                cartAdapter = new CartAdapter(cartArrayList, PaymentActivity.this);
                recyclerView_cart.setAdapter(cartAdapter);
                recyclerView_cart.setHasFixedSize(true);

                tongSl=sl;
                tong = totalAmount;
                tvTong.setText(String.valueOf(decimalFormat.format(totalAmount)) + " đ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PaymentActivity.this, "No Data", Toast.LENGTH_SHORT).show();

            }
        });

        myUser = FirebaseDatabase.getInstance().getReference("User");
        myUser.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    if (user.getToken().equalsIgnoreCase(auth.getUid())) {
                        address = user.getDiachi();
                        email = user.getEmail();
                    }
                }
                tvAddress.setText(address);
                Log.i("id", auth.getUid());
                Log.i("địa chỉ", "" + snapshot.child("diachi").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String saveCurrentTime, savecurrentDate;
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat curDate = new SimpleDateFormat("dd-MM-yyyy");
                    savecurrentDate = curDate.format(c.getTime());
                    SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
                    saveCurrentTime = curTime.format(c.getTime());
                    randomKey=savecurrentDate+"-"+saveCurrentTime;

                    HashMap<String,Object> bill=new HashMap<>();
                    bill.put("idBill", TAG+randomKey);
                    bill.put("email", auth.getUid());
                    bill.put("idOrder", idOrder);
                    bill.put("currentTime", saveCurrentTime);
                    bill.put("currentDate", savecurrentDate);
                    bill.put("address", address);
                    bill.put("quantity", tongSl);
                    bill.put("cartArrayList", cartArrayList);
                    bill.put("price",String.valueOf(tong));
                    myBill = FirebaseDatabase.getInstance().getReference("Bill/"+auth.getUid());
                    myBill.child(TAG+randomKey).updateChildren(bill)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(System.currentTimeMillis());
                                    AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                                    Intent intent = new Intent(PaymentActivity.this,
                                            MyReceiver.class);
                                    intent.putExtra("myAction", "mDoNotify");
                                    intent.putExtra("Name",email);
                                    intent.putExtra("Description", String.valueOf(decimalFormat.format(tong)) + " đ");

                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(PaymentActivity.this,
                                            0, intent, 0);
                                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                                    myCart.child("").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.i("Cart", "notifi");
                                        }
                                    });

                                    Toast.makeText(PaymentActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(PaymentActivity.this, DetailBillActivity.class);
                                    i.putExtra("idOrder",idOrder );
//                                    startActivity(i);
                                    finish();
                                }
                            });

            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        titlePage = findViewById(R.id.toolbar_title);
        tvAddress = findViewById(R.id.tvAddress);
        tvTong = findViewById(R.id.tvTong);
        recyclerView_cart = findViewById(R.id.recycleView_cart);
        btPay = findViewById(R.id.btPay);
    }
}