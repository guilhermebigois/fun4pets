package com.fiap.guilhermebigois.fun4pets.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fiap.guilhermebigois.fun4pets.R;
import com.fiap.guilhermebigois.fun4pets.dao.StaticList;
import com.fiap.guilhermebigois.fun4pets.model.Dono;
import com.fiap.guilhermebigois.fun4pets.service.AuthService;
import com.fiap.guilhermebigois.fun4pets.service.DonoService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class LoginActivity extends AppCompatActivity {
    private static final String DONO_URL = "https://curious-badger-81660-dev-ed.my.salesforce.com/services/data/v43.0/sobjects/Dono__c/Email__c/";
    
    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private String bearer;
    private HashMap<String, String> donoResponse;
    private ACProgressFlower progress;
    private Boolean connection;
    
    // MÉTODO PRINCIPAL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // INICIA VALORES NAS VARIÁVEIS
        mEmailView = findViewById(R.id.email);
        mEmailView.setText("juliafernandes@gmail.com");
        
        // INICIA VALORES NAS VARIÁVEIS
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setText("juliafernandes");
        
        // AÇÃO DO BOTÃO "ENTRAR"
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        
        // AÇÃO DO BOTÃO "REGISTRAR"
        Button mEmailRegisterButton = findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
                startActivity(intent);
            }
        });
    }
    
    // EFETUA A TENTATIVA DE LOGIN
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        
        // LIMPA OS ERROS EM MEMÓRIA
        mEmailView.setError(null);
        mPasswordView.setError(null);
        
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        
        boolean cancel = false;
        View focusView = null;
        
        // VERIFICA SE A SENHA ESTÁ CORRETA, CASO ESTEJA PREENCHIDA
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Este campo é obrigatório");
            focusView = mPasswordView;
            cancel = true;
        }
        
        // VERIFICA SE O E-MAIL ESTÁ CORRETO, CASO ESTEJA PREENCHIDO
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Este campo é obrigatório");
            focusView = mEmailView;
            cancel = true;
        } else if (!email.contains("@") && !email.contains(".com")) {
            mEmailView.setError("Informe um e-mail válido");
            focusView = mEmailView;
            cancel = true;
        }
        
        // CASO VÁLIDO, CONTINUA
        if (cancel) {
            focusView.requestFocus();
        } else {
            progress = new ACProgressFlower.Builder(LoginActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .fadeColor(Color.DKGRAY).build();
            progress.show();
            
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }
    
    // TAREFA DE VALIDAÇÃO DE USUÁRIO
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }
        
        // REALIZA PESQUISA EM BACKGROUD
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean status = true;
            
            // FECHA TECLADO
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
            
            // PESQUISA O DONO
            try {
                donoResponse = DonoService.getDonoData(mEmailView.getText().toString());
                
                // VALIDA O SE O DONO ESTÁ CORRETO
                if (!donoResponse.get("code").equals("200") || !donoResponse.get("senha").equals(mPassword)) {
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
            mAuthTask = null;
            progress.dismiss();
            
            if (success) {
                AuthService.changeSharedPreferences(true, getApplicationContext());
                
                // CPF + NOME + SEXO + NASCIMENTO + EMAIL + TELEFONE + ENDERECO + BAIRRO + MUNICIPIO + ESTADO + CEP + SENHA + COMPLEMENTO
                String cpf = donoResponse.get("cpf");
                String nome = donoResponse.get("nome");
                String email = donoResponse.get("email");
                String telefone = donoResponse.get("telefone");
                String endereco = donoResponse.get("endereco");
                String complemento = donoResponse.get("complemento");
                String bairro = donoResponse.get("bairro");
                String municipio = donoResponse.get("municipio");
                String estado = donoResponse.get("estado");
                String cep = donoResponse.get("cep");
                String senha = donoResponse.get("senha");
                String sexo = donoResponse.get("sexo");
                String strNasc = donoResponse.get("nascimento");
                
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                Date dataFormatada = new Date();
                
                try {
                    dataFormatada = formato.parse(strNasc);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                
                Dono dono = new Dono(cpf, nome, sexo, dataFormatada, email, telefone, endereco, bairro, municipio, estado, cep, senha, complemento);
                StaticList.AccessData.setDono(dono);

                AuthService.changeSharedPreferences(true, getApplicationContext());
                
                Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                startActivity(intent);
                
                finish();
            } else {
                ConnectivityManager cm = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                
                if (netInfo != null && netInfo.isConnected()) {
                    if (!donoResponse.get("code").equals("200")) {
                        mEmailView.requestFocus();
                        mEmailView.setError("Usuário inválido!");
                    } else if (!donoResponse.get("senha").equals(mPassword)) {
                        mPasswordView.requestFocus();
                        mPasswordView.setError("Senha inválida!");
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Não foi possível conectar\nVerifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
