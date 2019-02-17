package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayFruitActivity extends AppCompatActivity {

    TextView fruitTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_fruit);
        String fruit = getIntent().getStringExtra("message_key");
        fruitTextView = findViewById(R.id.textView);
        fruitTextView.setText(fruit);
    }
}
