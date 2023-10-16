package com.example.appbanhang;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.adapter.ListFoodAdapter;
import com.example.appbanhang.model.Cart;
import com.example.appbanhang.model.User;
import com.example.appbanhang.notification.MyReceiver;
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

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    TextView titlePage, tvAddress, tvTong, tvTmpPrice, tvShip, tvQuantityFood, tvChangeAddress;
    EditText edNote;
    RecyclerView recyclerView_cart;
    Button btPay;

    ListFoodAdapter cartAdapter;
    ArrayList<Cart> cartArrayList;
    private FirebaseUser auth;
    private DatabaseReference myCart, myUser, myBill, myFood, myNoti;
    String address, email, phone, name, note;
    double total = 0;
    int tongSl = 0;
    private static final String TAG = "Bill";
    String randomKey = "";
    String idOrder;
    DecimalFormat decimalFormat;
    String content = "";


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
                finish();
            }
        });

        idOrder = getIntent().getStringExtra("idOrder");
        total = getIntent().getDoubleExtra("total",22);

        decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        tvTong.setText(String.valueOf(decimalFormat.format(total)) + "đ");
        tvTmpPrice.setText(String.valueOf(decimalFormat.format(total)) + "đ");

        displayCart();
        getInforUser();

        btPay.setOnClickListener(this);
        tvChangeAddress.setOnClickListener(this);

    }

    private void displayCart() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView_cart.setLayoutManager(linearLayoutManager);

        auth = FirebaseAuth.getInstance().getCurrentUser();

        myCart = FirebaseDatabase.getInstance().getReference("Cart/" + auth.getUid());
        myCart.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sl = 0;
                cartArrayList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Cart cart = data.getValue(Cart.class);
                    cartArrayList.add(cart);
                    sl += Integer.parseInt(cart.getTotalQuantity());
                }
                cartAdapter = new ListFoodAdapter(cartArrayList, PaymentActivity.this);
                recyclerView_cart.setAdapter(cartAdapter);
                recyclerView_cart.setHasFixedSize(true);

                tongSl = sl;

                tvQuantityFood.setText("Tạm tính (" + String.valueOf(cartArrayList.size()) + " món)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PaymentActivity.this, "No Data", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getInforUser() {

        myUser = FirebaseDatabase.getInstance().getReference("User");
        myUser.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    if (user.getToken().equalsIgnoreCase(auth.getUid())) {
                        address = user.getDiachi();
                        email = user.getEmail();
                        phone = user.getPhone();
                        name = user.getName();
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
    }


    @Override
    public void onClick(View view) {
        if (view == btPay) {
            note = edNote.getText().toString();
            edNote.setText(note);

            String saveCurrentTime, savecurrentDate;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat curDate = new SimpleDateFormat("yyyy-MM-dd");
            savecurrentDate = curDate.format(c.getTime());
            SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = curTime.format(c.getTime());
            randomKey = savecurrentDate + "-" + saveCurrentTime;

            HashMap<String, Object> bill = new HashMap<>();
            bill.put("idBill", TAG + randomKey);
            bill.put("idUser", auth.getUid());
            bill.put("idOrder", idOrder);
            bill.put("currentTime", saveCurrentTime);
            bill.put("currentDate", savecurrentDate);
            bill.put("address", address);
            bill.put("email", email);
            bill.put("name", name);
            bill.put("note", note);
            bill.put("phone", phone);
            bill.put("quantity", tongSl);
            bill.put("cartArrayList", cartArrayList);
            bill.put("price", String.valueOf(total));
            bill.put("stateOrder", "Đã xác nhận");
            myBill = FirebaseDatabase.getInstance().getReference("Bill/" + auth.getUid());
            myBill.child(TAG + randomKey).updateChildren(bill)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //update số lượng của sản phẩm còn lại sau khi mua hàng
                            for (Cart cart : cartArrayList) {
                                String idFood = cart.getIdFood();
                                myFood = FirebaseDatabase.getInstance().getReference().child("food").child(idFood);
                                myFood.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String quantity = snapshot.child("quantity").getValue().toString();
                                            int newQuantity = Integer.parseInt(quantity) - Integer.parseInt(cart.getTotalQuantity());
                                            HashMap<String, Object> food = new HashMap<>();
                                            food.put("quantity", newQuantity);
                                            myFood.updateChildren(food).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        System.out.println("update soluong");
                                                        finish();
                                                    }

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                            Intent intent = new Intent(PaymentActivity.this,
                                    MyReceiver.class);
                            intent.putExtra("myAction", "mDoNotify");
                            intent.putExtra("Name", name);
                            intent.putExtra("Description", String.valueOf(decimalFormat.format(total)) + " đ");
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(PaymentActivity.this,
                                    0, intent, 0);
                            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            removeCart();

                            Toast.makeText(PaymentActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(PaymentActivity.this, DetailBillActivity.class);
                            i.putExtra("idOrder", idOrder);
                            finish();
                        }
                    });

        }
        if (view == tvChangeAddress) {
            startActivity(new Intent(PaymentActivity.this, MapActivity.class));
        }
    }

    private void removeCart() {
        myCart.child("").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("Cart", "notifi");
            }
        });

    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        titlePage = findViewById(R.id.toolbar_title);
        tvAddress = findViewById(R.id.tvAddress);
        tvTong = findViewById(R.id.tvTong);
        tvTmpPrice = findViewById(R.id.tvTmpPrice);
        tvShip = findViewById(R.id.tvShip);
        edNote = findViewById(R.id.edNote);
        tvQuantityFood = findViewById(R.id.tvQuantityFood);
        tvChangeAddress = findViewById(R.id.tvChangeAddress);
        recyclerView_cart = findViewById(R.id.recycleView_cart);
        btPay = findViewById(R.id.btPay);
    }

}