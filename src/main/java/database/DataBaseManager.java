package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/ISS-2026";
    private static final String USER = "postgres";
    private static final String PASSWORD = "ccdrt123";

    public static Connection getConnection() {
        try {

            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Eroare la conexiunea cu baza de date: " + e.getMessage());
            return null;
        }
    }
}