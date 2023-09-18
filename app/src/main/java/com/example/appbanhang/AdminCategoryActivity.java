package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appbanhang.adapter.AdminCategoryAdapter;
import com.example.appbanhang.model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminCategoryActivity extends AppCompatActivity {
    Toolbar toolbar;

    RecyclerView recyclerView;
    FloatingActionButton fabutton;
    AdminCategoryAdapter categoryAdapter;
    ArrayList<Category> dataCategory;
    DatabaseReference myCate;
    ImageView logout;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        recyclerView = findViewById(R.id.recyclerViewCate);
        toolbar = findViewById(R.id.toolbar);
        fabutton = findViewById(R.id.fabutton);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.notification) {
                    startActivity(new Intent(AdminCategoryActivity.this, AdminNotificationActivity.class));
                }
                if (id == R.id.logOut) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    startActivity(new Intent(AdminCategoryActivity.this, LoginActivity.class));
                    finish();
                    Toast.makeText(getBaseContext(), "See you later", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(AdminCategoryActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        myCate = FirebaseDatabase.getInstance().getReference("category");
        myCate.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataCategory = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Category category = data.getValue(Category.class);
                    dataCategory.add(category);
//                    System.out.println(category.getName() + "," + category.getImage() + "\n");
                }
                categoryAdapter = new AdminCategoryAdapter(dataCategory, AdminCategoryActivity.this);
                recyclerView.setAdapter(categoryAdapter);
                recyclerView.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminCategoryActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
        fabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminCategoryActivity.this, AdminNewCategoryActivity.class));
            }
        });
    }
}