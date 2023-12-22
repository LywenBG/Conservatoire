package fr.martinb.conservatoire.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EleveDAO {

    private final Connection connection;
    private PreparedStatement preparedStatement;

    public EleveDAO() {
        connection = DAO.getConnection();
    }

    public ResultSet getPartitions(int numero) throws SQLException {
        preparedStatement = connection.prepareStatement("""
                SELECT PARTITIONS.*, PARTITIONELEVE.NUMEROPAGECLASSEUR FROM PARTITIONS
                JOIN PARTITIONELEVE ON PARTITIONS.PARNUM = PARTITIONELEVE.PARNUM
                JOIN ELEVE on PARTITIONELEVE.ELENUM = ELEVE.ELENUM
                WHERE ELEVE.ELENUM = ?;
            """);
        preparedStatement.setInt(1, numero);

        return preparedStatement.executeQuery();
    }

    public ResultSet getEleve(String identifiant, String mdp) throws SQLException {
        preparedStatement = connection.prepareStatement("""
            SELECT * FROM ELEVE
            WHERE ELELOGIN = ? AND ELEMDP = PASSWORD(?)
            LIMIT 1;
        """);

        preparedStatement.setString(1, identifiant);
        preparedStatement.setString(2, mdp);

        return preparedStatement.executeQuery();
    }
}
