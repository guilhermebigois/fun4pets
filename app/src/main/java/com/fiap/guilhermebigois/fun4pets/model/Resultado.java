package com.fiap.guilhermebigois.fun4pets.model;

public class Resultado {
    private Integer id;
    private Animal primeiro;
    private Animal segundo;
    private Animal terceiro;
    private String premiacao;
    private Competicao competicao;
    
    public Resultado(Integer id, Animal primeiro, Animal segundo, Competicao competicao) {
        this.id = id;
        this.primeiro = primeiro;
        this.segundo = segundo;
        this.competicao = competicao;
    }
    
    public Resultado(Integer id, Animal primeiro, Animal segundo, Animal terceiro, String premiacao, Competicao competicao) {
        this.id = id;
        this.primeiro = primeiro;
        this.segundo = segundo;
        this.terceiro = terceiro;
        this.premiacao = premiacao;
        this.competicao = competicao;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Animal getPrimeiro() {
        return primeiro;
    }
    
    public void setPrimeiro(Animal primeiro) {
        this.primeiro = primeiro;
    }
    
    public Animal getSegundo() {
        return segundo;
    }
    
    public void setSegundo(Animal segundo) {
        this.segundo = segundo;
    }
    
    public Animal getTerceiro() {
        return terceiro;
    }
    
    public void setTerceiro(Animal terceiro) {
        this.terceiro = terceiro;
    }
    
    public String getPremiacao() {
        return premiacao;
    }
    
    public void setPremiacao(String premiacao) {
        this.premiacao = premiacao;
    }
    
    public Competicao getCompeticao() {
        return competicao;
    }
    
    public void setCompeticao(Competicao competicao) {
        this.competicao = competicao;
    }
}
