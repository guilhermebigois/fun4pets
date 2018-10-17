package com.fiap.guilhermebigois.fun4pets.model;

import java.util.Date;

public class Animal {
    private Integer id;
    private String nome;
    private String sexo;
    private Date nascimento;
    private String especie;
    private String raca;
    private String coloracao;
    private Dono dono;

    public Animal(Integer id, String nome, String sexo, String especie, String raca, String coloracao, Dono dono) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.especie = especie;
        this.raca = raca;
        this.coloracao = coloracao;
        this.dono = dono;
    }

    public Animal(Integer id, String nome, String sexo, Date nascimento, String especie, String raca, String coloracao, Dono dono) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.nascimento = nascimento;
        this.especie = especie;
        this.raca = raca;
        this.coloracao = coloracao;
        this.dono = dono;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getColoracao() {
        return coloracao;
    }

    public void setColoracao(String coloracao) {
        this.coloracao = coloracao;
    }

    public Dono getDono() {
        return dono;
    }

    public void setDono(Dono dono) {
        this.dono = dono;
    }
}
