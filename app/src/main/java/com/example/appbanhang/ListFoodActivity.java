package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.adapter.AdminFoodAdapter;
import com.example.appbanhang.adapter.FoodAdapter;
import com.example.appbanhang.model.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    FoodAdapter foodAdapter;
    ArrayList<Food> dataFood;
    DatabaseReference myFood;
    String idCate = "";

    public String name = "";
    TextView titlePage;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewFood);
        titlePage = findViewById(R.id.toolbar_title);
//        back = findViewById(R.id.fBack);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListFoodActivity.this,MainActivity.class));
                finish();
            }
        });

        GridLayoutManager manager = new GridLayoutManager(ListFoodActivity.this, 2);
        recyclerView.setLayoutManager(manager);

        idCate = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
//        titlePage.setText(name);

        System.out.println(idCate);
        dataFood = new ArrayList<>();

        foodAdapter = new FoodAdapter(dataFood, ListFoodActivity.this);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setHasFixedSize(true);

        myFood = FirebaseDatabase.getInstance().getReference("food");
        myFood.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dataFood.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Food food = data.getValue(Food.class);
                        if (food.getIdCate().equalsIgnoreCase(idCate) ) {
                            dataFood.add(food);
                        }
                    }
                    foodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListFoodActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });

    }
}