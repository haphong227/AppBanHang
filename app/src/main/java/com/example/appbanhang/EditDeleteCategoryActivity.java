package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditDeleteCategoryActivity extends AppCompatActivity {
    ImageView imgCategory, back;
    EditText idCategory, nameCategory, desCategory;
    Button btUpdate, btDelete;
    private final int PICK_IMAGE_REQUEST = 22;
    private String key="";

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_category);
        initView();
        key=getIntent().getStringExtra("id");
        myRef= FirebaseDatabase.getInstance().getReference().
                child("category").child(key);
        displayCategory();

//        imgCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SelectImage();
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                startActivity(new Intent(EditDeleteCategoryActivity.this, AdminFoodActivity .class));
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCategory();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xoa danh muc");
                builder.setMessage("Co chac muon xoa?");
                builder.setIcon(R.drawable.remove);
                builder.setNegativeButton("Bo qua",null);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCategory();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
    }

    private void updateCategory() {
        String name=nameCategory.getText().toString();
        if(name.isEmpty()){
            Toast.makeText(this, "Không được để trống tên!", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String,Object> category=new HashMap<>();
//            category.put("key",key);
            category.put("name",name);
            myRef.updateChildren(category).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(EditDeleteCategoryActivity.this,
                                "Sửa thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(EditDeleteCategoryActivity.this,AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void deleteCategory() {
        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditDeleteCategoryActivity.this,
                        "Xóa thành công!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(EditDeleteCategoryActivity.this,
                        AdminCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void displayCategory() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    String image = snapshot.child("image").getValue().toString();
                    System.out.println(name + "+" + image);
                    nameCategory.setText(name);
                    Picasso.get().load(image).into(imgCategory);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
//        idCategory = findViewById(R.id.idCategory);
        nameCategory = findViewById(R.id.nameCategory);
        imgCategory = findViewById(R.id.imgCategory);
        back = findViewById(R.id.back);
        btUpdate = findViewById(R.id.btUpdate);
        btDelete = findViewById(R.id.btDelete);
    }

//    private void SelectImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(
//                Intent.createChooser(
//                        intent,
//                        "Select Image from here..."),
//                PICK_IMAGE_REQUEST);
//    }
}