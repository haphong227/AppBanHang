package com.example.appbanhang.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//
//import com.example.food.MainActivity;
//import com.example.food.R;
//import com.example.food.ResetpasswordActivity;
//import com.example.food.callback.Usercallback;
//import com.example.food.daofirebase.DaoUser;
//import com.example.food.model.User;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
import com.example.appbanhang.LoginActivity;
import com.example.appbanhang.MainActivity;
import com.example.appbanhang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentLogin extends Fragment {
    EditText eEmail, ePassword;
    TextView tvForgot;
    Button btLogin;
    //    DaoUser daoUser;
//    ArrayList<User> datastore;
//    private FirebaseAuth mAuth;
//    FirebaseDatabase database;
//    DatabaseReference databaseReference;
    FirebaseAuth mFirebaseAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        eEmail = view.findViewById(R.id.eEmail);
        ePassword = view.findViewById(R.id.ePassword);
        tvForgot = view.findViewById(R.id.tvFogort);
        btLogin = view.findViewById(R.id.btLogin);
//        daoStore = new DaoStore(getContext());
//        datastore = new ArrayList<>();
        eEmail.setText("haphong@gmail.com");
        ePassword.setText("22072001");
//        database =FirebaseDatabase.getInstance();
//        databaseReference = database.getReference("User");
//        mAuth = FirebaseAuth.getInstance();

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
//        loginbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                login();
//            }
//        });


        return view;
    }

    //    private void login() {
//
//        final String ussername = email.getText().toString().trim();
//        final String pass1 = pass.getText().toString().trim();
//         if (ussername.isEmpty() || pass1.isEmpty()) {
//            Toast.makeText(getActivity(), "Vui Lòng Nhập Đầy Đủ 2 Trường", Toast.LENGTH_SHORT).show();
//        }  else if (!ussername.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
//            Toast.makeText(getActivity(), "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
//        }else {
//            daoUser = new DaoUser(getContext());
//            datastore = new ArrayList<>();
//            daoUser.getAll(new Usercallback() {
//                @Override
//                public void onSuccess(ArrayList<User> lists) {
//                    datastore.clear();
//                    datastore.addAll(lists);
//
//                }
//
//                @Override
//                public void onError(String message) {
//
//                }
//            });
//            mAuth.signInWithEmailAndPassword(ussername,pass1).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        for (int i = 0; i < datastore.size(); i++) {
//                            if (datastore.get(i).getEmail().equalsIgnoreCase(email.getText().toString()) && datastore.get(i).getPassword().equalsIgnoreCase(pass.getText().toString())) {
//                                Toast.makeText(getActivity(), "Login Thành Công", Toast.LENGTH_SHORT).show();
//                                Intent is = new Intent(getActivity(),MainActivity.class);
//                                startActivity(is);
//                                break;
//                            }
//                            else {
//                                Toast.makeText(getActivity(), "Login Thất Bại", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    } else {
//                        Toast.makeText(getActivity(), "Login Thất Bại", Toast.LENGTH_SHORT).show();
//
////                        Boolean sure = false;
////                        for (int i = 0; i < datastore.size(); i++) {
////                            if (datastore.get(i).getEmail().matches(email.getText().toString()) && datastore.get(i).getPass().matches(pass.getText().toString())) {
////                                sure = true;
////                                Toast.makeText(getActivity(), "Login Thành Công", Toast.LENGTH_SHORT).show();
//////                                    Bundle bundle = new Bundle();
//////                                    bundle.putString("email",email.getText().toString());
//////                                bundle.putString("pass",pass.getText().toString());
//////                                FragmentUpdateProfile fragmentUpdateProfile = new FragmentUpdateProfile();
//////                                fragmentUpdateProfile.setArguments(bundle);
//////                                fragmentUpdateProfile.show(getFragmentManager(),fragmentUpdateProfile.getTag());
////                                Intent is = new Intent(getActivity(),MainActivity.class);
////                                is.putExtra("email",email.getText().toString());
////                                startActivity(is);
////
////
////                                break;
////                            }
////                        }
////                        if (sure == true){
////                            Toast.makeText(getActivity(), "Login Thành Công", Toast.LENGTH_SHORT).show();
//////                            FragmentUpdateProfile fragmentUpdateProfile = new FragmentUpdateProfile();
//////                            FragmentManager fm = getFragmentManager();
//////                            fm.beginTransaction()
//////                                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
//////                                    .show(fragmentUpdateProfile)
//////                                    .commit();
////                        }else {
////                            Toast.makeText(getActivity(), "Login Thất Bại", Toast.LENGTH_SHORT).show();
////
////                        }
//                    }
//
//                }
//            });
//        }
//        }
//    private void setupRatio(BottomSheetDialog bottomSheetDialog) {
//        //id = com.google.android.material.R.id.design_bottom_sheet for Material Components
//        //id = android.support.design.R.id.design_bottom_sheet for support librares
//        FrameLayout bottomSheet = (FrameLayout)
//                bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
//        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
//        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
//        layoutParams.height = getBottomSheetDialogDefaultHeight();
//        bottomSheet.setLayoutParams(layoutParams);
//        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//    }
    private int getBottomSheetDialogDefaultHeight() {
        return getWindowHeight() * 85 / 100;
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}
