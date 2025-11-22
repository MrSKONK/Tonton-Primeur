# Tonton-Primeur
Logiciel de gestion de stock pour un primeur avec interface utilisateur en Java Swing, base de données MySQL et architecture MVC.
# 📦 Gestion de Stock Primeur - Application Desktop

## 🎯 Description

Application de gestion de stock pour primeur (fruits et légumes) développée en Java avec interface graphique Swing.

## 💻 Prérequis

### Pour les utilisateurs :

- **Java Runtime Environment (JRE) 8 ou supérieur**
  - Télécharger : https://www.java.com/fr/download/
- **MySQL Server** (pour la base de données)
  - Télécharger : https://dev.mysql.com/downloads/installer/

## 📥 Installation

### 1. Télécharger les fichiers

Téléchargez le dossier complet contenant :

- `GestionStockPrimeur.jar`
- `mysql-connector-java-8.0.30.jar`
- `Lancer_Application.bat`
- Dossier `View/assets/` (avec les images)

### 2. Configuration de la base de données

Créez la base de données MySQL avec ce script :

```sql
CREATE DATABASE primeur;
USE primeur;

CREATE TABLE fournisseur (
    id_fournisseur INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE article (
    id_article INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    prix_unitaire DECIMAL(10,2),
    quantite_stock INT,
    id_fournisseur INT,
    FOREIGN KEY (id_fournisseur) REFERENCES fournisseur(id_fournisseur)
);

-- Données de test
INSERT INTO fournisseur VALUES
(1, 'Fruits & Co', '0123456789', 'contact@fruitsco.fr'),
(2, 'Légumes Bio', '0987654321', 'bio@legumes.fr');

INSERT INTO article VALUES
(1, 'Pomme', 'Fruit', 2.50, 100, 1),
(2, 'Carotte', 'Légume', 1.80, 150, 2),
(3, 'Banane', 'Fruit', 2.20, 80, 1);
```

### 3. Configuration de connexion

**⚠️ Important** : Modifiez la connexion MySQL si nécessaire

Par défaut, l'application se connecte à :

- **Hôte** : `127.0.0.1:3306`
- **Utilisateur** : `root`
- **Mot de passe** : _(vide)_
- **Base** : `primeur`

Si vos paramètres sont différents, contactez le développeur pour personnalisation.

## 🚀 Lancement

### Méthode 1 : Double-clic (Windows)

Double-cliquez sur `Lancer_Application.bat`

### Méthode 2 : Ligne de commande

```bash
java -jar GestionStockPrimeur.jar
```

### Méthode 3 : Avec classpath explicite

```bash
java -cp "GestionStockPrimeur.jar;mysql-connector-java-8.0.30.jar" Main
```

## ⚙️ Fonctionnalités

✅ Gestion des articles (CRUD)
✅ Gestion des fournisseurs (CRUD)
✅ Filtrage par type (Fruits/Légumes)
✅ Interface graphique intuitive
✅ Base de données MySQL

## 🛠️ Technologies utilisées

- **Langage** : Java 8+
- **Interface** : Swing / AWT
- **Base de données** : MySQL
- **Connecteur** : JDBC (MySQL Connector/J 8.0.30)
- **Architecture** : MVC (Model-View-Controller)

## ❗ Dépannage

### L'application ne démarre pas

- Vérifiez que Java est installé : `java -version`
- Vérifiez que MySQL est démarré
- Vérifiez que le fichier `mysql-connector-java-8.0.30.jar` est présent

### Erreur de connexion à la base

- Vérifiez que MySQL est actif
- Vérifiez vos identifiants de connexion
- Vérifiez que la base `primeur` existe

### Interface ne s'affiche pas

- Certains systèmes nécessitent : `java -Dsun.java2d.uiScale=1.0 -jar GestionStockPrimeur.jar`

## 👨‍💻 Développeur

**CHAMPAGNE Nathan**

- Portfolio : https://mrchampagne-myportfolio.alwaysdata.net/
- GitHub : https://github.com/MrSKONK

## 📝 Licence

Projet éducatif - Libre d'utilisation pour démonstration

---

**Version** : 1.0.0  
**Date** : Novembre 2025
