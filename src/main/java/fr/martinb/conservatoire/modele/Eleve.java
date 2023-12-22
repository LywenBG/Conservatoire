package fr.martinb.conservatoire.modele;

import java.util.ArrayList;
import java.util.List;

public class Eleve {

    private final int numero;
    private String nom;
    private String prenom;
    private int cycle;
    private int annee;
    private String instrument;
    private final List<Partition> partitions;

    public Eleve(int numero, String nom, String prenom, int cycle, int annee, String instrument) {
        this.numero = numero;
        this.nom = nom;
        this.prenom = prenom;
        this.cycle = cycle;
        this.annee = annee;
        this.instrument = instrument;
        this.partitions = new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public List<Partition> getPartitions() {
        return partitions;
    }
}
