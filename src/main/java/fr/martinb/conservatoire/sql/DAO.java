package fr.martinb.conservatoire.sql;

import java.sql.*;

public class DAO {

    private static Connection connection;
    private static Statement statement;

    static {

        String url = "jdbc:mysql://192.168.5.240:3307/Conservatoire";
        String loginBd = "admcons";
        String passwd = "pwadmcons";

        try {
            connection = DriverManager.getConnection(url,loginBd,passwd);
            statement = connection.createStatement();
        } catch(SQLException e){
            System.out.println("SQL Exception : " + e);
        }
    }

    public static Statement getStatement(){
        return statement;
    }

    public static Connection getConnection() {
        return connection;
    }
}
