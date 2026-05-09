package repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import domain.Carte;
import database.DataBaseManager;

public class BookRepository {


    public List<Carte> getAllBooks() {
        List<Carte> carti = new ArrayList<>();
        String query = "SELECT * FROM carti";

        try (Connection conn = DataBaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                carti.add(mapResultSetToCarte(rs));
            }
        } catch (SQLException e) {
            System.err.println("Eroare la încărcarea catalogului: " + e.getMessage());
        }
        return carti;
    }


    public List<Carte> searchBooks(String coloana, String valoare) {
        List<Carte> rezultate = new ArrayList<>();

        String query = "SELECT * FROM carti WHERE " + coloana + " ILIKE ?";

        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + valoare + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rezultate.add(mapResultSetToCarte(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la căutare: " + e.getMessage());
        }
        return rezultate;
    }


    public boolean addBook(Carte c) {
        String query = "INSERT INTO carti (titlu, autor, isbn, stoc, an_publicare) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, c.getTitlu());
            stmt.setString(2, c.getAutor());
            stmt.setString(3, c.getIsbn());
            stmt.setInt(4, c.getStoc());
            stmt.setInt(5, c.getAnPublicare());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la adăugarea cărții: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteBook(int id) {
        String query = "DELETE FROM carti WHERE id = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la ștergerea cărții: " + e.getMessage());
            return false;
        }
    }


    private Carte mapResultSetToCarte(ResultSet rs) throws SQLException {
        return new Carte(
                rs.getInt("id"),
                rs.getString("titlu"),
                rs.getString("autor"),
                rs.getString("isbn"),
                rs.getInt("stoc"),
                rs.getInt("an_publicare")
        );
    }
    public boolean updateStock(int id, int nouStoc) {
        String query = "UPDATE carti SET stoc = ? WHERE id = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, nouStoc);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la actualizarea stocului: " + e.getMessage());
            return false;
        }
    }
}