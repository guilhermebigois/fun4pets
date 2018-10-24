package com.fiap.guilhermebigois.fun4pets.dao;

import com.fiap.guilhermebigois.fun4pets.model.Animal;
import com.fiap.guilhermebigois.fun4pets.model.Competicao;
import com.fiap.guilhermebigois.fun4pets.model.Dono;
import com.fiap.guilhermebigois.fun4pets.model.Inscrito;
import com.fiap.guilhermebigois.fun4pets.model.Resultado;

import java.util.ArrayList;
import java.util.List;

public class StaticList {
    public static class AccessData {
        static List<Animal> animalList;
        static List<Competicao> competicaoList;
        static Dono dono;
        static List<Inscrito> inscritoList;
        static List<Resultado> resultadoList;

        public static List<Animal> getAnimalList() {
            if (animalList == null) {
                animalList = new ArrayList<Animal>();
            }
            return animalList;
        }

        public static void setAnimalList(List<Animal> animais) {
            AccessData.animalList = animais;
        }

        public static void addAnimal(Animal animal) {
            animalList.add(animal);
        }

        public static List<Competicao> getCompeticaoList() {
            if (competicaoList == null) {
                competicaoList = new ArrayList<Competicao>();
            }

            return competicaoList;
        }

        public static void addCompeticao(Competicao competicao) {
            competicaoList.add(competicao);
        }

        public static Dono getDono() {
            if (dono == null) {
                dono = new Dono();
            }

            return dono;
        }

        public static void setDono(Dono dono) {
            AccessData.dono = dono;
        }

        public static List<Inscrito> getInscritoList() {
            if (inscritoList == null) {
                inscritoList = new ArrayList<Inscrito>();
            }

            return inscritoList;
        }

        public static void addInscrito(Inscrito inscrito) {
            inscritoList.add(inscrito);
        }

        public static List<Resultado> getResultadoList() {
            if (getResultadoList() == null) {
                resultadoList = new ArrayList<Resultado>();
            }

            return resultadoList;
        }

        public static void addResultado(Resultado resultado) {
            resultadoList.add(resultado);
        }

        public static void clearStaticData() {
            animalList = null;
            competicaoList = null;
            dono = null;
            inscritoList = null;
            resultadoList = null;
        }
    }
}
