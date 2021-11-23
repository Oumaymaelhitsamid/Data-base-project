package query;

import java.sql.*;
import java.util.Calendar;

public class InsertTuples {
    // Pour se connecter à la base de données :
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "trouchda";
    static final String PASSWD = "trouchda";

    private int N = 17;
    private static String[] insertions = new String[]{
            "INSERT INTO PRODUITS VALUES ('1', 'XBOX 360', '250', 'console nulle', 'paspourlinstant', 'Console')",
            "INSERT INTO PRODUITS VALUES ('2', 'Playstation 5', '300', 'trop bien', 'nop', 'Console')",
            "INSERT INTO PRODUITS VALUES ('3', 'Guerre et Paix Partie 1', '12', 'Livre de Tolstoï, partie 1', 'plus tard', 'Roman')",
            "INSERT INTO PRODUITS VALUES ('4', 'Guerre et Paix Partie 2', '12', 'Livre de Tolstoï, partie 2', 'un jour peut être', 'Roman')",
            "INSERT INTO PRODUITS VALUES ('5', 'Tintin au Congo', '15', 'Un peu border', 'si dieu le veut', 'BD')",
            "INSERT INTO CATEGORIES VALUES ('Livre')",
            "INSERT INTO CATEGORIES VALUES ('BD')",
            "INSERT INTO CATEGORIES VALUES ('Roman')",
            "INSERT INTO CATEGORIES VALUES ('Consoles')",
            "INSERT INTO APOURMERE VALUES ('BD', 'Livre')",
            "INSERT INTO APOURMERE VALUES ('Roman', 'Livre')",
            "INSERT INTO COMPTES VALUES ('1', 'alanfoiré@gmail.com', 'alanfoiré', 'foiré', 'alan', '2 rue Belgrade Belgique')",
            "INSERT INTO COMPTES VALUES ('2', 'loriefice@gmail.com', 'loriefice', 'fice', 'lorie', '2 rue Marcel Pagnol Grenoble')",
            "INSERT INTO COMPTES VALUES ('3', 'ottograf@grenoble-inp.org', 'ottograf', 'graf', 'otto', '7 rue de la Paix Paris')",
            "INSERT INTO UTILISATEURS VALUES ('1')",
            "INSERT INTO UTILISATEURS VALUES ('2')",
            "INSERT INTO UTILISATEURS VALUES ('3')"
    };

    public InsertTuples() {
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

            PreparedStatement stmt_test;
            for(int i=0; i<N; i++){
                System.out.println("1");
                stmt_test = conn.prepareStatement(insertions[i]);
                stmt_test.executeQuery();
                stmt_test.close();
            }

            System.out.println("Tuples inserted correctly");
            conn.commit();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Insertion of tuples failed");
            e.printStackTrace(System.err);
        }
    }

    public static void main(String args[]){ new InsertTuples();}


}
