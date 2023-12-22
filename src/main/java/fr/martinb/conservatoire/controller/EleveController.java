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

    private final Map<String, String[]> morceaux = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eleve = App.getEleve();
        identifiants.setText(eleve.getPrenom() + " " + eleve.getNom());

        categories.getItems().addAll( "Auteur", "Titre");
        recherche("", "");

        categories.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            recherche.setVisible(true);
            recherche.setText("");
        });

        list.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> supprimer.setVisible(newValue != null));
    }

    @FXML
    private void changeRecherche() {
        ReadOnlyObjectProperty<String> selectionModel = categories.getSelectionModel().selectedItemProperty();
        recherche(recherche.getText(), selectionModel.isNull().get() ? "" : selectionModel.getValue());
    }

    @FXML
    private void suppression() throws SQLException {
        String[] morceau = morceaux.get(list.getSelectionModel().selectedItemProperty().getValue());
        new PartitionDAO().supprimerPartition(morceau[0], morceau[1]);
        changeRecherche();
    }

    private void recherche(String pattern, String tag) {
        list.getItems().clear();
        morceaux.clear();

        String newPattern = ".*" + Pattern.quote(pattern.toLowerCase()) + ".*";

        eleve.getPartitions().stream().filter(p -> switch (tag) {
            case "Auteur" -> p.getAuteur().toLowerCase().matches(newPattern);
            case "Titre" -> p.getTitre().toLowerCase().matches(newPattern);
            default -> true;
        }).forEach(p -> {
            String phrase = "Page N°" + p.getPage() + ", " + p.getTitre() + " - " + p.getAuteur();
            list.getItems().add(phrase);
            morceaux.put(phrase, new String[]{p.getTitre(), p.getAuteur()});
        });
        list.getItems().sort(String.CASE_INSENSITIVE_ORDER);
    }
}
