package Controller;

import Model.Fournisseur;

import java.sql.*;
import java.util.*;

public class FournisseurDAO {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/primeur";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Bloc statique pour charger le driver MySQL
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL non trouvé : " + e.getMessage());
        }
    }

    public List<Fournisseur> ListerFournisseur() {
        List<Fournisseur> fournisseurs = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            PreparedStatement ps = conn.prepareStatement("SELECT id_fournisseur, nom, telephone, email, adresse FROM fournisseur");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fournisseurs.add(new Fournisseur(
                        rs.getInt("id_fournisseur"),
                        rs.getString("nom"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("adresse")
                ));
            }

            rs.close();
            ps.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }

        return fournisseurs;
    }

    public void AjouterFournisseur(Fournisseur fournisseur) {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps = conn.prepareStatement("INSERT INTO fournisseur (nom, telephone, email, adresse) VALUES (?,?,?,?)");

            // Liaison des paramètres
            ps.setString(1, fournisseur.getNom());
            ps.setString(2, fournisseur.getTelephone());
            ps.setString(3, fournisseur.getEmail());
            ps.setString(4, fournisseur.getAdresse());

            // Exécution de la requête
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du fournisseur : " + e.getMessage());
        } finally {
            System.out.println("Fournisseur ajouté avec succès à votre répertoire !");
        }
    }

    public void mettreAjourFournisseur(Fournisseur fournisseur) {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps = conn.prepareStatement("UPDATE fournisseur SET nom = ?, telephone = ?, email = ?, adresse = ? WHERE id_fournisseur = ?");
            // Liaison des paramètres
            ps.setString(1, fournisseur.getNom());
            ps.setString(2, fournisseur.getTelephone());
            ps.setString(3, fournisseur.getEmail());
            ps.setString(4, fournisseur.getAdresse());
            ps.setInt(5, fournisseur.getId_fournisseur());

            // Exécution de la requête
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour des informations du fournisseur : " + e.getMessage());
        } finally {
            System.out.println("Fournisseur mis à jour avec succès dans votre répertoire !");
        }
    }

    public void SupprimerFournisseur(int id_fournisseur) {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps = conn.prepareStatement("DELETE FROM fournisseur WHERE id_fournisseur = ?");

            // Liaison des paramètres
            ps.setInt(1, id_fournisseur);

            // Exécution de la requête
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du fournisseur : " + e.getMessage());
        } finally {
            System.out.println("Fournisseur supprimé avec succès à votre répertoire !");
        }
    }
}
