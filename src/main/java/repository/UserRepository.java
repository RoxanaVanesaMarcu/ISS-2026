package repository;

import java.sql.*;
import database.DataBaseManager;
import domain.Utilizator;
import domain.Bibliotecar;
import domain.Cititor;

public class UserRepository {


    public Utilizator getUserByUsername(String username) {
        String query = "SELECT * FROM utilizatori WHERE username = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String parolaHash = rs.getString("parola");
                    String email = rs.getString("email");
                    String rol = rs.getString("rol");


                    if ("Bibliotecar".equalsIgnoreCase(rol)) {
                        return new Bibliotecar(id, username, parolaHash, email);
                    } else {
                        return new Cititor(id, username, parolaHash, email);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la preluarea utilizatorului: " + e.getMessage());
        }
        return null;
    }


    public boolean addUser(Utilizator u) {

        String query = "INSERT INTO utilizatori (username, parola, email, rol, cod_acces) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getParola());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getRol());


            if (u instanceof Bibliotecar) {
                stmt.setString(5, "biblioteca1234");
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la adăugarea în DB: " + e.getMessage());
            return false;
        }
    }


}