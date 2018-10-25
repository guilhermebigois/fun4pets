package com.fiap.guilhermebigois.fun4pets.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.fiap.guilhermebigois.fun4pets.R;
import com.fiap.guilhermebigois.fun4pets.adapter.AnimalAdapter;
import com.fiap.guilhermebigois.fun4pets.dao.StaticList;
import com.fiap.guilhermebigois.fun4pets.model.Animal;
import com.fiap.guilhermebigois.fun4pets.service.AnimalService;

import java.util.List;

public class AnimalActivity extends AppCompatActivity {
    private FloatingActionButton btnAddAnimal;
    private RecyclerView animaisRecycle;
    private AnimalTask animalTask;
    private List<Animal> animalResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        animaisRecycle = findViewById(R.id.animais_recycle);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        animaisRecycle.setLayoutManager(manager);

        if (StaticList.AccessData.getAnimalList().isEmpty()) {
            Intent intent = new Intent(AnimalActivity.this, EsperaActivity.class);
            startActivity(intent);

            animalTask = new AnimalTask(StaticList.AccessData.getDono().getId());
            animalTask.execute((Void) null);
        }

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

    // MÉTODO PARA FECHAR ACTIVITY AO SAIR
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

    public class AnimalTask extends AsyncTask<Void, Void, Boolean> {
        private final String id;

        AnimalTask(String id) {
            this.id = id;
        }

        // REALIZA PESQUISA EM BACKGROUD
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean status = true;

            // PESQUISA O DONO
            try {
                animalResponse = AnimalService.getAllAnimals();

                if (animalResponse == null) {
                    status = false;
                }
            } catch (Exception e) {
                status = false;
            }

            return status;
        }

        // PROCESSO PÓS-VALIDAÇÃO DE USUÁRIO E SENHA
        @Override
        protected void onPostExecute(final Boolean success) {
            animalTask = null;

            if (success) {
                StaticList.AccessData.setAnimalList(animalResponse);

                Intent intent = new Intent(AnimalActivity.this, AnimalActivity.class);
                startActivity(intent);
            } else {
                ConnectivityManager cm = (ConnectivityManager) AnimalActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                if ((!(netInfo != null)) && !netInfo.isConnected()) {
                    Toast.makeText(AnimalActivity.this, "Não foi retornar a listagem de pets\nVerifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
