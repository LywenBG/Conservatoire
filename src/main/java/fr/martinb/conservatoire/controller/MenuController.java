package fr.martinb.conservatoire.controller;

import fr.martinb.conservatoire.App;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.Locale;

public class MenuController {

    @FXML
    private void saisie() throws IOException {
        String partition = "Partitions";
        if(App.getEleve() == null || App.getStage().getTitle().equals(partition)) return;
        App.setRoot(partition.toLowerCase(Locale.ROOT));
        App.getStage().setTitle(partition);
    }

    @FXML
    private void recherche() throws IOException {
        String eleve = "Eleve";
        if(App.getEleve() == null || App.getStage().getTitle().equals(eleve)) return;
        App.setRoot(eleve.toLowerCase(Locale.ROOT));
        App.getStage().setTitle(eleve);
    }
}
