package com.example.appbanhang.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appbanhang.fragment.FragmentCart;
import com.example.appbanhang.fragment.FragmentFood;
import com.example.appbanhang.fragment.FragmentMessage;
import com.example.appbanhang.fragment.FragmentUser;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new FragmentFood();
            case 1:return new FragmentCart();
            case 2:return new FragmentMessage();
            case 3:return new FragmentUser();
        }
        return null;    }

    @Override
    public int getCount() {
        return 4;
    }
}
