
import Controller.ArticleDAO;
import Controller.FournisseurDAO;
import Model.Article;
import Model.Fournisseur;
import View.ArticleView;

public class Main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new ArticleView();
        });

    }

}
// pour lancer l'application :
// javac Main.java
// java -classpath ".;mysql-connector-java-8.0.30.jar" Main
