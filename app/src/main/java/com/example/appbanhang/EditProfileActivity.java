package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appbanhang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    public static ImageView back;

    EditText eName, ePhone, eEmail, eAddress;
    Button btUpdateprofile;
    CircleImageView imgprofile;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    String pass;

    FirebaseUser firebaseUser;
    String mail, name, phone, address, img;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        back = findViewById(R.id.back);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
            }
        });
        eName = findViewById(R.id.eName);
        ePhone = findViewById(R.id.ePhone);
        eEmail = findViewById(R.id.eEmail);
        eAddress = findViewById(R.id.eAddress);
        imgprofile = findViewById(R.id.img);
        btUpdateprofile = findViewById(R.id.btUpdateprofile);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<User> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    if (user.getToken().equalsIgnoreCase(firebaseUser.getUid())) {
                        System.out.println(firebaseUser.getUid() + "\nhihihihihihihihi \n");
                        name = user.getName();
                        address = user.getDiachi();
                        mail = user.getEmail();
                        phone = user.getPhone();
                        pass = user.getPassword();
                        img = user.getImage();
                        System.out.println("ảnh:     "+user.getImage());
                    }
                }
                eAddress.setText(address);
                eEmail.setText(mail);
                eName.setText(name);
                ePhone.setText(phone);
                if (img == null) {
                    Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(imgprofile);
                } else if (img != null) {
                    Picasso.get().load(img).into(imgprofile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        btUpdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    String email = eEmail.getText().toString().trim();
                    String phone = ePhone.getText().toString().trim();
                    String name = eName.getText().toString().trim();
                    String address = eAddress.getText().toString().trim();
                    if (email.isEmpty() || phone.isEmpty() || name.isEmpty() || address.isEmpty()) {
                        Toast.makeText(EditProfileActivity.this, "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                    } else if (!email.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
                        Toast.makeText(EditProfileActivity.this, "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                    } else if (phone.length() < 10 || phone.length() > 12) {
                        Toast.makeText(EditProfileActivity.this, "Vui lòng nhập đúng số điện thoại!", Toast.LENGTH_SHORT).show();
                    } else {
                        change();
                    }
                } else {
                    String email = eEmail.getText().toString().trim();
                    String phone = ePhone.getText().toString().trim();
                    String name = eName.getText().toString().trim();
                    String address = eAddress.getText().toString().trim();
                    if (email.isEmpty() || phone.isEmpty() || name.isEmpty() || address.isEmpty()) {
                        Toast.makeText(EditProfileActivity.this, "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                    } else if (!email.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
                        Toast.makeText(EditProfileActivity.this, "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                    } else if (phone.length() < 10 || phone.length() > 12) {
                        Toast.makeText(EditProfileActivity.this, "Vui lòng nhập đúng số điện thoại!", Toast.LENGTH_SHORT).show();
                    } else {
                        User user = new User();
                        user.setEmail(eEmail.getText().toString());
                        user.setName(eName.getText().toString());
                        user.setPhone(ePhone.getText().toString());
                        user.setDiachi(eAddress.getText().toString());
                        user.setPassword(pass);
                        user.setImage(img);
                        user.setToken(firebaseUser.getUid());
                        System.out.println("ảnh nè: " + user.getImage());
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot.child("email").getValue(String.class).equalsIgnoreCase(user.getEmail())) {
                                        String key = dataSnapshot.getKey();
                                        mRef.child(key).setValue(user);
                                        Toast.makeText(getBaseContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
//                        startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                    }
                }


            }
        });
    }

    private void change() {

        final StorageReference imageFolder = storageReference.child("ImageProfile/" + UUID.randomUUID().toString());
        imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        User user = new User();
                        user.setEmail(eEmail.getText().toString());
                        user.setName(eName.getText().toString());
                        user.setPhone(ePhone.getText().toString());
                        user.setDiachi(eAddress.getText().toString());
                        user.setPassword(pass);
                        user.setImage(uri.toString());
                        user.setToken(firebaseUser.getUid());
                        System.out.println("ảnh nè hihihhihihi: " + user.getImage());
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot.child("email").getValue(String.class).equalsIgnoreCase(user.getEmail())) {
                                        String key = dataSnapshot.getKey();
                                        mRef.child(key).setValue(user);
                                        Toast.makeText(getBaseContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
//                        startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getBaseContext().getContentResolver(),
                                filePath);
                imgprofile.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}