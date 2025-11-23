package View;

import Controller.ArticleDAO;
import Controller.FournisseurDAO;
import Model.Article;
import Model.Fournisseur;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.File;

public class ArticleView extends JFrame {

    private ArticleDAO articleDAO;
    private FournisseurDAO fournisseurDAO;
    private JPanel mainContentPanel;
    private JPanel articlesPanel;
    private JComboBox<String> filtreComboBox;

    public ArticleView() {
        articleDAO = new ArticleDAO();
        fournisseurDAO = new FournisseurDAO();

        // Configuration de la fenêtre
        setTitle("Gestion de Stock Tonton Primeur");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        setLayout(new BorderLayout());

        // Créer le menu latéral
        JPanel menuLateral = creerMenuLateral();
        add(menuLateral, BorderLayout.WEST);

        // Créer le contenu principal
        mainContentPanel = new JPanel(new BorderLayout());
        afficherGestionArticles();
        add(mainContentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel creerMenuLateral() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(250, getHeight()));
        menuPanel.setBackground(new Color(45, 45, 45));
        menuPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Logo
        JLabel logoLabel = new JLabel();
        File logoFile = new File("./View/assets/Logo Tonton Primeur.png");
        if (logoFile.exists()) {
            ImageIcon logoIcon = new ImageIcon("./View/assets/Logo Tonton Primeur.png");
            Image img = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } else {
            logoLabel.setText("TONTON PRIMEUR");
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(logoLabel);

        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Bouton Gestion des Articles
        JButton btnArticles = new JButton("Gestion des Articles");
        btnArticles.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnArticles.setMaximumSize(new Dimension(200, 40));
        btnArticles.setBackground(new Color(76, 175, 80));
        btnArticles.setForeground(Color.WHITE);
        btnArticles.setFocusPainted(false);
        btnArticles.setFont(new Font("Arial", Font.BOLD, 14));
        btnArticles.addActionListener(e -> afficherGestionArticles());
        menuPanel.add(btnArticles);

        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Bouton Gestion des Fournisseurs
        JButton btnFournisseurs = new JButton("Gestion des Fournisseurs");
        btnFournisseurs.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFournisseurs.setMaximumSize(new Dimension(200, 40));
        btnFournisseurs.setBackground(new Color(33, 150, 243));
        btnFournisseurs.setForeground(Color.WHITE);
        btnFournisseurs.setFocusPainted(false);
        btnFournisseurs.setFont(new Font("Arial", Font.BOLD, 14));
        btnFournisseurs.addActionListener(e -> afficherGestionFournisseurs());
        menuPanel.add(btnFournisseurs);

        return menuPanel;
    }

    private JLabel obtenirImageArticle(Article article) {
        JLabel imgLabel = new JLabel("", SwingConstants.CENTER);

        // Recherche automatique de l'image
        String nomNormalise = article.getNom().toLowerCase().trim()
                .replace(" ", "_")
                .replace("é", "e")
                .replace("è", "e")
                .replace("ê", "e")
                .replace("ë", "e")
                .replace("à", "a")
                .replace("â", "a")
                .replace("ä", "a")
                .replace("ô", "o")
                .replace("ö", "o")
                .replace("û", "u")
                .replace("ü", "u")
                .replace("ï", "i")
                .replace("î", "i")
                .replace("ç", "c");
        String dossier = "";

        // Déterminer le dossier en fonction du type
        if (dossier.isEmpty()) {
            if (article.getType().equalsIgnoreCase("fruit")) {
                dossier = "View/assets/Fruits/";
            } else if (article.getType().equalsIgnoreCase("legume") || article.getType().equalsIgnoreCase("légume")) {
                dossier = "View/assets/Legumes/";
            }
        }

        if (!dossier.isEmpty()) {
            String[] extensions = {".png", ".jpg", ".jpeg"};
            boolean imageChargee = false;
            File imageTrouvee = null;

            // Essayer d'abord avec le nom complet normalisé
            for (String ext : extensions) {
                File imageFile = new File(dossier + nomNormalise + ext);
                if (imageFile.exists()) {
                    imageTrouvee = imageFile;
                    imageChargee = true;
                    break;
                }
            }

            // Si pas trouvé, chercher avec le premier mot du nom
            if (!imageChargee) {
                String premierMot = article.getNom().toLowerCase().split(" ")[0];
                for (String ext : extensions) {
                    File imageFile = new File(dossier + premierMot + ext);
                    if (imageFile.exists()) {
                        imageTrouvee = imageFile;
                        imageChargee = true;
                        break;
                    }
                }
            }

            // Charger et afficher l'image si trouvée
            if (imageChargee && imageTrouvee != null) {
                try {
                    ImageIcon icon = new ImageIcon(imageTrouvee.getAbsolutePath());
                    if (icon.getImageLoadStatus() == MediaTracker.COMPLETE && icon.getIconWidth() > 0) {
                        int largeurMax = 230;
                        int hauteurMax = 120;
                        int largeurOriginale = icon.getIconWidth();
                        int hauteurOriginale = icon.getIconHeight();

                        double ratioLargeur = (double) largeurMax / largeurOriginale;
                        double ratioHauteur = (double) hauteurMax / hauteurOriginale;
                        double ratio = Math.min(ratioLargeur, ratioHauteur);

                        int nouvelleLargeur = (int) (largeurOriginale * ratio);
                        int nouvelleHauteur = (int) (hauteurOriginale * ratio);

                        Image img = icon.getImage().getScaledInstance(nouvelleLargeur, nouvelleHauteur, Image.SCALE_SMOOTH);
                        imgLabel.setIcon(new ImageIcon(img));
                        return imgLabel;
                    }
                } catch (Exception e) {
                    // Erreur de chargement
                }
            }
        }

        // Aucune image trouvée
        imgLabel.setText("Pas d'image");
        imgLabel.setForeground(Color.GRAY);
        imgLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        return imgLabel;
    }

    private void afficherGestionArticles() {
        mainContentPanel.removeAll();

        // Panel pour le filtre et les boutons
        JPanel filtrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        filtrePanel.setBackground(Color.WHITE);

        JLabel lblFiltre = new JLabel("Trier par :");
        lblFiltre.setFont(new Font("Arial", Font.PLAIN, 14));

        String[] options = {"Aucun tri", "prix croissant", "prix décroissant", "nom A-Z"};
        filtreComboBox = new JComboBox<>(options);
        filtreComboBox.setPreferredSize(new Dimension(150, 30));
        filtreComboBox.addActionListener(e -> appliquerFiltre());

        filtrePanel.add(lblFiltre);
        filtrePanel.add(filtreComboBox);

        // Bouton Modifier
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setPreferredSize(new Dimension(100, 30));
        btnModifier.setBackground(new Color(255, 152, 0));
        btnModifier.setForeground(Color.WHITE);
        btnModifier.setFocusPainted(false);
        btnModifier.addActionListener(e -> ouvrirFormulaireModifierArticle());
        filtrePanel.add(btnModifier);

        // Bouton Supprimer
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setPreferredSize(new Dimension(100, 30));
        btnSupprimer.setBackground(new Color(244, 67, 54));
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.addActionListener(e -> ouvrirFormulaireSupprimerArticle());
        filtrePanel.add(btnSupprimer);

        mainContentPanel.add(filtrePanel, BorderLayout.NORTH);

        // Panel scrollable pour les articles
        articlesPanel = new JPanel();
        articlesPanel.setLayout(new GridLayout(0, 3, 20, 20));
        articlesPanel.setBackground(Color.WHITE);
        articlesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        chargerArticles();

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void chargerArticles() {
        articlesPanel.removeAll();

        List<Article> articles = articleDAO.ListerArticle();

        for (Article article : articles) {
            articlesPanel.add(creerCarteArticle(article));
        }

        // Carte pour ajouter un nouvel article
        articlesPanel.add(creerCarteAjout());

        articlesPanel.revalidate();
        articlesPanel.repaint();
    }

    private void appliquerFiltre() {
        articlesPanel.removeAll();

        String choix = (String) filtreComboBox.getSelectedItem();
        List<Article> articles;

        if (choix.equals("Aucun tri")) {
            articles = articleDAO.ListerArticle();
        } else {
            articles = articleDAO.TrierArticle(choix);
        }

        for (Article article : articles) {
            articlesPanel.add(creerCarteArticle(article));
        }

        // Carte pour ajouter un nouvel article
        articlesPanel.add(creerCarteAjout());

        articlesPanel.revalidate();
        articlesPanel.repaint();
    }

    private JPanel creerCarteArticle(Article article) {
        JPanel carte = new JPanel();
        carte.setLayout(new BorderLayout(5, 5));
        carte.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        carte.setBackground(Color.WHITE);
        carte.setPreferredSize(new Dimension(250, 230));

        // Zone pour l'image
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(250, 130));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        JLabel imgLabel = obtenirImageArticle(article);
        imagePanel.add(imgLabel);

        carte.add(imagePanel, BorderLayout.NORTH);

        // Informations de l'article
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel lblNom = new JLabel(article.getNom() + " (ID: " + article.getId_article() + ")");
        lblNom.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel lblPrix = new JLabel(String.format("Prix: %.2f €", article.getPrix_unitaire()));
        lblPrix.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel lblQuantite = new JLabel("Quantité: " + article.getQuantite_stock());
        lblQuantite.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel lblFournisseur = new JLabel("Fournisseur ID: " + article.getId_fournisseur());
        lblFournisseur.setFont(new Font("Arial", Font.PLAIN, 14));

        infoPanel.add(lblNom);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblPrix);
        infoPanel.add(lblQuantite);
        infoPanel.add(lblFournisseur);

        carte.add(infoPanel, BorderLayout.CENTER);

        // Ajouter un listener pour cliquer sur la carte (pour éditer/voir détails)
        carte.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                carte.setBorder(BorderFactory.createLineBorder(new Color(76, 175, 80), 2));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                carte.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            }
        });

        return carte;
    }

    private JPanel creerCarteAjout() {
        JPanel carte = new JPanel();
        carte.setLayout(new BorderLayout());
        carte.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        carte.setBackground(new Color(250, 250, 250));
        carte.setPreferredSize(new Dimension(250, 200));

        JLabel lblPlus = new JLabel("+", SwingConstants.CENTER);
        lblPlus.setFont(new Font("Arial", Font.BOLD, 60));
        lblPlus.setForeground(new Color(76, 175, 80));

        JLabel lblTexte = new JLabel("Ajouter Article", SwingConstants.CENTER);
        lblTexte.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(250, 250, 250));

        lblPlus.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTexte.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblPlus);
        centerPanel.add(lblTexte);
        centerPanel.add(Box.createVerticalGlue());

        carte.add(centerPanel, BorderLayout.CENTER);

        carte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ouvrirFormulaireAjoutArticle();
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                carte.setBorder(BorderFactory.createLineBorder(new Color(76, 175, 80), 2));
                carte.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                carte.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
                carte.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return carte;
    }

    private void ouvrirFormulaireAjoutArticle() {
        JDialog dialog = new JDialog(this, "Ajouter un Article", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblNom = new JLabel("Nom :");
        JTextField txtNom = new JTextField();

        JLabel lblType = new JLabel("Type :");
        JTextField txtType = new JTextField();

        JLabel lblPrix = new JLabel("Prix Unitaire :");
        JTextField txtPrix = new JTextField();

        JLabel lblQuantite = new JLabel("Quantité :");
        JTextField txtQuantite = new JTextField();

        JLabel lblFournisseur = new JLabel("ID Fournisseur :");
        JTextField txtFournisseur = new JTextField();

        JButton btnAjouter = new JButton("Ajouter");
        JButton btnAnnuler = new JButton("Annuler");

        formPanel.add(lblNom);
        formPanel.add(txtNom);
        formPanel.add(lblType);
        formPanel.add(txtType);
        formPanel.add(lblPrix);
        formPanel.add(txtPrix);
        formPanel.add(lblQuantite);
        formPanel.add(txtQuantite);
        formPanel.add(lblFournisseur);
        formPanel.add(txtFournisseur);
        formPanel.add(btnAnnuler);
        formPanel.add(btnAjouter);

        btnAjouter.setBackground(new Color(76, 175, 80));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.addActionListener(e -> {
            try {
                String nom = txtNom.getText();
                String type = txtType.getText();
                double prix = Double.parseDouble(txtPrix.getText());
                int quantite = Integer.parseInt(txtQuantite.getText());
                int idFournisseur = Integer.parseInt(txtFournisseur.getText());

                Article nouvelArticle = new Article(0, nom, type, prix, quantite, idFournisseur);
                articleDAO.AjouterArticle(nouvelArticle);

                JOptionPane.showMessageDialog(dialog, "Article ajouté avec succès !");
                dialog.dispose();
                appliquerFiltre(); // Rafraîchir la liste
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur : Vérifiez les valeurs numériques !", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAnnuler.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void ouvrirFormulaireModifierArticle() {
        // Récupérer l'ID de l'article à modifier
        String idStr = JOptionPane.showInputDialog(this, "Entrez l'ID de l'article à modifier :");
        if (idStr == null || idStr.trim().isEmpty()) {
            return;
        }

        try {
            int idArticle = Integer.parseInt(idStr);

            // Chercher l'article dans la liste
            List<Article> articles = articleDAO.ListerArticle();
            Article articleAModifier = null;
            for (Article a : articles) {
                if (a.getId_article() == idArticle) {
                    articleAModifier = a;
                    break;
                }
            }

            if (articleAModifier == null) {
                JOptionPane.showMessageDialog(this, "Article non trouvé !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Créer le formulaire de modification
            JDialog dialog = new JDialog(this, "Modifier un Article", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(400, 350);
            dialog.setLocationRelativeTo(this);

            JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel lblNom = new JLabel("Nom :");
            JTextField txtNom = new JTextField(articleAModifier.getNom());

            JLabel lblType = new JLabel("Type :");
            JTextField txtType = new JTextField(articleAModifier.getType());

            JLabel lblPrix = new JLabel("Prix Unitaire :");
            JTextField txtPrix = new JTextField(String.valueOf(articleAModifier.getPrix_unitaire()));

            JLabel lblQuantite = new JLabel("Quantité :");
            JTextField txtQuantite = new JTextField(String.valueOf(articleAModifier.getQuantite_stock()));

            JLabel lblFournisseur = new JLabel("ID Fournisseur :");
            JTextField txtFournisseur = new JTextField(String.valueOf(articleAModifier.getId_fournisseur()));

            JButton btnModifier = new JButton("Modifier");
            JButton btnAnnuler = new JButton("Annuler");

            formPanel.add(lblNom);
            formPanel.add(txtNom);
            formPanel.add(lblType);
            formPanel.add(txtType);
            formPanel.add(lblPrix);
            formPanel.add(txtPrix);
            formPanel.add(lblQuantite);
            formPanel.add(txtQuantite);
            formPanel.add(lblFournisseur);
            formPanel.add(txtFournisseur);
            formPanel.add(btnAnnuler);
            formPanel.add(btnModifier);

            btnModifier.setBackground(new Color(255, 152, 0));
            btnModifier.setForeground(Color.WHITE);

            final Article finalArticle = articleAModifier;
            btnModifier.addActionListener(e -> {
                try {
                    String nom = txtNom.getText();
                    String type = txtType.getText();
                    double prix = Double.parseDouble(txtPrix.getText());
                    int quantite = Integer.parseInt(txtQuantite.getText());
                    int idFournisseur = Integer.parseInt(txtFournisseur.getText());

                    Article articleModifie = new Article(finalArticle.getId_article(), nom, type, prix, quantite, idFournisseur);
                    articleDAO.mettreAjourArticle(articleModifie);

                    JOptionPane.showMessageDialog(dialog, "Article modifié avec succès !");
                    dialog.dispose();
                    appliquerFiltre();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Erreur : Vérifiez les valeurs numériques !", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnAnnuler.addActionListener(e -> dialog.dispose());

            dialog.add(formPanel, BorderLayout.CENTER);
            dialog.setVisible(true);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ouvrirFormulaireSupprimerArticle() {
        String idStr = JOptionPane.showInputDialog(this, "Entrez l'ID de l'article à supprimer :");
        if (idStr == null || idStr.trim().isEmpty()) {
            return;
        }

        try {
            int idArticle = Integer.parseInt(idStr);

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Êtes-vous sûr de vouloir supprimer l'article ID " + idArticle + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                articleDAO.SupprimerArticle(idArticle);
                JOptionPane.showMessageDialog(this, "Article supprimé avec succès !");
                appliquerFiltre();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherGestionFournisseurs() {
        mainContentPanel.removeAll();

        // Panel pour les boutons d'action
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        actionPanel.setBackground(Color.WHITE);

        JLabel lblTitre = new JLabel("Gestion des Fournisseurs");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 18));
        actionPanel.add(lblTitre);

        // Bouton Modifier
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setPreferredSize(new Dimension(100, 30));
        btnModifier.setBackground(new Color(255, 152, 0));
        btnModifier.setForeground(Color.WHITE);
        btnModifier.setFocusPainted(false);
        btnModifier.addActionListener(e -> ouvrirFormulaireModifierFournisseur());
        actionPanel.add(btnModifier);

        // Bouton Supprimer
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setPreferredSize(new Dimension(100, 30));
        btnSupprimer.setBackground(new Color(244, 67, 54));
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.addActionListener(e -> ouvrirFormulaireSupprimerFournisseur());
        actionPanel.add(btnSupprimer);

        mainContentPanel.add(actionPanel, BorderLayout.NORTH);

        // Panel scrollable pour les fournisseurs
        JPanel fournisseursPanel = new JPanel();
        fournisseursPanel.setLayout(new GridLayout(0, 3, 20, 20));
        fournisseursPanel.setBackground(Color.WHITE);
        fournisseursPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(fournisseursPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        chargerFournisseurs(fournisseursPanel);

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void chargerFournisseurs(JPanel fournisseursPanel) {
        fournisseursPanel.removeAll();

        List<Fournisseur> fournisseurs = fournisseurDAO.ListerFournisseur();

        for (Fournisseur fournisseur : fournisseurs) {
            fournisseursPanel.add(creerCarteFournisseur(fournisseur));
        }

        // Carte pour ajouter un nouveau fournisseur
        fournisseursPanel.add(creerCarteAjoutFournisseur(fournisseursPanel));

        fournisseursPanel.revalidate();
        fournisseursPanel.repaint();
    }

    private JPanel creerCarteFournisseur(Fournisseur fournisseur) {
        JPanel carte = new JPanel();
        carte.setLayout(new BorderLayout(10, 10));
        carte.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        carte.setBackground(Color.WHITE);
        carte.setPreferredSize(new Dimension(250, 220));

        // Informations du fournisseur
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblNom = new JLabel(fournisseur.getNom() + " (ID: " + fournisseur.getId_fournisseur() + ")");
        lblNom.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel lblTelephone = new JLabel("Tél: " + fournisseur.getTelephone());
        lblTelephone.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblEmail = new JLabel("Email: " + fournisseur.getEmail());
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblAdresse = new JLabel("<html>Adresse: " + fournisseur.getAdresse() + "</html>");
        lblAdresse.setFont(new Font("Arial", Font.PLAIN, 13));

        infoPanel.add(lblNom);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(lblTelephone);
        infoPanel.add(lblEmail);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblAdresse);

        carte.add(infoPanel, BorderLayout.CENTER);

        // Ajouter un listener pour cliquer sur la carte
        carte.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(ArticleView.this,
                        "Fournisseur: " + fournisseur.getNom()
                        + "\nTéléphone: " + fournisseur.getTelephone()
                        + "\nEmail: " + fournisseur.getEmail()
                        + "\nAdresse: " + fournisseur.getAdresse());
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                carte.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                carte.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            }
        });

        return carte;
    }

    private JPanel creerCarteAjoutFournisseur(JPanel fournisseursPanel) {
        JPanel carte = new JPanel();
        carte.setLayout(new BorderLayout());
        carte.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        carte.setBackground(new Color(250, 250, 250));
        carte.setPreferredSize(new Dimension(250, 220));

        JLabel lblPlus = new JLabel("+", SwingConstants.CENTER);
        lblPlus.setFont(new Font("Arial", Font.BOLD, 60));
        lblPlus.setForeground(new Color(33, 150, 243));

        JLabel lblTexte = new JLabel("Ajouter Fournisseur", SwingConstants.CENTER);
        lblTexte.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(250, 250, 250));

        lblPlus.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTexte.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblPlus);
        centerPanel.add(lblTexte);
        centerPanel.add(Box.createVerticalGlue());

        carte.add(centerPanel, BorderLayout.CENTER);

        carte.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ouvrirFormulaireAjoutFournisseur(fournisseursPanel);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                carte.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2));
                carte.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                carte.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
                carte.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return carte;
    }

    private void ouvrirFormulaireAjoutFournisseur(JPanel fournisseursPanel) {
        JDialog dialog = new JDialog(this, "Ajouter un Fournisseur", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblNom = new JLabel("Nom :");
        JTextField txtNom = new JTextField();

        JLabel lblTelephone = new JLabel("Téléphone :");
        JTextField txtTelephone = new JTextField();

        JLabel lblEmail = new JLabel("Email :");
        JTextField txtEmail = new JTextField();

        JLabel lblAdresse = new JLabel("Adresse :");
        JTextField txtAdresse = new JTextField();

        JButton btnAjouter = new JButton("Ajouter");
        JButton btnAnnuler = new JButton("Annuler");

        formPanel.add(lblNom);
        formPanel.add(txtNom);
        formPanel.add(lblTelephone);
        formPanel.add(txtTelephone);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblAdresse);
        formPanel.add(txtAdresse);
        formPanel.add(btnAnnuler);
        formPanel.add(btnAjouter);

        btnAjouter.setBackground(new Color(33, 150, 243));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.addActionListener(e -> {
            String nom = txtNom.getText();
            String telephone = txtTelephone.getText();
            String email = txtEmail.getText();
            String adresse = txtAdresse.getText();

            if (nom.trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Le nom est obligatoire !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Fournisseur nouveauFournisseur = new Fournisseur(0, nom, telephone, email, adresse);
            fournisseurDAO.AjouterFournisseur(nouveauFournisseur);

            JOptionPane.showMessageDialog(dialog, "Fournisseur ajouté avec succès !");
            dialog.dispose();
            chargerFournisseurs(fournisseursPanel);
        });

        btnAnnuler.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void ouvrirFormulaireModifierFournisseur() {
        String idStr = JOptionPane.showInputDialog(this, "Entrez l'ID du fournisseur à modifier :");
        if (idStr == null || idStr.trim().isEmpty()) {
            return;
        }

        try {
            int idFournisseur = Integer.parseInt(idStr);

            List<Fournisseur> fournisseurs = fournisseurDAO.ListerFournisseur();
            Fournisseur fournisseurAModifier = null;
            for (Fournisseur f : fournisseurs) {
                if (f.getId_fournisseur() == idFournisseur) {
                    fournisseurAModifier = f;
                    break;
                }
            }

            if (fournisseurAModifier == null) {
                JOptionPane.showMessageDialog(this, "Fournisseur non trouvé !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JDialog dialog = new JDialog(this, "Modifier un Fournisseur", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);

            JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel lblNom = new JLabel("Nom :");
            JTextField txtNom = new JTextField(fournisseurAModifier.getNom());

            JLabel lblTelephone = new JLabel("Téléphone :");
            JTextField txtTelephone = new JTextField(fournisseurAModifier.getTelephone());

            JLabel lblEmail = new JLabel("Email :");
            JTextField txtEmail = new JTextField(fournisseurAModifier.getEmail());

            JLabel lblAdresse = new JLabel("Adresse :");
            JTextField txtAdresse = new JTextField(fournisseurAModifier.getAdresse());

            JButton btnModifier = new JButton("Modifier");
            JButton btnAnnuler = new JButton("Annuler");

            formPanel.add(lblNom);
            formPanel.add(txtNom);
            formPanel.add(lblTelephone);
            formPanel.add(txtTelephone);
            formPanel.add(lblEmail);
            formPanel.add(txtEmail);
            formPanel.add(lblAdresse);
            formPanel.add(txtAdresse);
            formPanel.add(btnAnnuler);
            formPanel.add(btnModifier);

            btnModifier.setBackground(new Color(255, 152, 0));
            btnModifier.setForeground(Color.WHITE);

            final Fournisseur finalFournisseur = fournisseurAModifier;
            btnModifier.addActionListener(e -> {
                String nom = txtNom.getText();
                String telephone = txtTelephone.getText();
                String email = txtEmail.getText();
                String adresse = txtAdresse.getText();

                Fournisseur fournisseurModifie = new Fournisseur(finalFournisseur.getId_fournisseur(), nom, telephone, email, adresse);
                fournisseurDAO.mettreAjourFournisseur(fournisseurModifie);

                JOptionPane.showMessageDialog(dialog, "Fournisseur modifié avec succès !");
                dialog.dispose();
                afficherGestionFournisseurs();
            });

            btnAnnuler.addActionListener(e -> dialog.dispose());

            dialog.add(formPanel, BorderLayout.CENTER);
            dialog.setVisible(true);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ouvrirFormulaireSupprimerFournisseur() {
        String idStr = JOptionPane.showInputDialog(this, "Entrez l'ID du fournisseur à supprimer :");
        if (idStr == null || idStr.trim().isEmpty()) {
            return;
        }

        try {
            int idFournisseur = Integer.parseInt(idStr);

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Êtes-vous sûr de vouloir supprimer le fournisseur ID " + idFournisseur + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                fournisseurDAO.SupprimerFournisseur(idFournisseur);
                JOptionPane.showMessageDialog(this, "Fournisseur supprimé avec succès !");
                afficherGestionFournisseurs();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
