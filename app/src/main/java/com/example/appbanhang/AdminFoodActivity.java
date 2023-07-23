package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.adapter.AdminFoodAdapter;
import com.example.appbanhang.model.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminFoodActivity extends AppCompatActivity {
    Toolbar toolbar;

    RecyclerView recyclerView;
    FloatingActionButton fabutton;
    AdminFoodAdapter foodAdapter;
    ArrayList<Food> dataFood;
    DatabaseReference myFood;
    String idCate = "";

    public String name = "";
    TextView titlePage;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food);
        recyclerView = findViewById(R.id.recyclerViewFood);
        fabutton = findViewById(R.id.fabutton);
        toolbar = findViewById(R.id.toolbar);
        titlePage = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminFoodActivity.this,AdminCategoryActivity.class));
                finish();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(AdminFoodActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        idCate = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        titlePage.setText("Food");

        System.out.println(idCate);
        dataFood = new ArrayList<>();

        foodAdapter = new AdminFoodAdapter(dataFood, AdminFoodActivity.this);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setHasFixedSize(true);

        myFood = FirebaseDatabase.getInstance().getReference("food/");
        myFood.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    dataFood.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Food food = data.getValue(Food.class);
                        if (food.getIdCate().equalsIgnoreCase(idCate)) {
                            dataFood.add(food);
                        }

//                    System.out.println(food.getName()+ ","+food.getPrice()+","+food.getImage()+"\n");
                    }
                    foodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminFoodActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        fabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminFoodActivity.this, AdminNewFoodActivity.class);
                i.putExtra("idCate", idCate);
                i.putExtra("name", name);
                startActivity(i);
            }
        });
    }
}