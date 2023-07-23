package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class AdminNewCategoryActivity extends AppCompatActivity {
    private static final String TAG = "To";
    private String saveCurDate, saveCurTime;
    private final static int galleryPick = 1;
    private String downloadImgUrl, randomKey;

    ImageView imgCategory, back;
    EditText idCategory, nameCategory, desCategory;
    Button btAdd;
    String name;
    private Uri imageUri;
    private StorageReference imgRef;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_category);
        initView();
        imgRef = FirebaseStorage.getInstance().getReference().child("ImageCategory");
        myRef = FirebaseDatabase.getInstance().getReference().child("category");

        imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminNewCategoryActivity.this, AdminCategoryActivity.class));
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
        name = nameCategory.getText().toString().trim();
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
                Toast.makeText(AdminNewCategoryActivity.this,
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
                        saveCategorytoDatabas();
                    }
                });
            }
        });
    }

    private void saveCategorytoDatabas() {
        HashMap<String, Object> category = new HashMap<>();
        category.put("id", TAG + randomKey);
        category.put("name", name);
        category.put("image", downloadImgUrl);
        myRef.child(TAG + randomKey).updateChildren(category)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Intent intent1 = new Intent(AdminNewCategoryActivity.this, AdminCategoryActivity.class);
                            startActivity(intent1);
                            Toast.makeText(AdminNewCategoryActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminNewCategoryActivity.this,
                                    "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initView() {
//        idCategory=findViewById(R.id.idCategory);
        nameCategory = findViewById(R.id.nameCategory);
        imgCategory = findViewById(R.id.imgCategory);
        back = findViewById(R.id.back);
        btAdd = findViewById(R.id.btAdd);
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
            imgCategory.setImageURI(imageUri);
        }
    }
}