package com.fiap.guilhermebigois.fun4pets.service;

import com.fiap.guilhermebigois.fun4pets.model.Dono;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DonoService {
    private static final String DONO_URL = "https://curious-badger-81660-dev-ed.my.salesforce.com/services/data/v43.0/sobjects/Dono__c";
    
    public static HashMap<String, String> getDonoData(String email) throws Exception {
        HashMap<String, String> donoResponse = new HashMap<String, String>();
        String bearer = AuthService.getToken();
        
        // CLIENTE HTTP
        HttpClient client = new DefaultHttpClient();
        
        // URL DE REQUISIÇÃO
        HttpGet get = new HttpGet(DONO_URL + "/Email__c/" + email);
        
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
            
            donoResponse.put("code", "200");
            donoResponse.put("nome", jsonObject.getString("Name"));
            donoResponse.put("sexo", jsonObject.getString("Sexo__c"));
            donoResponse.put("cpf", jsonObject.getString("CPF__c"));
            donoResponse.put("senha", jsonObject.getString("Senha__c"));
            donoResponse.put("nascimento", jsonObject.getString("Data_Nasc__c"));
            donoResponse.put("telefone", jsonObject.getString("Telefone__c"));
            donoResponse.put("endereco", jsonObject.getString("Endereco__c"));
            donoResponse.put("complemento", jsonObject.getString("Complemento__c"));
            donoResponse.put("bairro", jsonObject.getString("Bairro__c"));
            donoResponse.put("cidade", jsonObject.getString("Cidade__c"));
            donoResponse.put("estado", jsonObject.getString("Estado__c"));
            donoResponse.put("cep", jsonObject.getString("CEP__c"));
        } else {
            JSONArray jsonArray = new JSONArray(result.toString());
            String error = jsonArray.get(0).toString();
            
            donoResponse.put("code", String.valueOf(response.getStatusLine().getStatusCode()));
            donoResponse.put("message", error.substring(error.indexOf("message") + 10, error.length() - 2));
        }
        
        return donoResponse;
    }
    
    public static HashMap<String, String> addDono(Dono dono) throws Exception {
        HashMap<String, String> donoResponse = new HashMap<String, String>();
        String bearer = AuthService.getToken();
        
        // CLIENTE HTTP
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(DONO_URL);
        
        // BODY
        StringEntity input = new StringEntity(changeDonoToJSON(dono), "UTF-8");
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
            
            donoResponse.put("code", "201");
        } else {
            JSONArray jsonArray = new JSONArray(result.toString());
            String error = jsonArray.get(0).toString();
            
            donoResponse.put("code", String.valueOf(response.getStatusLine().getStatusCode()));
        }
        
        return donoResponse;
    }
    
    private static String changeDonoToJSON(Dono dono) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("Name", dono.getNome().trim());
        jsonObject.put("Sexo__c", dono.getSexo().trim());
        jsonObject.put("Senha__c", dono.getSenha().trim());
        jsonObject.put("Email__c", dono.getEmail().trim());
        jsonObject.put("Endereco__c", dono.getEndereco().trim());
        jsonObject.put("Bairro__c", dono.getBairro().trim());
        jsonObject.put("Cidade__c", dono.getMunicipio().trim());
        jsonObject.put("CEP__c", dono.getCep().trim());
        jsonObject.put("CPF__c", dono.getCpf().trim());
        jsonObject.put("Estado__c", dono.getEstado());
        jsonObject.put("Telefone__c", dono.getTelefone().trim());
        
        String complemento = (dono.getComplemento().isEmpty()) ? "null\"" : dono.getComplemento().trim();
        jsonObject.put("Complemento__c", complemento);
        
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String nascimeto = formato.format(dono.getNascimento());
        jsonObject.put("Data_Nasc__c", nascimeto);
        
        String json = jsonObject.toString();
        
        return json;
    }
    
    public static List<String> getEstados() {
        List<String> estados = new ArrayList<String>();
        
        estados.add("Selecione o estado");
        estados.add("AC - Acre");
        estados.add("AL- Alagoas");
        estados.add("AP - Amapá");
        estados.add("AM - Amazonas");
        estados.add("BA - Bahia");
        estados.add("CE - Ceará");
        estados.add("DF - Distrito Federal");
        estados.add("ES - Espírito Santo");
        estados.add("GO - Goiás");
        estados.add("MA - Maranhão");
        estados.add("MT - Mato Grosso");
        estados.add("MS - Mato Grosso do Sul");
        estados.add("MG - Minas Gerais");
        estados.add("PA - Pará");
        estados.add("PB - Paraíba");
        estados.add("PR - Paraná");
        estados.add("PE - Pernambuco");
        estados.add("PI - Piauí");
        estados.add("RJ - Rio de Janeiro");
        estados.add("RN - Rio Grande do Norte");
        estados.add("RS - Rio Grande do Sul");
        estados.add("RO - Rondônia");
        estados.add("RR - Roraima");
        estados.add("SC - Santa Catarina");
        estados.add("SP - São Paulo");
        estados.add("SE - Sergipe");
        estados.add("TO - Tocantins");
        
        return estados;
    }
}