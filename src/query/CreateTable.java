package query;

import java.sql.*;
import java.util.Calendar;

public class CreateTable {
    // Pour se connecter à la base de données :
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";

    // Assure que les tables n'existes pas:
    static final String DROP_PRODUITS = "DROP TABLE PRODUITS";
    static final String DROP_CATEGORIES = "DROP TABLE CATEGORIES";
    static final String DROP_OFFRES = "DROP TABLE OFFRES";
    static final String DROP_UTILISATEURS = "DROP TABLE UTILISATEURS";
    static final String DROP_COMPTES = "DROP TABLE COMPTES";
    static final String DROP_CARACTERISTIQUES = "DROP TABLE CARACTERISTIQUES";
    static final String DROP_APOURMERE = "DROP TABLE APOURMERE";
    static final String DROP_ESTREMPORTEPAR = "DROP TABLE ESTREMPORTEPAR";

    // Pour les entités simples :
    static final String CREATE_PRODUITS = "CREATE TABLE PRODUITS(idProduit INT PRIMARY KEY, intitule char(50) NOT NULL," +
            "prixCourant FLOAT CHECK(prixCourant>0)," +
            "description char(100) NOT NULL," +
            "urlPhoto char(50)," +
            "nomCategorie char(30) NOT NULL)";
    static final String CREATE_CATEGORIES = "CREATE TABLE CATEGORIES(nomCategorie char(30) PRIMARY KEY)";
    static final String CREATE_OFFRES = "CREATE TABLE OFFRES(idProduit int," +
            "dateOffre VARCHAR(10)," +
            "heureOffre VARCHAR(20)," +
            "prixPropose FLOAT CHECK (prixPropose>0)," +
            "idUtilisateur INT NOT NULL, PRIMARY KEY(idProduit, dateOffre, heureOffre))";
    static final String CREATE_UTILISATEURS = "CREATE TABLE UTILISATEURS(idUtilisateur INT PRIMARY KEY)";
    static final String CREATE_COMPTES = "CREATE TABLE COMPTES(idUtilisateur INT PRIMARY KEY," +
            "email varchar(40) NOT NULL," +
            "mdp varchar(20) CHECK(length(mdp)>7)," +
            "nom varchar(20) NOT NULL," +
            "prenom varchar(20) NOT NULL," +
            "adresse varchar(100) NOT NULL)";
    static final String CREATE_CARACTERISTIQUES = "CREATE TABLE CARACTERISTIQUES(idProduit int," +
            "caracteristique VARCHAR(20) NOT NULL," +
            "valeurCarac VARCHAR(20) NOT NULL, PRIMARY KEY(idProduit, caracteristique))";

    // Pour les lisaisons type 0..1 :
    static final String CREATE_APOURMERE = "CREATE TABLE aPourMere(nomCategorieFille char(30) PRIMARY KEY," +
            "nomCategorieMere char(30))";
    static final String CREATE_ESTREMPORTEPAR = "CREATE TABLE estRemportePar(idProduit INT," +
            "dateOffre varchar(8)," +
            "heureOffre varchar(6))";

    private static int N = 8;
    private String[] DROP_TABLES = new String[]{DROP_PRODUITS, DROP_CATEGORIES, DROP_OFFRES, DROP_UTILISATEURS,
            DROP_COMPTES, DROP_CARACTERISTIQUES, DROP_APOURMERE, DROP_ESTREMPORTEPAR};
    private String[] CREATE_TABLES = new String[]{CREATE_PRODUITS, CREATE_CATEGORIES, CREATE_OFFRES, CREATE_UTILISATEURS,
            CREATE_COMPTES, CREATE_CARACTERISTIQUES, CREATE_APOURMERE, CREATE_ESTREMPORTEPAR};

    public CreateTable() {
        try {

            // Loading of the Oracle Driver
            System.out.print("Loading Oracle driver... ");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("loaded");

            // Connection to the database
            System.out.print("Connecting to the database... ");
            Connection conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
            conn.setAutoCommit(false);
            System.out.println("connected");

            PreparedStatement stmt_drop;
            PreparedStatement stmt_create;

            for(int index=0; index<N; index++){
                stmt_create = conn.prepareStatement(CREATE_TABLES[index]);
                // Try to create the table
                try {
                    stmt_create.executeQuery();
                    stmt_create.close();
                // If the table already exists, it is dropped before to be created
                } catch (SQLException e) {
                    stmt_drop = conn.prepareStatement(DROP_TABLES[index]);
                    stmt_drop.executeQuery();
                    stmt_drop.close();
                    stmt_create.executeQuery();
                    stmt_create.close();
                }
            }

            System.out.println("Tables created correctly");
            conn.commit();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Tables creation failed");
            e.printStackTrace(System.err);
        }
    }

    public static void main(String args[]){ new CreateTable();}


}
