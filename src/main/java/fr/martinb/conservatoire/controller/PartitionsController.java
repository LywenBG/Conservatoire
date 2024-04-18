package fr.martinb.conservatoire.controller;

import fr.martinb.conservatoire.App;
import fr.martinb.conservatoire.modele.Eleve;
import fr.martinb.conservatoire.modele.Partition;
import fr.martinb.conservatoire.sql.DAO;
import fr.martinb.conservatoire.sql.PartitionDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PartitionsController implements Initializable {

    @FXML
    private Label identifiants;

    @FXML
    private Label texte;

    @FXML
    private Label labelClasseur;

    @FXML
    private Label numeroLabel;

    @FXML
    private TextField numeroTextField;

    @FXML
    private Button ajouterButton;



    @FXML
    private Label labelMorceau;

    @FXML
    private TextField auteur;

    @FXML
    private TextField morceau;



    @FXML
    private ListView<String> list;

    private PartitionDAO partitionDAO;

    private final Map<String, String[]> morceaux = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partitionDAO = new PartitionDAO();
        Eleve eleve = App.getEleve();
        identifiants.setText(eleve.getPrenom() + " " + eleve.getNom());
        try {
            PreparedStatement preparedStatement = DAO.getConnection().prepareStatement("""
                SELECT * FROM PARTITIONS;
            """);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String titre = resultSet.getString("PARNOM");
                String auteur = resultSet.getString("PARAUTEUR");
                String phrase = titre + " - " + auteur;

                list.getItems().add(phrase);
                morceaux.put(phrase, new String[]{titre, auteur});
            }
            list.getItems().sort(String.CASE_INSENSITIVE_ORDER);
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void ajouterMorceau() throws SQLException {
        String auteurTexte = auteur.getText();
        String morceauTexte = morceau.getText();

        if(auteurTexte.isEmpty() || morceauTexte.isEmpty()) {
            changeTexte(labelMorceau, "Veuillez remplir tous les champs", "red");
            return;
        }

        ResultSet resultSet = partitionDAO.getPartition(morceauTexte, auteurTexte);

        if(resultSet.next()) {
            changeTexte(labelMorceau, "Ce morceau existe déjà", "red");
            return;
        }

        partitionDAO.ajouterPartition(morceauTexte, auteurTexte);
        String phrase = morceauTexte + " - " + auteurTexte;
        list.getItems().add(phrase);
        morceaux.put(phrase, new String[]{morceauTexte, auteurTexte});
        list.getItems().sort(String.CASE_INSENSITIVE_ORDER);
        changeTexte(labelMorceau, "Morceau ajouté", "green");
        auteur.clear();
        morceau.clear();
    }

    @FXML
    private void ajouterClasseur() throws SQLException {
        String numeroString = numeroTextField.getText();
        if(numeroString.isEmpty() || !numeroString.matches("^(?:[1-9]\\d{0,2}|1000)$") ) {
            changeTexte(labelClasseur, "Veuillez rentrer un numéro entre 1 et 1000", "red");
            return;
        }

        String[] morceau = morceaux.get(list.getSelectionModel().selectedItemProperty().getValue());
        ResultSet resultSet = partitionDAO.getPartition(morceau[0], morceau[1]);
        resultSet.next();

        Eleve eleve = App.getEleve();

        Partition partition = new Partition(
                resultSet.getInt("PARNUM"),
                resultSet.getString("PARNOM"),
                resultSet.getString("PARAUTEUR"));

        if(eleve.getPartitions().stream().map(Partition::getNumero).anyMatch(p -> p == partition.getNumero())) {
            changeTexte(labelClasseur, "Ce morceau est déjà dans votre classeur", "red");
            return;
        }

        int page = Integer.parseInt(numeroString);
        if(eleve.getPartitions().stream().map(Partition::getPage).anyMatch(p -> p == page)) {
            changeTexte(labelClasseur, "Cette page est déjà utilisé", "red");
            return;
        }

        partition.setPage(page);
        partitionDAO.ajouterPartitionPourEleve(eleve.getNumero(), partition.getNumero(), page);
        changeTexte(labelClasseur, "Morceau ajouté", "green");
        numeroTextField.clear();
        eleve.getPartitions().add(partition);
    }

    @FXML
    private void selectionMorceau() {
        if(list.getSelectionModel().getSelectedItem() != null) {
            texte.setVisible(true);
            numeroTextField.setVisible(true);
            numeroLabel.setVisible(true);
            ajouterButton.setVisible(true);
        }
    }

    private void changeTexte(Label label, String texte, String couleur) {
        label.setStyle("-fx-text-fill: " + couleur + ";");
        label.setText(texte + ".");
    }
}
