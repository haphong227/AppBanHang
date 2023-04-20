package com.example.appbanhang.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.CategoryAdapter;
import com.example.appbanhang.adapter.FoodAdapter;
import com.example.appbanhang.model.Category;
import com.example.appbanhang.model.Food;

import java.util.ArrayList;
import java.util.List;

public class FragmentFood extends Fragment implements CategoryAdapter.ItemListenerFood, FoodAdapter.ItemListener{
    SearchView searchView;
    RecyclerView recyclerView_category, recyclerView_food, recyclerView_like;
    CategoryAdapter categoryAdapter;
    FoodAdapter foodAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView=view.findViewById(R.id.searchView);
        recyclerView_category=view.findViewById(R.id.recycleView_category);
        recyclerView_food=view.findViewById(R.id.recycleView_food);
        categoryAdapter=new CategoryAdapter();
        foodAdapter=new FoodAdapter();
        categoryAdapter.setList(getListCategory());
        foodAdapter.setList(getListFood());
        LinearLayoutManager manager_category = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        GridLayoutManager manager_food = new GridLayoutManager(getActivity(), 2);
        recyclerView_category.setLayoutManager(manager_category);
        recyclerView_food.setLayoutManager(manager_food);
        recyclerView_category.setHasFixedSize(true);
        recyclerView_food.setHasFixedSize(true);
        recyclerView_category.setAdapter(categoryAdapter);
        recyclerView_food.setAdapter(foodAdapter);
        categoryAdapter.setItemListener(this);
        foodAdapter.setItemListener(this);
    }

    private List<Food> getListFood() {
        List<Food> list=new ArrayList<>();
        list.add(new Food(R.drawable.trasua, "Trà Sữa", "20.000đ"));
        list.add(new Food(R.drawable.kem, "Kem", "5.000đ"));
        list.add(new Food(R.drawable.doanvat, "Bánh tráng trộn", "10.000đ"));
        list.add(new Food(R.drawable.trasua, "Cơm tấm", "20.000đ"));
        list.add(new Food(R.drawable.kem, "Bim bim", "10.000đ"));
        return list;
    }

    private List<Category> getListCategory() {
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
        if(view==recyclerView_category){
            Toast.makeText(getContext(), categoryAdapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
        }
        if(view==recyclerView_food){
            Toast.makeText(getContext(), foodAdapter.getFood(position).getTxtName(), Toast.LENGTH_SHORT).show();
        }
    }
}
