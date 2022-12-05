package com.example.convertisseurdecouleur;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class ImageColorsFragment extends Fragment {

    private static final int RESULT_LOAD_IMAGE = 1;

    ImageView imageView;
    Button importButton;

    TableLayout colorTable;
    TextView colorTableTitle;

    TextView colorValue1, colorValue2, colorValue3, colorValue4;
    View colorBox1, colorBox2, colorBox3, colorBox4;
    Button colorButton1, colorButton2, colorButton3, colorButton4;

    public ImageColorsFragment() {
        super(R.layout.image_colors_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView =  view.findViewById(R.id.imageView);
        importButton =  view.findViewById(R.id.importButton);

        colorTable = view.findViewById(R.id.dominantColorsTable);
        colorTableTitle =  view.findViewById(R.id.dominantColorsTitle);

        // DOMINANT COLORS
        colorValue1 = view.findViewById(R.id.color1Value);
        colorValue2 = view.findViewById(R.id.color2Value);
        colorValue3 = view.findViewById(R.id.color3Value);
        colorValue4 = view.findViewById(R.id.color4Value);
        colorBox1 = view.findViewById(R.id.colorDominant1);
        colorBox2 = view.findViewById(R.id.colorDominant2);
        colorBox3 = view.findViewById(R.id.colorDominant3);
        colorBox4 = view.findViewById(R.id.colorDominant4);
        colorButton1 = view.findViewById(R.id.colorDominant1Text);
        colorButton2 = view.findViewById(R.id.colorDominant2Text);
        colorButton3 = view.findViewById(R.id.colorDominant3Text);
        colorButton4 = view.findViewById(R.id.colorDominant4Text);

        // Import image
        importButton.setOnClickListener(view1 -> {
            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        });

        colorButton1.setOnClickListener(view12 -> copyColorFromNumber(1));
        colorButton2.setOnClickListener(view13 -> copyColorFromNumber(2));
        colorButton3.setOnClickListener(view14 -> copyColorFromNumber(3));
        colorButton4.setOnClickListener(view15 -> copyColorFromNumber(4));
    }

    // Replace image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("BOOP");
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if(resultCode == getActivity().RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    imageView.setImageBitmap(bitmap);
                    updateDominantColors(bitmap);
                }
        }
    }

    protected void updateDominantColors(Bitmap bitmap) {

        // Compute the dominant colors

        // Optimized finder
        List<String> dominantColors = DominantColorFinderOpti.getHexColors(bitmap);

        // Non-optimized finder (not working)
        //DominantColorFinder colorFinder = new DominantColorFinder(bitmap);
        //List<String> dominantColors = colorFinder.getDominantColors();

        colorValue1.setText((dominantColors.get(0)).toUpperCase());
        colorBox1.setBackgroundColor(Color.parseColor(dominantColors.get(0)));

        colorValue2.setText((dominantColors.get(1)).toUpperCase());
        colorBox2.setBackgroundColor(Color.parseColor(dominantColors.get(1)));

        colorValue3.setText((dominantColors.get(2)).toUpperCase());
        colorBox3.setBackgroundColor(Color.parseColor(dominantColors.get(2)));

        colorValue4.setText((dominantColors.get(3)).toUpperCase());
        colorBox4.setBackgroundColor(Color.parseColor(dominantColors.get(3)));

        // DEPRECATED
        //DominantColorFinder colorFinder = new DominantColorFinder(bitmap);
        //HashMap<Integer, Color> dominantColors = colorFinder.getDominantColors();

        // Assign them to the texts and buttons
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            colorValue1.setText(String.valueOf(dominantColors.get(1).toArgb()));
            colorBox1.setBackgroundColor(dominantColors.get(1).toArgb());

            //colorValue2.setText(String.valueOf(dominantColors.get(2).toArgb()));
            //colorBox2.setBackgroundColor(dominantColors.get(2).toArgb());

            //colorValue3.setText(String.valueOf(dominantColors.get(3).toArgb()));
            //colorBox3.setBackgroundColor(dominantColors.get(3).toArgb());
        }*/

        // Make the table visible
        colorTableTitle.setVisibility(View.VISIBLE);
        colorTable.setVisibility(View.VISIBLE);
    }

    public void copyColorFromNumber(int colorNumber) {
        switch (colorNumber) {
            case 1:
                copyColor(colorValue1.getText()); break;
            case 2:
                copyColor(colorValue2.getText()); break;
            case 3:
                copyColor(colorValue3.getText()); break;
            case 4:
                copyColor(colorValue4.getText()); break;
        }
    }

    protected void copyColor(CharSequence text) {
        String value = String.valueOf(text);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(getContext(), ClipboardManager.class);
        ClipData clip = ClipData.newPlainText("Dominant color", value);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Color copied to clipboard!", Toast.LENGTH_SHORT).show();
    }
}
