package com.fiap.guilhermebigois.fun4pets.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fiap.guilhermebigois.fun4pets.MaskEditUtil;
import com.fiap.guilhermebigois.fun4pets.R;
import com.fiap.guilhermebigois.fun4pets.dao.StaticList;
import com.fiap.guilhermebigois.fun4pets.model.Animal;
import com.fiap.guilhermebigois.fun4pets.service.AnimalService;

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

public class AddAnimalActivity extends AppCompatActivity {
    private EditText txtNome;
    private RadioGroup rdoSexo;
    private EditText txtNascimento;
    private EditText txtEspecie;
    private EditText txtRaca;
    private EditText txtColoracao;
    private Button btnSalvar;
    private ACProgressFlower progress;
    private SFAddAnimal addAnimal;
    private Animal animalFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animal);

        // CAPTURA CAMPOS
        txtNome = findViewById(R.id.animal_nome);
        rdoSexo = findViewById(R.id.animal_sexo);
        txtNascimento = findViewById(R.id.animal_data);
        txtEspecie = findViewById(R.id.animal_especie);
        txtRaca = findViewById(R.id.animal_raca);
        txtColoracao = findViewById(R.id.animal_coloracao);
        btnSalvar = findViewById(R.id.animal_btn_salvar);

        // ADICIONA AS MÁSCARAS NOS CAMPOS
        txtNascimento.addTextChangedListener(MaskEditUtil.mask(txtNascimento, MaskEditUtil.FORMAT_DATE));

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
                new DatePickerDialog(AddAnimalActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                if (validaCampos()) {
                    String nome = txtNome.getText().toString();
                    String especie = txtEspecie.getText().toString();
                    String raca = txtRaca.getText().toString();
                    String coloracao = txtColoracao.getText().toString();

                    int opcSexo = rdoSexo.getCheckedRadioButtonId();
                    String sexo = (opcSexo == R.id.animal_rdo_masc) ? "M" : "F";

                    String strNasc = txtNascimento.getText().toString();
                    strNasc.replaceAll("/", "-");

                    SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                    Date dataFormatada = new Date();

                    try {
                        dataFormatada = formato.parse(strNasc);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // NOME, SEXO, NASCIMENTO, ESPECIE, RACA, COLORACAO, DONO, ID
                    animalFinal = new Animal(nome, sexo, dataFormatada, especie, raca, coloracao, StaticList.AccessData.getDono(), "");

                    progress = new ACProgressFlower.Builder(AddAnimalActivity.this)
                            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                            .themeColor(Color.WHITE)
                            .fadeColor(Color.DKGRAY).build();
                    progress.show();

                    addAnimal = new SFAddAnimal(animalFinal);
                    addAnimal.execute((Void) null);
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

        if (txtEspecie.getText().toString().isEmpty()) {
            campos.add("especie");
            txtEspecie.setError(ERROR_MESSAGE);

            isValid = false;
        }

        if (txtRaca.getText().toString().isEmpty()) {
            campos.add("raca");
            txtRaca.setError(ERROR_MESSAGE);

            isValid = false;
        }

        if (txtColoracao.getText().toString().isEmpty()) {
            campos.add("coloracao");
            txtColoracao.setError(ERROR_MESSAGE);

            isValid = false;
        }

        if (campos.size() > 0) {
            switch (campos.get(0)) {
                case "nome":
                    txtNome.requestFocus();
                    break;
                case "sexo":
                    Toast.makeText(this, "Campo SEXO é obrigatório!", Toast.LENGTH_SHORT).show();
                    break;
                case "nascimento":
                    txtNascimento.requestFocus();
                    break;
                case "especie":
                    txtEspecie.requestFocus();
                    break;
                case "raca":
                    txtRaca.requestFocus();
                    break;
                case "coloracao":
                    txtColoracao.requestFocus();
                    break;
            }
        }

        return isValid;
    }

    public class SFAddAnimal extends AsyncTask<Void, Void, Boolean> {
        private Animal animal;

        public SFAddAnimal(Animal animal) {
            this.animal = animal;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                HashMap<String, String> addAnimalResponse = AnimalService.addAnimal(animal);

                if (!addAnimalResponse.get("code").equals("201")) {
                    return false;
                } else {
                    List<Animal> animais = AnimalService.getAllAnimals();

                    if (animais == null) {
                        return false;
                    } else {
                        StaticList.AccessData.setAnimalList(animais);
                    }
                }
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            addAnimal = null;
            progress.dismiss();

            if (success) {
                Toast.makeText(AddAnimalActivity.this, "Pet cadastrado com sucesso!\nO mesmo estará em seus registros no próximo login", Toast.LENGTH_LONG).show();
                AddAnimalActivity.this.finish();
            } else {
                ConnectivityManager cm = (ConnectivityManager) AddAnimalActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                if (!(netInfo != null) && (!(netInfo.isConnected()))) {
                    Toast.makeText(AddAnimalActivity.this, "Não foi possível conectar\nVerifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddAnimalActivity.this, "Não foi possível cadastrar o pet\nTente novamente", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
