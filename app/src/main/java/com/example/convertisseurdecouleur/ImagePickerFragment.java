package com.example.convertisseurdecouleur;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImagePickerFragment extends Fragment {

    ImageView imageView;
    TextView RedColor;
    TextView GreenColor;
    TextView BlueColor;
    TextView HexColor;
    Button btnChangeImage;
    ImageButton copyRGB;
    ImageButton copyHEX;
    View colorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.image_picker_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.imgColorPicker);
        RedColor = view.findViewById(R.id.imagepickRedValue);
        GreenColor = view.findViewById(R.id.imagepickGreenValue);
        BlueColor = view.findViewById(R.id.imagepickBlueValue);
        HexColor = view.findViewById(R.id.imagepickHexValue);
        copyRGB = view.findViewById(R.id.pickerCopyRGB);
        copyHEX = view.findViewById(R.id.btnCopyHex);
        btnChangeImage = view.findViewById(R.id.btnChangeImage);
        colorView = view.findViewById(R.id.displayColor);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache(true);

        ActivityResultLauncher<Intent> launcherGetImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData(); //on récupère les données qu'on a été chercher
                        try {
                            //on met notre image dans la vue de l'activité
                            InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imageView.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

        btnChangeImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  //on lance une activité qui va aller chercher une donnée
            intent.setType("image/*");  //on précise qu'on veut une image
            launcherGetImage.launch(intent);
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    Bitmap bitmap = imageView.getDrawingCache();
                    int pixel = bitmap.getPixel((int) event.getX(), (int) event.getY());

                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);

                    colorView.setBackgroundColor(Color.rgb(r, g, b));
                    RedColor.setText(Integer.toString(r));
                    GreenColor.setText(Integer.toString(g));
                    BlueColor.setText(Integer.toString(b));

                    String hex = "#" + Integer.toHexString(pixel);
                    HexColor.setText(hex);
                }
                return true;
            }
        });

        copyRGB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ClipboardManager clipboard = (ClipboardManager)getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", "rgb(" + RedColor.getText() + ", " + GreenColor.getText() + ", " + BlueColor.getText() + ")");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copied to clipboard!", Toast.LENGTH_LONG).show();
            }
        });

        copyHEX.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ClipboardManager clipboard = (ClipboardManager)getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", HexColor.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copied to clipboard!", Toast.LENGTH_LONG).show();

            }
        });
    }
}
