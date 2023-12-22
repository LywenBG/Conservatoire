package fr.martinb.conservatoire.controller;

import fr.martinb.conservatoire.App;
import fr.martinb.conservatoire.modele.Eleve;
import fr.martinb.conservatoire.sql.EleveDAO;
import fr.martinb.conservatoire.modele.Partition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnexionController {

    @FXML
    private TextField identifiant;

    @FXML
    private PasswordField mdp;

    @FXML
    private Label erreur;

    @FXML
    private void seConnecter() throws SQLException, IOException {
        if(identifiant.getText().isEmpty() || mdp.getText().isEmpty()) {
            erreur.setText("Veuillez remplir tous les champs.");
            return;
        }

        EleveDAO eleveDAO = new EleveDAO();
        ResultSet resultSet = eleveDAO.getEleve(identifiant.getText(), mdp.getText());

        if(!resultSet.next()) {
            erreur.setText("Identifiant et/ou mot de passe incorrect(s).");
            return;
        }

        int numero = resultSet.getInt("ELENUM");
        Eleve eleve = new Eleve(
                numero,
                resultSet.getString("ELENOM"),
                resultSet.getString("ELEPRENOM"),
                resultSet.getInt("ELECYCLE"),
                resultSet.getInt("ELEANNEECYCLE"),
                resultSet.getString("DISNUM"));

        resultSet = eleveDAO.getPartitions(numero);
        List<Partition> partitions = new ArrayList<>();

        while(resultSet.next()) {
            partitions.add(new Partition(
                    resultSet.getInt("PARNUM"),
                    resultSet.getString("PARNOM"),
                    resultSet.getString("PARAUTEUR"),
                    resultSet.getInt("NUMEROPAGECLASSEUR")));
        }
        eleve.getPartitions().addAll(partitions);

        App.setEleve(eleve);
        App.setRoot("partitions");
        App.getStage().setTitle("Partitions");
    }
}
