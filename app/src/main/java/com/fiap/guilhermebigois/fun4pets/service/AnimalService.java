package com.fiap.guilhermebigois.fun4pets.service;

import com.fiap.guilhermebigois.fun4pets.dao.StaticList;
import com.fiap.guilhermebigois.fun4pets.model.Animal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AnimalService {
    private static final String DONO_ID = StaticList.AccessData.getDono().getId();
    private static final String ANIMAL_URL = "https://curious-badger-81660-dev-ed.my.salesforce.com/services/data/v43.0/sobjects/Animal__c";
    private static final String ANIMAL_BY_DONO_URL = "https://curious-badger-81660-dev-ed.my.salesforce.com/services/data/v43.0/query/?q=" +
            "SELECT+Name,Sexo__c,Especie__c,Raca__c,Coloracao__c,Data_Nasc__c,Id+FROM+Animal__c+WHERE+Dono__c+=+'" + DONO_ID + "'";

    public static HashMap<String, String> addAnimal(Animal animal) throws Exception {
        HashMap<String, String> animalResponse = new HashMap<String, String>();
        String bearer = AuthService.getToken();

        // CLIENTE HTTP
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(ANIMAL_URL);

        // BODY
        StringEntity input = new StringEntity(changeAnimalToJSON(animal), "UTF-8");
        input.setContentType("application/json;charset=UTF-8");
        post.setEntity(input);

        input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
        post.setHeader("Accept", "application/json");
        post.setEntity(input);

        // CABEÇALHO
        post.setHeader("Authorization", "Bearer " + bearer);
        post.setHeader("Content-Type", "application/json");

        // RESPOSTA DO POST
        HttpResponse response = client.execute(post);

        // FAZ O TRAMPO DE LER TUDO
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        // BUFFERIZA A LINHA
        StringBuffer result = new StringBuffer();
        String line = "";

        // WHILE DOIDO ATÉ PREENCHER A LINHA
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        // VERIFICA SE DEU SUCESSO
        if (response.getStatusLine().getStatusCode() == 201) {
            JSONObject jsonObject = new JSONObject(result.toString());

            animalResponse.put("code", "201");
            animalResponse.put("id", jsonObject.get("id").toString());
        } else {
            JSONArray jsonArray = new JSONArray(result.toString());
            String error = jsonArray.get(0).toString();

            animalResponse.put("code", String.valueOf(response.getStatusLine().getStatusCode()));
        }

        return animalResponse;
    }

    public static List<Animal> getAllAnimals() throws Exception {
        List<Animal> animais = new ArrayList<Animal>();

        String bearer = AuthService.getToken();

        // CLIENTE HTTP
        HttpClient client = new DefaultHttpClient();

        // URL DE REQUISIÇÃO
        HttpGet get = new HttpGet(ANIMAL_BY_DONO_URL);

        // CABEÇALHO
        get.setHeader("Authorization", "Bearer " + bearer);

        // RESPOSTA DO POST
        HttpResponse response = client.execute(get);

        // FAZ O TRAMPO DE LER TUDO
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        // BUFFERIZA A LINHA
        StringBuffer result = new StringBuffer();
        String line = "";

        // WHILE DOIDO ATÉ PREENCHER A LINHA
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        // VERIFICA SE DEU SUCESSO
        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject jsonObject = new JSONObject(result.toString());

            for (int i = 0; i < jsonObject.getJSONArray("records").length(); i++) {
                JSONObject auxAnimal = new JSONObject(jsonObject.getJSONArray("records").get(i).toString());

                String nome = auxAnimal.get("Name").toString();
                String sexo = auxAnimal.get("Sexo__c").toString();
                String especie = auxAnimal.get("Especie__c").toString();
                String raca = auxAnimal.get("Raca__c").toString();
                String coloracao = auxAnimal.get("Coloracao__c").toString();
                String id = auxAnimal.get("Id").toString();

                String nascimento = auxAnimal.get("Data_Nasc__c").toString();

                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                Date dataFormatada = new Date();

                try {
                    dataFormatada = formato.parse(nascimento);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // NOME, SEXO, NASCIMENTO, ESPECIE, RACA, COLORACAO, DONO, ID
                animais.add(new Animal(nome, sexo, dataFormatada, especie, raca, coloracao, StaticList.AccessData.getDono(), id));
            }
        }

        return animais;
    }

    private static String changeAnimalToJSON(Animal animal) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("Name", animal.getNome().trim());
        jsonObject.put("Sexo__c", animal.getSexo().trim());
        jsonObject.put("Especie__c", animal.getEspecie().trim());
        jsonObject.put("Raca__c", animal.getRaca().trim());
        jsonObject.put("Coloracao__c", animal.getColoracao().trim());

        jsonObject.put("Dono__c", StaticList.AccessData.getDono().getId().trim());

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String nascimeto = formato.format(animal.getNascimento());
        jsonObject.put("Data_Nasc__c", nascimeto);

        String json = jsonObject.toString();

        return json;
    }
}
