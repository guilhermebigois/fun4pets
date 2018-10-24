package com.fiap.guilhermebigois.fun4pets.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fiap.guilhermebigois.fun4pets.R;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class EsperaActivity extends AppCompatActivity {
    private ACProgressFlower progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_espera);

        progress = new ACProgressFlower.Builder(EsperaActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        progress.show();
    }

    // MÉTODO PARA FECHAR ACTIVITY AO SAIR
    protected void onPause() {
        super.onPause();
        progress.dismiss();
        finish();
    }
}
