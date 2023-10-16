package com.example.appbanhang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tvNote;
    Button btAdd;
    String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initView();
        btAdd.setOnClickListener(this);
        note = getIntent().getStringExtra("note");
        tvNote.setText(note);
    }


    @Override
    public void onClick(View view) {
        if (view == btAdd){
            String n = tvNote.getText().toString();
            Intent i = new Intent(NoteActivity.this, PaymentActivity.class);
            i.putExtra("note", n);
            startActivity(i);
        }
    }
    private void initView() {
        tvNote = findViewById(R.id.tvNote);
        btAdd = findViewById(R.id.btAdd);
    }

}