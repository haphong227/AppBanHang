package com.example.appbanhang;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btRegister, btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        btLogin=findViewById(R.id.btLogin);
        btRegister=findViewById(R.id.btRegister);
        btLogin.setOnClickListener(this);
        btRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==btLogin){
            Intent intent=new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        if(view==btRegister){
            Intent intent=new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}