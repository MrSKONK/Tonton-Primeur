package Controller;

import Model.Article;
import java.sql.*;
import java.util.*;

public class ArticleDAO {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/primeur";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Bloc statique pour charger le driver MySQL
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL non trouvé : " + e.getMessage());
            System.err.println("Veuillez ajouter mysql-connector-java au classpath");
        }
    }

    public List<Article> ListerArticle() {
        List<Article> articles = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            PreparedStatement ps = conn.prepareStatement("SELECT id_article, nom, type, prix_unitaire, quantite_stock, id_fournisseur FROM article");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                articles.add(new Article(
                        rs.getInt("id_article"),
                        rs.getString("nom"),
                        rs.getString("type"),
                        rs.getDouble("prix_unitaire"),
                        rs.getInt("quantite_stock"),
                        rs.getInt("id_fournisseur")
                ));
            }

            rs.close();
            ps.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }

        return articles;
    }

    public void AjouterArticle(Article article) {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps = conn.prepareStatement("INSERT INTO article (nom, type, prix_unitaire, quantite_stock,id_fournisseur) VALUES (?,?,?,?,?)");

            // Liaison des paramètres
            ps.setString(1, article.getNom());
            ps.setString(2, article.getType());
            ps.setDouble(3, article.getPrix_unitaire());
            ps.setInt(4, article.getQuantite_stock());
            ps.setInt(5, article.getId_fournisseur());

            // Exécution de la requête
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'article : " + e.getMessage());
        } finally {
            System.out.println("Article ajouté avec succès à votre inventaire !");
        }
    }

    public void mettreAjourArticle(Article article) {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps = conn.prepareStatement("UPDATE article SET nom = ?, type = ?, prix_unitaire = ?, quantite_stock = ?, id_fournisseur = ? WHERE id_article = ?");

            // Liaison des paramètres
            ps.setString(1, article.getNom());
            ps.setString(2, article.getType());
            ps.setDouble(3, article.getPrix_unitaire());
            ps.setInt(4, article.getQuantite_stock());
            ps.setInt(5, article.getId_fournisseur());
            ps.setInt(6, article.getId_article());

            // Exécution de la requête
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'article : " + e.getMessage());
        } finally {
            System.out.println("Article mis à jour avec succès dans votre inventaire !");
        }
    }

    public void SupprimerArticle(int id_article) {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps = conn.prepareStatement("DELETE FROM articles WHERE id_article = ?");

            // Liaison des paramètres
            ps.setInt(1, id_article);

            // Exécution de la requête
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'article : " + e.getMessage());
        } finally {
            System.out.println("Article supprimé avec succès à votre inventaire !");
        }
    }

    public List<Article> TrierArticle(String critere) {
        List<Article> articles = new ArrayList<>();
        String requete = "";

        if (critere.equals("prix croissant")) {
            requete = "SELECT * FROM article ORDER BY prix_unitaire ASC";
        } else if (critere.equals("prix décroissant")) {
            requete = "SELECT * FROM article ORDER BY prix_unitaire DESC";
        } else if (critere.equals("nom A-Z")) {
            requete = "SELECT * FROM article ORDER BY nom ASC";
        } else {
            return ListerArticle(); // Par défaut, retourne la liste non triée
        }

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(requete);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                articles.add(new Article(
                        rs.getInt("id_article"),
                        rs.getString("nom"),
                        rs.getString("type"),
                        rs.getDouble("prix_unitaire"),
                        rs.getInt("quantite_stock"),
                        rs.getInt("id_fournisseur")
                ));
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors du tri des articles : " + e.getMessage());
        }

        return articles;
    }

}
