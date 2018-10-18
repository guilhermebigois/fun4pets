package com.fiap.guilhermebigois.fun4pets.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fiap.guilhermebigois.fun4pets.R;
import com.fiap.guilhermebigois.fun4pets.adapter.AnimalAdapter;
import com.fiap.guilhermebigois.fun4pets.dao.StaticList;

public class AnimalActivity extends AppCompatActivity {
    private FloatingActionButton btnAddAnimal;
    private RecyclerView animaisRecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        animaisRecycle = findViewById(R.id.animais_recycle);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        animaisRecycle.setLayoutManager(manager);

        AnimalAdapter animalAdapter = new AnimalAdapter(StaticList.AccessData.getAnimalList());
        animalAdapter.notifyDataSetChanged();
        animaisRecycle.setAdapter(animalAdapter);

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
