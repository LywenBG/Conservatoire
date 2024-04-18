package fr.martinb.conservatoire.controller;

import fr.martinb.conservatoire.App;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.Locale;

public class MenuController {

    /**
     * Amène à la page de la liste des partitions
     * Ne fait rien si l'élève est null (il ne s'est pas connecté) ou si l'élève se trouve déjà dans la page de la liste des partitions
     *
     * @throws IOException Si le chemin vers cette page n'existe pas
     */
    @FXML
    private void saisie() throws IOException {
        String partition = "Partitions";
        if(App.getEleve() == null || App.getStage().getTitle().equals(partition)) return;
        App.setRoot(partition.toLowerCase(Locale.ROOT));
        App.getStage().setTitle(partition);
    }

    /**
     * Amène à la page des partitions de l'élève
     * Ne fait rien si l'élève est null (il ne s'est pas connecté) ou si l'élève se trouve déjà dans la page de ses partitions
     *
     * @throws IOException Si le chemin vers cette page n'existe pas
     */
    @FXML
    private void recherche() throws IOException {
        String eleve = "Eleve";
        if(App.getEleve() == null || App.getStage().getTitle().equals(eleve)) return;
        App.setRoot(eleve.toLowerCase(Locale.ROOT));
        App.getStage().setTitle(eleve);
    }
}
