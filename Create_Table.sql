DROP TABLE PRODUITS;
DROP TABLE CATEGORIES;
DROP TABLE OFFRES;
DROP TABLE UTILISATEURS;
DROP TABLE COMPTES;
DROP TABLE CARACTERISTIQUES;
DROP TABLE APOURMERE;
DROP TABLE ESTREMPORTEPAR;

CREATE TABLE PRODUITS(idProduit INT PRIMARY KEY, intitule char(50) NOT NULL,
            prixCourant FLOAT CHECK(prixCourant>0),
            description char(100) NOT NULL,
            urlPhoto char(200),
            nomCategorie char(30) NOT NULL);
            
            
CREATE TABLE CATEGORIES(nomCategorie char(30) PRIMARY KEY);

CREATE TABLE OFFRES(idProduit int,
            dateOffre VARCHAR(10),
            heureOffre VARCHAR(20),
            prixPropose FLOAT CHECK (prixPropose>0),
            idUtilisateur INT NOT NULL, PRIMARY KEY(idProduit, dateOffre, heureOffre));

CREATE TABLE UTILISATEURS(idUtilisateur INT PRIMARY KEY);

CREATE TABLE COMPTES(idUtilisateur INT PRIMARY KEY,
            email char(40) NOT NULL,
            mdp char(20) CHECK(length(mdp)>7),
            nom char(20) NOT NULL,
            prenom char(20) NOT NULL,
            adresse char(100) NOT NULL);
            
            
CREATE TABLE CARACTERISTIQUES(idProduit int,
            caracteristique VARCHAR(20) NOT NULL,
            valeurCarac VARCHAR(20) NOT NULL, PRIMARY KEY(idProduit, caracteristique));

CREATE TABLE aPourMere(nomCategorieFille char(30) PRIMARY KEY,
            nomCategorieMere char(30));

CREATE TABLE estRemportePar(idProduit INT,
            dateOffre varchar(8),
            heureOffre varchar(6));
