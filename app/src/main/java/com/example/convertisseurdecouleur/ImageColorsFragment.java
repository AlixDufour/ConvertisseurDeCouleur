package com.example.convertisseurdecouleur;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class ImageColorsFragment extends Fragment {

    private static final int RESULT_LOAD_IMAGE = 1;

    ImageView imageView;
    Button importButton;

    TableLayout colorTable;
    TextView colorTableTitle;

    public ImageColorsFragment() {
        super(R.layout.image_colors_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = (ImageView) getView().findViewById(R.id.imageView);
        importButton = (Button) getView().findViewById(R.id.importButton);

        colorTable = (TableLayout) getView().findViewById(R.id.dominantColorsTable);
        colorTableTitle = (TextView) getView().findViewById(R.id.dominantColorsTitle);

        // Import image
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
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

        // Assign them to the texts and buttons

        // Make the table visible
        colorTableTitle.setVisibility(View.VISIBLE);
        colorTable.setVisibility(View.VISIBLE);
    }
}
