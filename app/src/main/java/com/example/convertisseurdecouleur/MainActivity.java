package com.example.convertisseurdecouleur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    //import des fragments


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}