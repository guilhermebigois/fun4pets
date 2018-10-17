package com.fiap.guilhermebigois.fun4pets.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fiap.guilhermebigois.fun4pets.R;

public class AnimalActivity extends AppCompatActivity {
    private FloatingActionButton btnAddAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        btnAddAnimal = findViewById(R.id.btnAddAnimal);
        btnAddAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnimalActivity.this, AddAnimalActivity.class);
                startActivity(intent);
            }
        });
    }
}
