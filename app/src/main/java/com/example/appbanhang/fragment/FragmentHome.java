package com.example.appbanhang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.SearchFoodActivity;
import com.example.appbanhang.adapter.CategoryAdapter;
import com.example.appbanhang.adapter.FoodAdapter;
import com.example.appbanhang.model.Category;
import com.example.appbanhang.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    EditText searchView;
    RecyclerView recyclerView_category, recyclerView_food;
    CategoryAdapter categoryAdapter;
    FoodAdapter foodAdapter;
    ArrayList<Category> dataCategory;
    ArrayList<Food> dataFood;
    DatabaseReference myCate, myFood;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.eSearch);
        recyclerView_category = view.findViewById(R.id.recycleView_category);
        recyclerView_food = view.findViewById(R.id.recycleView_food);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchFoodActivity.class));
            }
        });

        LinearLayoutManager manager_category = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        GridLayoutManager manager_food = new GridLayoutManager(getContext(), 2);
        recyclerView_category.setLayoutManager(manager_category);
        recyclerView_food.setLayoutManager(manager_food);

        myCate = FirebaseDatabase.getInstance().getReference("category");
        myCate.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataCategory = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Category category = data.getValue(Category.class);
                    dataCategory.add(category);
//                    System.out.println(category.getName()+ ","+category.getImage()+"\n");
                }
                categoryAdapter = new CategoryAdapter(dataCategory, getContext());
                recyclerView_category.setAdapter(categoryAdapter);
                recyclerView_category.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        myFood = FirebaseDatabase.getInstance().getReference("food");
        myFood.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataFood = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Food food = data.getValue(Food.class);
                    dataFood.add(food);

//                    System.out.println(food.getName()+ ","+food.getPrice()+","+food.getImage()+"\n");
                }
                foodAdapter = new FoodAdapter(dataFood, getContext());
                recyclerView_food.setAdapter(foodAdapter);
                recyclerView_food.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
