package com.fiap.guilhermebigois.fun4pets.service;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AuthService {
    private static final String SALESFORCE_AUTH_URL = "https://login.salesforce.com/services/oauth2/token";
    private static final String CLIENT_ID = "3MVG9dZJodJWITSuw2GAlgeugiW6A2_HeYPkTHrWX9MDylPDbG_bFbbe_1NJKJC8x0VUgO4ZOalb9McOs4Ho7";
    private static final String CLIENT_SECRET = "4588630803233802908";
    private static final String SECURITY_KEY = "C2EWwEfq7lfFDJm0O5GhduuPl";
    private static final String REDIRECT_URI = "https://www.fiap.com.br";
    private static final String USERNAME = "guilhermebigois@outlook.com";
    private static final String PASSWORD = "Berna021" + SECURITY_KEY;
    private static final String GRANT_TYPE = "password";

    public static String getToken() throws Exception {
        // CLIENTE HTTP
        HttpClient client = new DefaultHttpClient();

        // URL DE REQUISIÇÃO
        HttpPost post = new HttpPost(SALESFORCE_AUTH_URL);

        // CABEÇALHO
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        // PARÂMETROS
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("client_id", CLIENT_ID));
        urlParameters.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
        urlParameters.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
        urlParameters.add(new BasicNameValuePair("grant_type", GRANT_TYPE));
        urlParameters.add(new BasicNameValuePair("username", USERNAME));
        urlParameters.add(new BasicNameValuePair("password", PASSWORD));

        // INCORPORA OS PARÂMETROS
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

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

        // DÁ A LUZ AO TOKEN
        JSONObject jsonObject = new JSONObject(result.toString());
        String baerer = jsonObject.getString("access_token");

        return baerer;
    }

    public static void changeSharedPreferences(Boolean change, String email, Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("user_preferences", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ativo", change);
        editor.putString("email", email);
        editor.commit();

    }
}