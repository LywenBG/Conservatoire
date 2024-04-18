package fr.martinb.conservatoire.controller;

import fr.martinb.conservatoire.App;
import fr.martinb.conservatoire.modele.Eleve;
import fr.martinb.conservatoire.sql.PartitionDAO;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EleveController implements Initializable {

    @FXML
    private TextField recherche;

    @FXML
    private Label identifiants;

    @FXML
    private ComboBox<String> categories;

    @FXML
    private ListView<String> list;

    @FXML
    private Button supprimer;

    private Eleve eleve;

    private final Map<String, String[]> partitions = new HashMap<>();

    /**
     * Initialise les éléments en modifiant l'élève et donner les propriétés aux Controls
     *
     * @param url L'URL vers la ressource. Dans ce contexte, cela peut être null.
     * @param resourceBundle Le ResourceBundle associé à la ressource. Dans ce contexte, cela peut être null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eleve = App.getEleve();
        identifiants.setText(eleve.getPrenom() + " " + eleve.getNom());

        categories.getItems().addAll( "Auteur", "Titre");
        recherche("", "");

        categories.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            recherche.setVisible(true);
            recherche.setText("");
            changeRecherche();
        });

        list.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> supprimer.setVisible(newValue != null));
    }

    /**
     * Quand il y a suppression d'une partition alors on reset la recherche
     */
    @FXML
    private void changeRecherche() {
        ReadOnlyObjectProperty<String> selectionModel = categories.getSelectionModel().selectedItemProperty();
        recherche(recherche.getText(), selectionModel.isNull().get() ? "" : selectionModel.getValue());
    }

    /**
     * Supprime une partition du classeur d'une élève du conservatoire
     *
     * @throws SQLException Si la requête n'abouti pas
     */
    @FXML
    private void suppression() throws SQLException {
        String[] partition = partitions.get(list.getSelectionModel().selectedItemProperty().getValue());
        new PartitionDAO().supprimerPartition(partition[0], partition[1]);
        changeRecherche();
    }

    /**
     * Recherche une partition par son titre ou auteur avec les lettres mit dans le TextField recherche
     * et le tag pour la recherche par auteur ou par titre
     *
     * @param pattern les
     * @param tag Type de recherche par auteur ou titre, cherche les deux si tag est null
     */
    private void recherche(String pattern, String tag) {
        list.getItems().clear();
        partitions.clear();

        String newPattern = ".*" + Pattern.quote(pattern.toLowerCase()) + ".*";

        eleve.getPartitions().stream().filter(p -> switch (tag) {
            case "Auteur" -> p.getAuteur().toLowerCase().matches(newPattern);
            case "Titre" -> p.getTitre().toLowerCase().matches(newPattern);
            default -> true;
        }).forEach(p -> {
            String phrase = "Page N°" + p.getPage() + ", " + p.getTitre() + " - " + p.getAuteur();
            list.getItems().add(phrase);
            partitions.put(phrase, new String[]{p.getTitre(), p.getAuteur()});
        });
        list.getItems().sort(String.CASE_INSENSITIVE_ORDER);
    }
}
