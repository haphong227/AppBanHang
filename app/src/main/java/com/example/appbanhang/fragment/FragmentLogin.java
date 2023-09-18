package com.example.appbanhang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appbanhang.AdminCategoryActivity;
import com.example.appbanhang.MainActivity;
import com.example.appbanhang.R;
import com.example.appbanhang.ResetPasswordActivity;
import com.example.appbanhang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;



public class FragmentLogin extends Fragment {
    EditText eEmail, ePassword;
    TextView tvForgot;
    Button btLogin;
    ArrayList<User> datastore;
    private FirebaseAuth mAuth;
    DatabaseReference database;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        eEmail = view.findViewById(R.id.eEmail);
        ePassword = view.findViewById(R.id.ePassword);
        tvForgot = view.findViewById(R.id.tvFogort);
        btLogin = view.findViewById(R.id.btLogin);
        eEmail.setText("hang2207@gmail.com");
//        eEmail.setText("admin@gmail.com");
        ePassword.setText("22072001");

        database = FirebaseDatabase.getInstance().getReference();
        databaseReference = database.child("User");
        mAuth = FirebaseAuth.getInstance();

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ResetPasswordActivity.class));
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        return view;
    }

    private void login() {
        final String email = eEmail.getText().toString().trim();
        final String password = ePassword.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Vui Lòng Nhập Đầy Đủ 2 Trường", Toast.LENGTH_SHORT).show();
        } else if (!email.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
            Toast.makeText(getActivity(), "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent;
                        if(email.equalsIgnoreCase("admin@gmail.com")){
                            intent = new Intent(getActivity(), AdminCategoryActivity.class);
                        }else{
                            intent = new Intent(getActivity(), MainActivity.class);
                        }
                        intent.putExtra("email", eEmail.getText().toString().trim());
                        startActivity(intent);

                    } else {
                        Toast.makeText(getActivity(), "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
