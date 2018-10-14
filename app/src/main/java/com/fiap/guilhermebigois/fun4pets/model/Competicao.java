package com.fiap.guilhermebigois.fun4pets.model;

import java.util.Date;

public class Competicao {
    private Integer id;
    private String status;
    private Date date;
    private String local;
    private String modalidade;
    private String especie;
    
    public Competicao(Integer id, String status, Date date, String local, String modalidade, String especie) {
        this.id = id;
        this.status = status;
        this.date = date;
        this.local = local;
        this.modalidade = modalidade;
        this.especie = especie;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getLocal() {
        return local;
    }
    
    public void setLocal(String local) {
        this.local = local;
    }
    
    public String getModalidade() {
        return modalidade;
    }
    
    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }
    
    public String getEspecie() {
        return especie;
    }
    
    public void setEspecie(String especie) {
        this.especie = especie;
    }
}
