package com.fiap.guilhermebigois.fun4pets.service;

import com.fiap.guilhermebigois.fun4pets.dao.StaticList;
import com.fiap.guilhermebigois.fun4pets.model.Animal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class AnimalService {
    private static final String ANIMAL_URL = "https://curious-badger-81660-dev-ed.my.salesforce.com/services/data/v43.0/sobjects/Animal__c";

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
            animalResponse.put("id", "");
        } else {
            JSONArray jsonArray = new JSONArray(result.toString());
            String error = jsonArray.get(0).toString();

            animalResponse.put("code", String.valueOf(response.getStatusLine().getStatusCode()));
        }

        return animalResponse;
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
