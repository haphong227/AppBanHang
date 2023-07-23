package com.example.appbanhang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appbanhang.ChangePasswordActivity;
import com.example.appbanhang.EditProfileActivity;
import com.example.appbanhang.HistoryOrderActivity;
import com.example.appbanhang.LoginActivity;
import com.example.appbanhang.MainActivity;

import com.example.appbanhang.R;
import com.example.appbanhang.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentUser extends Fragment {
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference mRef;
    CircleImageView img_profile;
    TextView tvUsername, tvEmail, tvEditprofile, tvChangepassword, tvHistory, tvLogout;
    String name, email, img;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        img_profile = view.findViewById(R.id.img_profile);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvEditprofile = view.findViewById(R.id.tvEditprofile);
        tvChangepassword = view.findViewById(R.id.tvChangepassword);
        tvHistory = view.findViewById(R.id.tvHistory);
        tvLogout = view.findViewById(R.id.tvLogout);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            mRef = FirebaseDatabase.getInstance().getReference("User");
            mRef.child("").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        User user = data.getValue(User.class);
                        if (user.getToken().equalsIgnoreCase(firebaseUser.getUid())) {
                            name=user.getName();
                            email=user.getEmail();
                            img=user.getImage();
                        }
                    }
                    tvEmail.setText(email);
                    tvUsername.setText(name);
                    if (img == null) {
                        Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(img_profile);
                    } else if (img != null) {
                        Picasso.get().load(img).into(img_profile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        tvEditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }

        });

        tvChangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
            }
        });

        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), HistoryOrderActivity.class));
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                Toast.makeText(getActivity(), "See you later", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
