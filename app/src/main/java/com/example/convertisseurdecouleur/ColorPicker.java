package com.example.convertisseurdecouleur;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ColorPicker extends AppCompatActivity {
    Button btn;
    ConstraintLayout cstrLayout;
    int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorpicker);

        btn = findViewById(R.id.btn);
        cstrLayout = findViewById(R.id.layout);

        defaultColor = ContextCompat.getColor(ColorPicker.this, com.google.android.material.R.color.design_default_color_primary);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
    }

    public void openColorPicker(){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                cstrLayout.setBackgroundColor(defaultColor);
            }
        });
        ambilWarnaDialog.show();
    }

}
