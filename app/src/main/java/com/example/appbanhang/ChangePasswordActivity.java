package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appbanhang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText eNewpass, eOldpass, eConfirm;
    Button btChangepassword;
    ImageView back;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;

    String email, name, pass, phone, address, img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        eOldpass = findViewById(R.id.eOldpass);
        eNewpass = findViewById(R.id.eNewpass);
        eConfirm = findViewById(R.id.eConfirm);
        back = findViewById(R.id.back);
        btChangepassword = findViewById(R.id.btChangepassword);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
            }
        });
        myRef = FirebaseDatabase.getInstance().getReference("User");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btChangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = eOldpass.getText().toString().trim();
                String newPass = eNewpass.getText().toString().trim();
                String confirm = eConfirm.getText().toString().trim();
                if (oldPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
                }
                if (oldPass.length() < 6 || newPass.length() < 6 || confirm.length() < 6) {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu phải có ít nhất 6 ký tự!",
                            Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(ChangePasswordActivity.this, "okiii", Toast.LENGTH_SHORT).show();
                    myRef.child("").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                User user = dataSnapshot1.getValue(User.class);
                                if (user.getToken().equalsIgnoreCase(firebaseUser.getUid())) {
                                    if (!(oldPass.equalsIgnoreCase(user.getPassword()))) {
                                        System.out.println("Mat khau cu: " + user.getPassword() + "\n");
                                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                                    } else {
                                        System.out.println(firebaseUser.getUid() + "\nhihiihihihihihihi \n");
                                        name = user.getName();
                                        address = user.getDiachi();
                                        email = user.getEmail();
                                        phone = user.getPhone();
                                        pass = user.getPassword();
                                        img = user.getImage();
                                        System.out.println(name + ":" + address + ":" + email + ":" + pass + "\n222222222");
                                        if (newPass.equalsIgnoreCase(oldPass)) {
                                            System.out.println("Mật khẩu mới không được trùng mật khẩu cũ\n");
                                            Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới không được trùng mật khẩu cũ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (!(confirm.equalsIgnoreCase(newPass))) {
                                                System.out.println("Mật khẩu mới không khớp\n");
                                                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                                            } else {

                                                User user1 = new User(email, newPass, name, phone, img, address, firebaseUser.getUid());
                                                System.out.println("Mat khau moi:" + newPass + "\n");

                                                FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
                                                String newPassword = eNewpass.getText().toString().trim();

                                                firebaseUser1.updatePassword(newPassword)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    String key = dataSnapshot1.getKey();
                                                                    myRef.child(key).setValue(user1);
                                                                    System.out.println("success\n");
                                                                    Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                }
                                                            }
                                                        });
//                                                startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));

                                            }
                                        }
                                    }

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}