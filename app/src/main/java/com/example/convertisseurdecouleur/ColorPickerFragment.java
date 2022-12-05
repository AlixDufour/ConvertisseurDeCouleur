
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

import java.util.regex.Pattern;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ColorPickerFragment extends Fragment {
    int defaultColor;
    Couleur couleur = new Couleur(defaultColor);

    //composants UI
    Button btnPick;
    ImageButton copyRGB, copyHEX, copyHSL, copyHSV;
    View colorView;
    EditText rgbR, rgbG, rgbB, hex, hslH, hslS, hslL, hsvH, hsvS, hsvV;

    //pour trier les changements dans les edits text entre ceux de l'utilisateur et ceux du programme
    boolean canlisteninput = true;

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

        defaultColor = ContextCompat.getColor(getContext(), R.color.background_icon_selected);

        couleur.setColor(defaultColor);
        updateEditText();

        View.OnFocusChangeListener focusChangeListener = (v, b) -> {
            if (rgbR == v || rgbG == v || rgbB == v) {
                if(rgbR.getText().toString().matches(""))
                    rgbR.setText("0");
                if(rgbB.getText().toString().matches(""))
                    rgbB.setText("0");
                if(rgbG.getText().toString().matches(""))
                    rgbG.setText("0");
                couleur.setFromRGB(Integer.parseInt(rgbR.getText().toString()), Integer.parseInt(rgbG.getText().toString()), Integer.parseInt(rgbB.getText().toString()));
            } else if (hsvH == v || hsvS == v || hsvV == v) {
                if(hsvH.getText().toString().matches(""))
                    hsvH.setText("0");
                if(hsvS.getText().toString().matches(""))
                    hsvS.setText("0");
                if(hsvV.getText().toString().matches(""))
                    hsvV.setText("0");
                couleur.setFromHSV(Integer.parseInt(hsvH.getText().toString()), Integer.parseInt(hsvS.getText().toString()), Integer.parseInt(hsvV.getText().toString()));
            }else if (hslH == v || hslS == v || hslL == v) {
                if(hslH.getText().toString().matches(""))
                    hslH.setText("0");
                if(hslS.getText().toString().matches(""))
                    hslS.setText("0");
                if(hslL.getText().toString().matches(""))
                    hslL.setText("0");
                couleur.setFromHSL(Integer.parseInt(hslH.getText().toString()), Integer.parseInt(hslS.getText().toString()), Integer.parseInt(hslL.getText().toString()));
            }else {
                if (!hex.getText().toString().matches("[a-f0-9A-F]{6}")) {
                    hex.setText("FFFFFF");
                    Toast.makeText(getContext(),"Incorrect HEX format", Toast.LENGTH_SHORT).show();
                }
                couleur.setFromStringHEX(hex.getText().toString());
            }
            updateEditText();
        };


        rgbR.setOnFocusChangeListener(focusChangeListener);
        rgbG.setOnFocusChangeListener(focusChangeListener);
        rgbB.setOnFocusChangeListener(focusChangeListener);

        hsvH.setOnFocusChangeListener(focusChangeListener);
        hsvS.setOnFocusChangeListener(focusChangeListener);
        hsvV.setOnFocusChangeListener(focusChangeListener);

        hslH.setOnFocusChangeListener(focusChangeListener);
        hslS.setOnFocusChangeListener(focusChangeListener);
        hslL.setOnFocusChangeListener(focusChangeListener);

        hex.setOnFocusChangeListener(focusChangeListener);

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

        colorView.setBackgroundColor(couleur.getColor());
    }

}