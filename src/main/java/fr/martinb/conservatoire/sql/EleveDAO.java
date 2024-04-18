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

    /**
     * Renvoie toutes les partitions d'un élève par rapport à son numéro
     *
     * @param numero Numéro de l'élève
     * @return Les partitions
     * @throws SQLException Si la requête n'abouti pas
     */
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

    /**
     * Retourne un élève à partir de son identifiant et son mot de passe
     *
     * @param identifiant Identifiant de l'élève
     * @param mdp Mot de passe de l'élève
     * @return L'élève ou null si les champs renseignés sont incorrects
     * @throws SQLException Si la requête n'abouti pas
     */
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
