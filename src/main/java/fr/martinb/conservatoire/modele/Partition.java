package fr.martinb.conservatoire.modele;

public class Partition {

    private final int numero;
    private final String titre;
    private final String auteur;
    private int page;

    public Partition(int numero, String titre, String auteur) {
        this.numero = numero;
        this.titre = titre;
        this.auteur = auteur;
    }

    public Partition(int numero, String titre, String auteur, int page) {
        this(numero, titre, auteur);
        this.page = page;
    }

    public int getNumero() {
        return numero;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
