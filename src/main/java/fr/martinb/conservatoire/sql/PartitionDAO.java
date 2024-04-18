package fr.martinb.conservatoire.sql;

import fr.martinb.conservatoire.App;
import fr.martinb.conservatoire.modele.Eleve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PartitionDAO {

    private final Connection connection;
    private PreparedStatement preparedStatement;

    public PartitionDAO() {
        connection = DAO.getConnection();
    }

    /**
     * Retourne une partition à partir de son titre et son auteyr
     *
     * @param titre
     * @param auteur
     * @return
     * @throws SQLException
     */
    public ResultSet getPartition(String titre, String auteur) throws SQLException {
        preparedStatement = connection.prepareStatement("""
            SELECT * FROM PARTITIONS
            WHERE PARNOM = ? AND PARAUTEUR = ?
            LIMIT 1;
        """);

        preparedStatement.setString(1, titre);
        preparedStatement.setString(2, auteur);

        return preparedStatement.executeQuery();
    }

    /**
     * Ajoute une partition pour un élève
     *
     * @param eleveNum Le numéro de l'élève
     * @param partitionNum Le numéro de la partition
     * @param classeurNum Le numéro du classeur
     * @throws SQLException Si la requête n'abouti pas
     */
    public void ajouterPartitionPourEleve(int eleveNum, int partitionNum, int classeurNum) throws SQLException {
        preparedStatement = connection.prepareStatement("""
            INSERT INTO PARTITIONELEVE VALUES (?, ?, ?);
        """);

        preparedStatement.setInt(1, eleveNum);
        preparedStatement.setInt(2, partitionNum);
        preparedStatement.setInt(3, classeurNum);

        preparedStatement.executeUpdate();
    }

    /**
     * Ajoute une partition dans la base de donnée
     *
     * @param partitionTitre Le nom de la partition
     * @param partitionAuteur L'auteur de la partition
     * @throws SQLException Si la requête n'abouti pas
     */
    public void ajouterPartition(String partitionTitre, String partitionAuteur) throws SQLException {

        try (ResultSet resultSet = DAO.getStatement().executeQuery("""
                SELECT MAX(PARNUM) FROM PARTITIONS;
            """)) {
            resultSet.next();

            preparedStatement = connection.prepareStatement("""
                INSERT INTO PARTITIONS VALUES (?, ?, ?);
            """);

            preparedStatement.setInt(1, resultSet.getInt(1) + 1);
            preparedStatement.setString(2, partitionTitre);
            preparedStatement.setString(3, partitionAuteur);

            preparedStatement.executeUpdate();
        }
    }

    /**
     * Supprime une partition
     *
     * @param titre Titre de la partition
     * @param auteur Auteur de la partition
     * @throws SQLException Si la requête n'abouti pas
     */
    public void supprimerPartition(String titre, String auteur) throws SQLException {
        ResultSet resultSet = getPartition(titre, auteur);
        if(!resultSet.next()) return;

        Eleve eleve = App.getEleve();
        int numero = resultSet.getInt("PARNUM");

        preparedStatement = connection.prepareStatement("""
            DELETE FROM PARTITIONELEVE
            WHERE ELENUM = ? AND PARNUM = ?
        """);

        preparedStatement.setInt(1, eleve.getNumero());
        preparedStatement.setInt(2, numero);

        eleve.getPartitions().removeIf(p -> p.getNumero() == numero);
        preparedStatement.executeUpdate();
    }
}
