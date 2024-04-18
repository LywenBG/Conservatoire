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
    private Label labelPartition;

    @FXML
    private TextField auteur;

    @FXML
    private TextField titre;


    @FXML
    private ListView<String> list;

    private PartitionDAO partitionDAO;

    private final Map<String, String[]> partitions = new HashMap<>();

    /**
     * Initialise la page en mettant dans ListView toutes les partitions stockées dans la BDD
     * Va aussi lier le string de cette listview au nom de l'auteur dans une hashmap
     * Ce qui va nous permettre de récupérer l'auteur juste à partir de l'élément qu'on a sélectionné dans la ListView
     *
     * @param url L'URL vers la ressource. Dans ce contexte, cela peut être null.
     * @param resourceBundle Le ResourceBundle associé à la ressource. Dans ce contexte, cela peut être null.
     */
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
                partitions.put(phrase, new String[]{titre, auteur});
            }
            list.getItems().sort(String.CASE_INSENSITIVE_ORDER);
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ajoute une partition dans la base de donnée
     * Va aussi l'ajouter dans la ListView en temps réel
     * Ainsi que la mettre dans la hashmap
     *
     * @throws SQLException Si la requête n'abouti pas
     */
    @FXML
    private void ajouterPartition() throws SQLException {
        String auteurTexte = auteur.getText();
        String titreText = titre.getText();

        if(auteurTexte.isEmpty() || titreText.isEmpty()) {
            changeTexte(labelPartition, "Veuillez remplir tous les champs", "red");
            return;
        }

        ResultSet resultSet = partitionDAO.getPartition(titreText, auteurTexte);

        if(resultSet.next()) {
            changeTexte(labelPartition, "Cette partition existe déjà", "red");
            return;
        }

        partitionDAO.ajouterPartition(titreText, auteurTexte);
        String phrase = titreText + " - " + auteurTexte;
        list.getItems().add(phrase);
        partitions.put(phrase, new String[]{titreText, auteurTexte});
        list.getItems().sort(String.CASE_INSENSITIVE_ORDER);
        changeTexte(labelPartition, "Partition ajoutée", "green");
        auteur.clear();
        titre.clear();
    }

    /**
     * Va ajouter la partition au classeur de l'élève connecté avec la page de l'insertion
     * Tout en regardant que le numéro du classeur est valide (entre 1 et 1000)
     * En regardant si la partition est déjà dans le classeur
     * Et en regardant si la page du classeur est déjà utilisé
     *
     * @throws SQLException Si la requête n'abouti pas
     */
    @FXML
    private void ajouterClasseur() throws SQLException {
        String numeroString = numeroTextField.getText();
        if(numeroString.isEmpty() || !numeroString.matches("^(?:[1-9]\\d{0,2}|1000)$") ) {
            changeTexte(labelClasseur, "Veuillez rentrer un numéro entre 1 et 1000", "red");
            return;
        }

        String[] partitionString = partitions.get(list.getSelectionModel().selectedItemProperty().getValue());
        ResultSet resultSet = partitionDAO.getPartition(partitionString[0], partitionString[1]);
        resultSet.next();

        Eleve eleve = App.getEleve();

        Partition partition = new Partition(
                resultSet.getInt("PARNUM"),
                resultSet.getString("PARNOM"),
                resultSet.getString("PARAUTEUR"));

        if(eleve.getPartitions().stream().map(Partition::getNumero).anyMatch(p -> p == partition.getNumero())) {
            changeTexte(labelClasseur, "Cette partition est déjà dans votre classeur", "red");
            return;
        }

        int page = Integer.parseInt(numeroString);
        if(eleve.getPartitions().stream().map(Partition::getPage).anyMatch(p -> p == page)) {
            changeTexte(labelClasseur, "Cette page est déjà utilisé", "red");
            return;
        }

        partition.setPage(page);
        partitionDAO.ajouterPartitionPourEleve(eleve.getNumero(), partition.getNumero(), page);
        changeTexte(labelClasseur, "Partition ajoutée", "green");
        numeroTextField.clear();
        eleve.getPartitions().add(partition);
    }

    /**
     * Si on sélectionne une partition va afficher les éléments pour sélectionner la page du classeur et
     * Le bouton pour ajouter le classeur
     */
    @FXML
    private void selectionPartition() {
        if(list.getSelectionModel().getSelectedItem() != null) {
            texte.setVisible(true);
            numeroTextField.setVisible(true);
            numeroLabel.setVisible(true);
            ajouterButton.setVisible(true);
        }
    }

    /**
     * Change le texte
     *
     * @param label Le label à changer
     * @param texte Le texte à changer
     * @param couleur La couleur à changer, vert pour un succès, rouge pour un échec
     */
    private void changeTexte(Label label, String texte, String couleur) {
        label.setStyle("-fx-text-fill: " + couleur + ";");
        label.setText(texte + ".");
    }
}
