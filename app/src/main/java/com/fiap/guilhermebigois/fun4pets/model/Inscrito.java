package com.fiap.guilhermebigois.fun4pets.model;

public class Inscrito {
    private Integer inscrito;
    private Animal animal;
    private Competicao competicao;

    public Inscrito(Integer inscrito, Animal animal, Competicao competicao) {
        this.inscrito = inscrito;
        this.animal = animal;
        this.competicao = competicao;
    }

    public Integer getInscrito() {
        return inscrito;
    }

    public void setInscrito(Integer inscrito) {
        this.inscrito = inscrito;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Competicao getCompeticao() {
        return competicao;
    }

    public void setCompeticao(Competicao competicao) {
        this.competicao = competicao;
    }
}
