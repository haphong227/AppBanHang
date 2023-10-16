package com.example.appbanhang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appbanhang.R;
import com.example.appbanhang.SearchFoodActivity;
import com.example.appbanhang.adapter.CategoryAdapter;
import com.example.appbanhang.adapter.FoodAdapter;
import com.example.appbanhang.adapter.SliderAdapter;
import com.example.appbanhang.model.Category;
import com.example.appbanhang.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    ViewPager2 viewPager;
    SliderAdapter sliderAdapter;
    EditText searchView;
    RecyclerView recyclerView_category, recyclerView_food;
    CategoryAdapter categoryAdapter;
    FoodAdapter foodAdapter;
    ArrayList<Category> dataCategory;
    ArrayList<Food> dataFood = new ArrayList<>();;
    DatabaseReference myCate, myFood;

    //    Slider truot ve anh dau khi o slide cuoi
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = viewPager.getCurrentItem();
            Log.d("sliderrrr", String.valueOf(dataCategory.size()));
            if (currentPosition == dataCategory.size() - 1) {
                viewPager.setCurrentItem(0);
            } else {
                viewPager.setCurrentItem(currentPosition + 1);
            }
        }
    };

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
        viewPager = view.findViewById(R.id.viewPager);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchFoodActivity.class));
            }
        });

        //      3 slide
        viewPager.setOffscreenPageLimit(3);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);

//
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            public void transformPage(View page, float positon) {
                float r = 1 - Math.abs(positon);
                page.setScaleY(0.65f + r * 0.1f);
            }

        });
        viewPager.setPageTransformer(compositePageTransformer);

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

                //                set adapter cho slider
                sliderAdapter = new SliderAdapter(dataCategory);
                viewPager.setAdapter(sliderAdapter);
//                tu chuyen slide sau 3s
                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable, 3000);
                    }
                });

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
