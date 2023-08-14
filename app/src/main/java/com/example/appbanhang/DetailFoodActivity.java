package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.adapter.FoodAdapter;
import com.example.appbanhang.model.Food;
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
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class DetailFoodActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    TextView titlePage;
    ImageView img_food, sub, add;
    TextView tvPrice, tvName, tvDes, tvSl, tvQuantity;
    RecyclerView recyclerView_review;
    Button btAdd;
    FoodAdapter adapter;
    ArrayList<Food> dataFood = new ArrayList<>();
    DatabaseReference myFood;
    DatabaseReference databaseReference;
    private FirebaseUser auth;
    String name, idFood, price, des, img, idCate;
    int quantity=0;
    int totalQuantity = 0;
    double totalPrice = 0;
    ArrayList<Food> dsfoodall = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        initView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailFoodActivity.this, MainActivity.class));
                finish();
            }
        });
        sub.setOnClickListener(this);
        add.setOnClickListener(this);
        btAdd.setOnClickListener(this);

        displayDetailFood();
        displayListFood();
    }

    private void displayListFood() {
        LinearLayoutManager manager = new LinearLayoutManager(DetailFoodActivity.this, RecyclerView.HORIZONTAL, false);
        recyclerView_review.setLayoutManager(manager);
        adapter = new FoodAdapter(dataFood, DetailFoodActivity.this);

        myFood = FirebaseDatabase.getInstance().getReference("food");
        myFood.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Food food = data.getValue(Food.class);
                    dsfoodall.add(food);
                    if (food.getIdCate().equalsIgnoreCase(idCate) && !food.getId().equalsIgnoreCase(idFood)) {
                        dataFood.add(food);
                    }
                }
                recyclerView_review.setAdapter(adapter);
                recyclerView_review.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailFoodActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDetailFood() {
        idFood = getIntent().getStringExtra("idFood");
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        des = getIntent().getStringExtra("des");
        img = getIntent().getStringExtra("image");
        idCate = getIntent().getStringExtra("idCate");
        quantity = getIntent().getIntExtra("quantity", 22);
        System.out.println(quantity);
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        tvName.setText(name);
        tvPrice.setText(String.valueOf(decimalFormat.format(Double.parseDouble(price))) + " đ");
        tvDes.setText("Mô tả: " + des);
        if(quantity > 0){
            tvQuantity.setText("Số lượng: "+String.valueOf(quantity));
        }else{
            tvQuantity.setText("SOLD OUT!");
            tvQuantity.setTextColor(Color.RED);
        }
        Picasso.get().load(img).into(img_food);
    }

    @Override
    public void onClick(View view) {
        if (view == sub) {
            if (totalQuantity > 1) {
                totalQuantity--;
                tvSl.setText(String.valueOf(totalQuantity));
                totalPrice = Double.parseDouble(price) * totalQuantity;
            }
        }
        if (view == add) {
            if (totalQuantity < 10) {
                totalQuantity++;
                tvSl.setText(String.valueOf(totalQuantity));
                totalPrice = Double.parseDouble(price) * totalQuantity;
            }
        }
        if (view == btAdd) {
            if (totalQuantity > 0) {
                if(quantity != 0) addToCart();
                else Toast.makeText(this, "Sản phẩm đã hết!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Vui lòng chọn số lượng sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void addToCart() {
        auth = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart/" + auth.getUid());

        final HashMap<String, Object> cart = new HashMap<>();
        cart.put("idFood", idFood);
        cart.put("productImg", img);
        cart.put("productName", name);
        cart.put("productPrice", price);
        cart.put("totalQuantity", tvSl.getText().toString());
        cart.put("totalPrice", String.valueOf(totalPrice));
        databaseReference.child(name).updateChildren(cart)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DetailFoodActivity.this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailFoodActivity.this,
                                    "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        titlePage = findViewById(R.id.toolbar_title);
        img_food = findViewById(R.id.img_food);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDes = findViewById(R.id.tvDes);
        btAdd = findViewById(R.id.btAdd);
        sub = findViewById(R.id.sub);
        add = findViewById(R.id.add);
        tvSl = findViewById(R.id.tvSl);
        tvQuantity = findViewById(R.id.tvQuantity);
        recyclerView_review = findViewById(R.id.recycleView_review);
    }
}