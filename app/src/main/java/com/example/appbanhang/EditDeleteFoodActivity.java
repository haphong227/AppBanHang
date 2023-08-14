package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class EditDeleteFoodActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageView imgFood, back;
    EditText idFood, nameFood, price, desFood, quantity;
    Button btUpdate, btDelete;
    private String key="";
    private DatabaseReference myRef;
    String idCate, nameCate="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_food);
        initView();
        idCate=getIntent().getStringExtra("idCate");
        key=getIntent().getStringExtra("id");
        myRef= FirebaseDatabase.getInstance().getReference().child("food").child(key);
        displayFood();
//        imgFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SelectImage();
//            }
//        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditDeleteFoodActivity.this, AdminFoodActivity.class);
                i.putExtra("id", idCate);
                finish();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFood();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xóa món ăn");
                builder.setMessage("Bạn có chắc muốn xóa?");
                builder.setIcon(R.drawable.remove);
                builder.setNegativeButton("Bỏ qua",null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteFood();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
    }

    private void deleteFood() {
        myRef.child("").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditDeleteFoodActivity.this,
                        "Xóa thành công!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditDeleteFoodActivity.this, AdminFoodActivity.class);
                i.putExtra("id", idCate);
                i.putExtra("name", nameCate);
                startActivity(i);
                finish();
            }
        });
    }

    private void editFood() {
        String name=nameFood.getText().toString();
        String desc=desFood.getText().toString();
        String priceFood=price.getText().toString();
        int sl= Integer.parseInt(quantity.getText().toString());
        if(name.isEmpty()){
            Toast.makeText(this, "Không được để trống tên!", Toast.LENGTH_SHORT).show();
        }else if(desc.isEmpty()){
            Toast.makeText(this, "Không được để trống miêu tả!", Toast.LENGTH_SHORT).show();
        }else if(priceFood.isEmpty()){
            Toast.makeText(this, "Không được để trống giá!", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String,Object> food=new HashMap<>();
            food.put("name",name);
            food.put("des",desc);
            food.put("price",priceFood);
            food.put("quantity",sl);
            myRef.child("").updateChildren(food).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(EditDeleteFoodActivity.this,
                                "Sửa thành công!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(EditDeleteFoodActivity.this, AdminFoodActivity.class);
                        i.putExtra("id", idCate);
                        i.putExtra("name", nameCate);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
    }

    private void displayFood() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name=snapshot.child("name").getValue().toString();
                    String des=snapshot.child("des").getValue().toString();
                    String priceFood=snapshot.child("price").getValue().toString();
                    String quantityFood = snapshot.child("quantity").getValue().toString();
                    String image=snapshot.child("image").getValue().toString();
                    System.out.println(name +"+" + des +"+" + priceFood +"+" +image+"q"+quantityFood);
                    nameFood.setText(name);
                    desFood.setText(des);
                    price.setText(priceFood);
                    quantity.setText(quantityFood);
                    Picasso.get().load(image).into(imgFood);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    private void initView() {
        nameFood = findViewById(R.id.nameFood);
        price = findViewById(R.id.price);
        desFood = findViewById(R.id.desFood);
        imgFood = findViewById(R.id.imgFood);
        quantity = findViewById(R.id.quantity);
        back = findViewById(R.id.back);
        btUpdate = findViewById(R.id.btUpdate);
        btDelete = findViewById(R.id.btDelete);
        toolbar = findViewById(R.id.toolbar);
    }
}