package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminNewFoodActivity extends AppCompatActivity {
    Toolbar toolbar;
    private static final String TAG = "To";
    private String saveCurDate, saveCurTime;
    private final static int galleryPick = 1;
    String name, des, priceFood;
    private String downloadImgUrl, randomKey;
    ImageView imgFood, back;
    EditText idFood, nameFood, price, desFood;
    Button btAdd;
    private Uri imageUri;
    private StorageReference imgRef;
    private DatabaseReference myRef;
    String idCate, nameCate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_food);
        initView();

        idCate=getIntent().getStringExtra("idCate");
        nameCate=getIntent().getStringExtra("name");

        imgRef = FirebaseStorage.getInstance().getReference().child("ImageFood");
        myRef = FirebaseDatabase.getInstance().getReference().child("food");

        imgFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminNewFoodActivity.this, AdminFoodActivity.class);
                i.putExtra("id", idCate);
                i.putExtra("name", nameCate);
                finish();
            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }
    private void validateData() {
        name = nameFood.getText().toString().trim();
        des = desFood.getText().toString().trim();
        priceFood = price.getText().toString().trim();
        if (imageUri == null) {
            Toast.makeText(this, "Chọn ảnh", Toast.LENGTH_SHORT).show();
        } else if (name.isEmpty()) {
            Toast.makeText(this, "Nhập tên", Toast.LENGTH_SHORT).show();
        } else {
            storeCategory();
        }
    }
    private void storeCategory() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat curDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurDate = curDate.format(c.getTime());
        SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
        saveCurTime = curTime.format(c.getTime());
        randomKey = saveCurDate + "-" + saveCurTime;
        StorageReference imageFolder = imgRef.child(imageUri.getLastPathSegment() + randomKey + ".jpg");
        final UploadTask uploadTask = imageFolder.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminNewFoodActivity.this,
                        "error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AdminNewCategoryActivity.this, "up anh thanh cong!", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImgUrl = imageFolder.getDownloadUrl().toString();
                        return imageFolder.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        downloadImgUrl = task.getResult().toString();
//                        Toast.makeText(AdminNewCategoryActivity.this, "Luu Url anh thanh cong!", Toast.LENGTH_SHORT).show();
                        saveFoodtoDatabas();
                    }
                });
            }
        });
    }

    private void saveFoodtoDatabas() {
        HashMap<String, Object> food = new HashMap<>();
        food.put("id", TAG + randomKey);
        food.put("idCate", idCate);
        food.put("name", name);
        food.put("des", des);
        food.put("price", priceFood);
        food.put("image", downloadImgUrl);
        myRef.child(TAG + randomKey).updateChildren(food)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminNewFoodActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AdminNewFoodActivity.this, AdminFoodActivity.class);
                            i.putExtra("id", idCate);
                            i.putExtra("name", nameCate);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(AdminNewFoodActivity.this,
                                    "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imgFood.setImageURI(imageUri);
        }
    }
    private void initView() {
        nameFood=findViewById(R.id.nameFood);
        price=findViewById(R.id.price);
        desFood=findViewById(R.id.desFood);
        imgFood=findViewById(R.id.imgFood);
        back=findViewById(R.id.back);
        btAdd=findViewById(R.id.btAdd);
        toolbar = findViewById(R.id.toolbar);
    }
}