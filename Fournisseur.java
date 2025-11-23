package Model;

public class Fournisseur implements CRUD {

    //Attributs
    private int id_fournisseur;
    private String nom;
    private String telephone;
    private String email;
    private String adresse;

    //Constructeur
    public Fournisseur(int id_fournisseur, String nom, String telephone, String email, String adresse) {
        this.id_fournisseur = id_fournisseur;
        this.nom = nom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
    }

    // Getters et Setters
    public int getId_fournisseur() {
        return id_fournisseur;
    }

    public void setId_fournisseur(int id_fournisseur) {
        this.id_fournisseur = id_fournisseur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public void ajouter() {
        //Ajouter un fourniseur
    }

    @Override
    public void mettreAjour() {
        //Mettre à jour un fournisseur
    }

    @Override
    public void supprimer() {
        //Supprimer un fournisseur
    }
}
