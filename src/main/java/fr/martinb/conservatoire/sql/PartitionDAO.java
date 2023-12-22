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

    public void ajouterPartitionPourEleve(int eleveNum, int partitionNum, int classeurNum) throws SQLException {
        preparedStatement = connection.prepareStatement("""
            INSERT INTO PARTITIONELEVE VALUES (?, ?, ?);
        """);

        preparedStatement.setInt(1, eleveNum);
        preparedStatement.setInt(2, partitionNum);
        preparedStatement.setInt(3, classeurNum);

        preparedStatement.executeUpdate();
    }

    public void ajouterPartition(String partitionNom, String partitionAuteur) throws SQLException {

        ResultSet resultSet = DAO.getStatement().executeQuery("""
            SELECT MAX(PARNUM) FROM PARTITIONS;
        """);
        resultSet.next();

        preparedStatement = connection.prepareStatement("""
            INSERT INTO PARTITIONS VALUES (?, ?, ?);
        """);

        preparedStatement.setInt(1, resultSet.getInt(1) + 1);
        preparedStatement.setString(2, partitionNom);
        preparedStatement.setString(3, partitionAuteur);

        preparedStatement.executeUpdate();
    }

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
