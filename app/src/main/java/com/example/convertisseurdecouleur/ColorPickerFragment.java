package com.example.convertisseurdecouleur;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ColorPickerFragment extends Fragment {
    Button btn;
    View colorView;
    int defaultColor;


    public ColorPickerFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn = view.findViewById(R.id.btn);
        colorView = view.findViewById(R.id.viewPicker);

        defaultColor = ContextCompat.getColor(getContext(), com.google.android.material.R.color.design_default_color_primary);

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
                colorView.setBackgroundColor(defaultColor);
            }
        });
        ambilWarnaDialog.show();
    }

}
