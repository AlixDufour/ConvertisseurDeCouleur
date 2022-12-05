package com.example.convertisseurdecouleur;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ColorPicker extends AppCompatActivity {
    Button btnPick;
    ImageButton copyRGB, copyHEX, copyHSL, copyHSV;
    View colorView;
    int defaultColor;
    Couleur couleur =new Couleur(defaultColor);
    EditText rgbR, rgbG, rgbB, hex, hslH, hslS, hslL, hsvH, hsvS, hsvV;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorpicker);

        btnPick = findViewById(R.id.btn);
        colorView = findViewById(R.id.fond);

        rgbR=findViewById(R.id.editRgbRed);
        rgbG=findViewById(R.id.editRgbGreen);
        rgbB=findViewById(R.id.editRgbBlue);

        hex=findViewById(R.id.hexEdit);

        hslH=findViewById(R.id.editH1);
        hslS=findViewById(R.id.editS1);
        hslL=findViewById(R.id.editL);

        hsvH=findViewById(R.id.editH2);
        hsvS=findViewById(R.id.editS2);
        hsvV=findViewById(R.id.editV);

        defaultColor = ContextCompat.getColor(ColorPicker.this, com.google.android.material.R.color.design_default_color_primary);
        couleur.setColor(Color.WHITE);
        colorView.setBackgroundColor(couleur.getColor());
        updateEditText();

        rgbR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                int i = 0;
                if (!s.toString().isEmpty())
                    i = Integer.parseInt(s.toString());
                if (i > 255) {
                    i=255;
                }
                //couleur.setFromRGB(i,i,i);

                couleur.setFromRGB(i, Integer.parseInt(rgbG.getText().toString()), Integer.parseInt(rgbB.getText().toString()) );
                //updateEditText();
                //colorView.setBackgroundColor(couleur.getColor());
            }
        });

        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });


        copyRGB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ClipboardManager clipboard = (ClipboardManager)getApplicationContext().getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", "rgb(" + rgbR.getText() + ", " + rgbG.getText() + ", " + rgbB.getText() + ")");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied to clipboard!", Toast.LENGTH_SHORT).show();
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
                couleur.setColor(defaultColor);
                colorView.setBackgroundColor(defaultColor);
                updateEditText();
            }
        });
        ambilWarnaDialog.show();
    }

    public void updateEditText(){
        rgbR.setText(Integer.toString(couleur.getRed()));
        rgbG.setText(Integer.toString(couleur.getGreen()));
        rgbB.setText(Integer.toString(couleur.getBlue()));

        hex.setText(couleur.getHexString());

        hslH.setText(String.format("%,.0f", couleur.getHslH()));
        hslS.setText(String.format("%,.0f", couleur.getHslS()));
        hslL.setText(String.format("%,.0f", couleur.getHslL()));

        hsvH.setText(String.format("%,.0f",couleur.getHsvH()));
        hsvS.setText(String.format("%,.0f",couleur.getHsvS()));
        hsvV.setText(String.format("%,.0f",couleur.getHsvV()));

    }

}
