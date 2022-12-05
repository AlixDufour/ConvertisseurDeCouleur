
package com.example.convertisseurdecouleur;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import yuku.ambilwarna.AmbilWarnaDialog;

public class ColorPickerFragment extends Fragment {
    int defaultColor;
    Couleur couleur = new Couleur(defaultColor);

    //composants UI
    Button btnPick;
    ImageButton copyRGB, copyHEX, copyHSL, copyHSV;
    View colorView;
    EditText rgbR, rgbG, rgbB, hex, hslH, hslS, hslL, hsvH, hsvS, hsvV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.color_picker_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnPick = view.findViewById(R.id.pickerOpenPaletteToPick);
        colorView = view.findViewById(R.id.pickerCouleurResultat);

        rgbR=view.findViewById(R.id.pickerEditRgbRed);
        rgbG=view.findViewById(R.id.pickerEditRgbGreen);
        rgbB=view.findViewById(R.id.pickerEditRgbBlue);

        hex=view.findViewById(R.id.pickerHexEdit);

        hslH=view.findViewById(R.id.pickerHSLEditH);
        hslS=view.findViewById(R.id.pickerHSLEditS);
        hslL=view.findViewById(R.id.pickerHSLEditL);

        hsvH=view.findViewById(R.id.pickerHSVEditH);
        hsvS=view.findViewById(R.id.pickerHSVEditS);
        hsvV=view.findViewById(R.id.pickerHSVEditV);

        defaultColor = ContextCompat.getColor(getContext(), R.color.white);
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
            }
        });

        btnPick = view.findViewById(R.id.pickerOpenPaletteToPick);
        btnPick.setOnClickListener(view1 -> openColorPicker());

        copyRGB = view.findViewById(R.id.pickerCopyRGB);
        copyRGB.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager)getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", "rgb(" + rgbR.getText() + ", " + rgbG.getText() + ", " + rgbB.getText() + ")");
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Copied to clipboard!", Toast.LENGTH_SHORT).show();
        });
    }

    public void openColorPicker(){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(getContext(), defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {}

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