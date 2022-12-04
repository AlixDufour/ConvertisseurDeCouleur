package com.example.convertisseurdecouleur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button changeImage = findViewById(R.id.buttonImage);
        Button changePicker = findViewById(R.id.buttonPicker);

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent change = new Intent(MainActivity.this, ImagePicker.class);
                startActivity(change);
            }
        });

        changePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent change = new Intent(MainActivity.this, ColorPicker.class);
                startActivity(change);
            }
        });
    }
}