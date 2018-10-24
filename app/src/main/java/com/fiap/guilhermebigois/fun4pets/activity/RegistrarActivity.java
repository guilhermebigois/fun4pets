package com.fiap.guilhermebigois.fun4pets.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.fiap.guilhermebigois.fun4pets.MaskEditUtil;
import com.fiap.guilhermebigois.fun4pets.R;
import com.fiap.guilhermebigois.fun4pets.model.Dono;
import com.fiap.guilhermebigois.fun4pets.service.DonoService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class RegistrarActivity extends AppCompatActivity {
    private EditText txtCPF;
    private EditText txtNome;
    private RadioGroup rdoSexo;
    private EditText txtNascimento;
    private EditText txtEmail;
    private EditText txtTelefone;
    private EditText txtEndereco;
    private EditText txtComplemento;
    private EditText txtBairro;
    private EditText txtMunicipio;
    private Spinner spnEstado;
    private EditText txtCEP;
    private EditText txtSenha;
    private Button btnSalvar;
    private ACProgressFlower progress;
    private SFDonoPOST donoPOST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        // CAPTURA OS CAMPOS
        txtCPF = findViewById(R.id.registrar_cpf);
        txtNome = findViewById(R.id.registrar_nome);
        rdoSexo = findViewById(R.id.registrar_sexo);
        txtNascimento = findViewById(R.id.registrar_data);
        txtEmail = findViewById(R.id.registrar_email);
        txtTelefone = findViewById(R.id.registrar_telefone);
        txtEndereco = findViewById(R.id.registrar_endereco);
        txtComplemento = findViewById(R.id.registrar_complemento);
        txtBairro = findViewById(R.id.registrar_bairro);
        txtMunicipio = findViewById(R.id.registrar_municipio);
        spnEstado = findViewById(R.id.registrar_estado);
        txtCEP = findViewById(R.id.registrar_cep);
        txtSenha = findViewById(R.id.registrar_senha);
        btnSalvar = findViewById(R.id.registrar_btn_salvar);

        // ADICIONA AS MÁSCARAS NOS CAMPOS
        txtNascimento.addTextChangedListener(MaskEditUtil.mask(txtNascimento, MaskEditUtil.FORMAT_DATE));
        txtTelefone.addTextChangedListener(MaskEditUtil.mask(txtTelefone, MaskEditUtil.FORMAT_FONE));
        txtCPF.addTextChangedListener(MaskEditUtil.mask(txtCPF, MaskEditUtil.FORMAT_CPF));
        txtCEP.addTextChangedListener(MaskEditUtil.mask(txtCEP, MaskEditUtil.FORMAT_CEP));

        // INSERE OS ESTADOS
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, DonoService.getEstados());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnEstado.setAdapter(arrayAdapter);

        // MONTA DATEPICKER
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));

                txtNascimento.setText(sdf.format(myCalendar.getTime()));
            }

        };

        // LISTENER PARA QUANDO CLICADO NA DATA DE NASCIMENTO
        txtNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegistrarActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // LISTENER PARA QUANDO CLICADO EM SALVAR
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                if (validaCampos()) {
                    String cpf = txtCPF.getText().toString();
                    String nome = txtNome.getText().toString();
                    String email = txtEmail.getText().toString();
                    String telefone = txtTelefone.getText().toString();
                    String endereco = txtEndereco.getText().toString();
                    String complemento = txtComplemento.getText().toString();
                    String bairro = txtBairro.getText().toString();
                    String municipio = txtMunicipio.getText().toString();
                    String estado = spnEstado.getSelectedItem().toString().substring(0, 2);
                    String cep = txtCEP.getText().toString();
                    String senha = txtSenha.getText().toString();

                    int opcSexo = rdoSexo.getCheckedRadioButtonId();
                    String sexo = (opcSexo == R.id.registrar_rdo_masc) ? "M" : "F";

                    String strNasc = txtNascimento.getText().toString();
                    strNasc.replaceAll("/", "-");

                    SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                    Date dataFormatada = new Date();

                    try {
                        dataFormatada = formato.parse(strNasc);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // CPF + NOME + SEXO + NASCIMENTO + EMAIL + TELEFONE + ENDERECO + BAIRRO + MUNICIPIO + ESTADO + CEP + SENHA + COMPLEMENTO + ID
                    Dono dono = new Dono(cpf, nome, sexo, dataFormatada, email, telefone, endereco, bairro, municipio, estado, cep, senha, complemento, "");

                    progress = new ACProgressFlower.Builder(RegistrarActivity.this)
                            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                            .themeColor(Color.WHITE)
                            .fadeColor(Color.DKGRAY).build();
                    progress.show();

                    donoPOST = new SFDonoPOST(dono);
                    donoPOST.execute((Void) null);
                }
            }
        });
    }

    // FUNÇÃO PARA VALIDAÇÃO DE CAMPOS
    private Boolean validaCampos() {
        final String ERROR_MESSAGE = "Este campo é obrigatório!";

        Boolean isValid = true;
        List<String> campos = new ArrayList<String>();

        if (txtNome.getText().toString().isEmpty()) {
            campos.add("nome");
            txtNome.setError(ERROR_MESSAGE);

            isValid = false;
        }

        if (txtSenha.getText().toString().isEmpty()) {
            campos.add("senha");
            txtSenha.setError(ERROR_MESSAGE);

            isValid = false;
        } else if (txtSenha.getText().toString().length() <= 4) {
            campos.add("senha");
            txtSenha.setError("Senha muito curta!");

            isValid = false;
        }

        if (txtCPF.getText().toString().isEmpty()) {
            campos.add("cpf");
            txtCPF.setError(ERROR_MESSAGE);

            isValid = false;
        } else {
            String cpf = txtCPF.getText().toString().replace(".", "");
            cpf = cpf.replace("-", "");

            if (cpf.length() != 11) {
                campos.add("cpf");
                txtCPF.setError("CPF inválido!");

                isValid = false;
            }
        }

        if (rdoSexo.getCheckedRadioButtonId() == -1) {
            campos.add("sexo");

            isValid = false;
        }

        if (txtNascimento.getText().toString().isEmpty()) {
            campos.add("nascimento");
            txtNascimento.setError(ERROR_MESSAGE);

            isValid = false;
        } else {
            String data = txtNascimento.getText().toString();
            if (data.length() != 10) {
                campos.add("nascimento");
                txtNascimento.setError("Data de nascimento inválida!");

                isValid = false;
            } else {
                Integer dia = Integer.parseInt(data.substring(0, 2));
                Integer mes = Integer.parseInt(data.substring(3, 5));
                Integer ano = Integer.parseInt(data.substring(6));
                Calendar cal = GregorianCalendar.getInstance();

                if ((dia > 31) || ((mes > 12)) || ((ano > cal.get(Calendar.YEAR)))) {
                    campos.add("nascimento");
                    txtNascimento.setError("Data de nascimento inválida!");

                    isValid = false;
                }
            }
        }

        if (txtEmail.getText().toString().isEmpty()) {
            campos.add("email");
            txtEmail.setError(ERROR_MESSAGE);

            isValid = false;
        }

        if (txtTelefone.getText().toString().isEmpty()) {
            campos.add("telefone");
            txtTelefone.setError(ERROR_MESSAGE);

            isValid = false;
        } else {
            String telefone = txtTelefone.getText().toString().replace("(", "");
            telefone = telefone.replace(")", "");
            telefone = telefone.replace(" ", "");
            telefone = telefone.replace("-", "");

            if (telefone.length() < 10 || telefone.length() > 11) {
                campos.add("telefone");
                txtTelefone.setError("Número de telefone inválido!");

                isValid = false;
            }
        }

        if (txtCEP.getText().toString().isEmpty()) {
            campos.add("cep");
            txtCEP.setError(ERROR_MESSAGE);

            isValid = false;
        } else {
            String cep = txtCEP.getText().toString().replace("-", "");

            if (cep.length() != 8) {
                campos.add("cep");
                txtCEP.setError("CEP inválido!");

                isValid = false;
            }
        }

        if (txtEndereco.getText().toString().isEmpty()) {
            campos.add("endereco");
            txtEndereco.setError(ERROR_MESSAGE);

            isValid = false;
        }

        if (txtBairro.getText().toString().isEmpty()) {
            campos.add("bairro");
            txtBairro.setError(ERROR_MESSAGE);

            isValid = false;
        }

        if (txtMunicipio.getText().toString().isEmpty()) {
            campos.add("municipio");
            txtMunicipio.setError(ERROR_MESSAGE);

            isValid = false;
        }

        if (spnEstado.getSelectedItem().toString().equals("Selecione o estado")) {
            campos.add("estado");

            isValid = false;
        }

        if (campos.size() > 0) {
            switch (campos.get(0)) {
                case "nome":
                    txtNome.requestFocus();
                    break;
                case "senha":
                    txtSenha.requestFocus();
                    break;
                case "cpf":
                    txtCPF.requestFocus();
                    break;
                case "sexo":
                    Toast.makeText(this, "Campo SEXO é obrigatório!", Toast.LENGTH_SHORT).show();
                    break;
                case "nascimento":
                    txtNascimento.requestFocus();
                    break;
                case "email":
                    txtEmail.requestFocus();
                    break;
                case "telefone":
                    txtTelefone.requestFocus();
                    break;
                case "cep":
                    txtCEP.requestFocus();
                    break;
                case "endereco":
                    txtEndereco.requestFocus();
                    break;
                case "bairro":
                    txtBairro.requestFocus();
                    break;
                case "municipio":
                    txtMunicipio.requestFocus();
                    break;
                case "estado":
                    Toast.makeText(this, "Campo ESTADO é obrigatório!", Toast.LENGTH_SHORT).show();
            }
        }

        return isValid;
    }

    public class SFDonoPOST extends AsyncTask<Void, Void, Boolean> {
        private Dono dono;

        public SFDonoPOST(Dono dono) {
            this.dono = dono;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                HashMap<String, String> addDonoResponse = DonoService.addDono(dono);

                if (!addDonoResponse.get("code").equals("201")) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            donoPOST = null;
            progress.dismiss();

            if (success) {
                Toast.makeText(RegistrarActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                RegistrarActivity.this.recreate();
                RegistrarActivity.this.finish();
            } else {
                ConnectivityManager cm = (ConnectivityManager) RegistrarActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                if (!(netInfo != null) && (!(netInfo.isConnected()))) {
                    Toast.makeText(RegistrarActivity.this, "Não foi possível conectar\nVerifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistrarActivity.this, "Não foi possível cadastrar o usuário\nTente novamente", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
