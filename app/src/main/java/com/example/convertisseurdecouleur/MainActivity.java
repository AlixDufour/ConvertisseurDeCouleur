package com.example.convertisseurdecouleur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    //import des fragments


    private static final int PERMISSIONS_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verification qu'on a la permission d'ouvrir le stockage
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Demande de permission sinon
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);
        }

        // Dynamic fragment switch
        //if (savedInstanceState == null) {
        //    getSupportFragmentManager().beginTransaction()
        //            .setReorderingAllowed(true)
        //            .add(R.id.fragment_container_view, ImageColorsFragment.class, null)
        //            .commit();
        //}
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
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //fragment de base (surement convertisseur)
        //getSupportFragmentManager().beginTransaction().replace(R.id.container, 'fragment').commit();

        /*
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case fragment convert :
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, 'convert').commit();
                        return true;
                    case fragment picker :
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, 'picker').commit();
                        return true;
                    case fragment image color :
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, 'image color').commit();
                        return true;
                }
                return false;
            }
        });*/
    }

    // Toaster for permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

}