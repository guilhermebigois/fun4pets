package com.fiap.guilhermebigois.fun4pets.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.fiap.guilhermebigois.fun4pets.R;
import com.fiap.guilhermebigois.fun4pets.dao.StaticList;
import com.fiap.guilhermebigois.fun4pets.model.Dono;
import com.fiap.guilhermebigois.fun4pets.service.DonoService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = 2000;

    private GetDonoTask getDonoTask;
    private HashMap<String, String> donoResponse;
    private Intent view;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                String email;
                
                if (checkSharedPreferences()) {
                    SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
                    email = preferences.getString("email", "");

                    // TELA RODANDO
                    intent = new Intent(SplashActivity.this, EsperaActivity.class);
                    startActivity(intent);

                    if (!email.isEmpty()) {
                        getDonoTask = new GetDonoTask(email);
                        getDonoTask.execute((Void) null);
                    } else {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    
    private boolean checkSharedPreferences() {
        boolean isActive = true;
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        
        if (!preferences.contains("ativo")) {
            isActive = false;
        } else if (!preferences.getBoolean("ativo", false)) {
            isActive = false;
        }
        
        return isActive;
    }

    public class GetDonoTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;

        GetDonoTask(String email) {
            mEmail = email;
        }

        // REALIZA PESQUISA EM BACKGROUD
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean status = true;

            // PESQUISA O DONO
            try {
                donoResponse = DonoService.getDonoData(mEmail);

                // VALIDA O SE O DONO ESTÁ CORRETO
                if (!donoResponse.get("code").equals("200")) {
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
            getDonoTask = null;

            if (success) {
                // CPF + NOME + SEXO + NASCIMENTO + EMAIL + TELEFONE + ENDERECO + BAIRRO + MUNICIPIO + ESTADO + CEP + SENHA + COMPLEMENTO + ID
                String cpf = donoResponse.get("cpf");
                String nome = donoResponse.get("nome");
                String email = donoResponse.get("email");
                String telefone = donoResponse.get("telefone");
                String endereco = donoResponse.get("endereco");
                String complemento = donoResponse.get("complemento");
                String bairro = donoResponse.get("bairro");
                String municipio = donoResponse.get("cidade");
                String estado = donoResponse.get("estado");
                String cep = donoResponse.get("cep");
                String senha = donoResponse.get("senha");
                String sexo = donoResponse.get("sexo");
                String strNasc = donoResponse.get("nascimento");
                String id = donoResponse.get("id");

                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                Date dataFormatada = new Date();

                try {
                    dataFormatada = formato.parse(strNasc);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Dono dono = new Dono(cpf, nome, sexo, dataFormatada, email, telefone, endereco, bairro, municipio, estado, cep, senha, complemento, id);
                StaticList.AccessData.setDono(dono);

                Intent intent = new Intent(SplashActivity.this, PrincipalActivity.class);
                startActivity(intent);
            } else {
                ConnectivityManager cm = (ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                if (!(netInfo != null && netInfo.isConnected())) {
                    Toast.makeText(SplashActivity.this, "Não foi possível conectar\nVerifique sua conexão com a internet", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
}