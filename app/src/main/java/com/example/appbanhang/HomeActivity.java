package com.example.appbanhang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appbanhang.adapter.CategoryAdapter;
import com.example.appbanhang.adapter.FoodAdapter;
import com.example.appbanhang.model.Category;
import com.example.appbanhang.model.Food;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements CategoryAdapter.ItemListenerFood,
        FoodAdapter.ItemListener{
    private RecyclerView recyclerView_category, recyclerView_sale, recyclerView_like;
    public CategoryAdapter adapter_cate;
    public FoodAdapter adapter_food;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView_category=findViewById(R.id.recycleView_category);
        recyclerView_sale=findViewById(R.id.recycleView_sale);
        recyclerView_like=findViewById(R.id.recycleView_like);
        adapter_cate=new CategoryAdapter();
        adapter_food=new FoodAdapter();
        adapter_cate.setList(getListFood());
        adapter_food.setList(getListSale());
//        GridLayoutManager manager = new GridLayoutManager(this, 3);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager_sale = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager_like = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView_category.setLayoutManager(manager);
        recyclerView_sale.setLayoutManager(manager_sale);
        recyclerView_like.setLayoutManager(manager_like);
        recyclerView_category.setAdapter(adapter_cate);
        recyclerView_sale.setAdapter(adapter_food);
        adapter_cate.setItemListener(this);
        adapter_food.setItemListener(this);
    }

    private List<Food> getListSale() {
        List<Food> list=new ArrayList<>();
        list.add(new Food(R.drawable.trasua, "30.000", "20.000"));
        list.add(new Food(R.drawable.kem, "10.000", "5.000"));
        list.add(new Food(R.drawable.doanvat, "20.000", "10.000"));
        list.add(new Food(R.drawable.trasua, "30.000", "20.000"));
        list.add(new Food(R.drawable.kem, "15.000", "10.000"));
        return list;
    }

    private List<Category> getListFood() {
        List<Category> list=new ArrayList<>();
        list.add(new Category(R.drawable.trasua, "Trà sữa"));
        list.add(new Category(R.drawable.kem, "Kem"));
        list.add(new Category(R.drawable.doanvat, "Đồ ăn vặt"));
        list.add(new Category(R.drawable.trasua, "Trà sữa"));
        list.add(new Category(R.drawable.img, "Kem"));
        list.add(new Category(R.drawable.img, "Cafe"));
        return list;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, adapter_cate.getItem(position).getName(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, adapter_food.getFood(position).getTxtName(), Toast.LENGTH_SHORT).show();
    }
}