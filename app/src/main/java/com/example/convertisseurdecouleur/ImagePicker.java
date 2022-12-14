package com.example.convertisseurdecouleur;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

public class ImagePicker extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.imgColorPicker);
        RedColor = findViewById(R.id.editRedColor);
        GreenColor = findViewById(R.id.editGreenColor);
        BlueColor = findViewById(R.id.editBlueColor);
        HexColor = findViewById(R.id.editHexColor);
        copyRGB = findViewById(R.id.btnCopyRGB);
        copyHEX = findViewById(R.id.btnCopyHex);
        btnChangeImage = findViewById(R.id.btnChangeImage);
        colorView = findViewById(R.id.displayColor);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache(true);

        ActivityResultLauncher<Intent> launcherGetImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData(); //on r??cup??re les donn??es qu'on a ??t?? chercher
                        try {
                            //on met notre image dans la vue de l'activit??
                            InputStream inputStream = getContentResolver().openInputStream(data.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imageView.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

        btnChangeImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  //on lance une activit?? qui va aller chercher une donn??e
            intent.setType("image/*");  //on pr??cise qu'on veut une image
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
                ClipboardManager clipboard = (ClipboardManager)getApplicationContext().getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", "rgb(" + RedColor.getText() + ", " + GreenColor.getText() + ", " + BlueColor.getText() + ")");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ImagePicker.this, "Copied to clipboard!", Toast.LENGTH_LONG).show();
            }
        });

        copyHEX.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ClipboardManager clipboard = (ClipboardManager)getApplicationContext().getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", HexColor.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ImagePicker.this, "Copied to clipboard!", Toast.LENGTH_LONG).show();

            }
        });
    }
}
