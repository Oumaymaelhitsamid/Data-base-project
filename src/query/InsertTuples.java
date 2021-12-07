package query;

import java.sql.*;
import java.util.Calendar;

public class InsertTuples {
    // Pour se connecter à la base de données :
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";

    private int N = 59;
    private static String[] insertions = new String[]{
            "INSERT INTO PRODUITS VALUES ('1', 'XBOX 360', '250', 'console sortie en 2005, vendu plus de 25 millions de fois en Europe', 'https://www.cdiscount.com/pdt2/8/5/9/1/700x700/0882224035859/rw/console-xbox-360-premium.jpg', 'Console')",
            "INSERT INTO PRODUITS VALUES ('2', 'Playstation 5', '300', 'console sortie en 2020 par Sony Interactive Entertainement', 'https://www.journaldugeek.com/content/uploads/2021/09/template-jdg-2021-09-14t154227-127.jpg', 'Console')",
            "INSERT INTO PRODUITS VALUES ('3', 'Guerre et Paix Partie 1', '12', 'Livre de Tolstoï, partie 1, sur la Russie pendant les guerres Napoleoniennes', 'https://static.fnac-static.com/multimedia/Images/FR/NR/99/dc/03/253081/1540-1/tsp20211125070425/La-Guerre-et-la-Paix-La-Guerre-et-la-Paix-Tome-1.jpg', 'Roman')",
            "INSERT INTO PRODUITS VALUES ('4', 'Guerre et Paix Partie 2', '12', 'Livre de Tolstoï, partie 2,sur la Russie pendant les guerres Napoleoniennes', 'https://static.fnac-static.com/multimedia/Images/FR/NR/13/81/14/1343763/1540-1/tsp20211127072831/La-Guerre-et-la-Paix.jpg', 'Roman')",
            "INSERT INTO PRODUITS VALUES ('5', 'Tintin au Congo', '15', 'Tintin voyage au Congo pour lutter contre des gangsters', 'https://static.fnac-static.com/multimedia/FR/Images_Produits/FR/fnac.com/Visual_Principal_340/5/1/0/9782203001015/tsp20120926112403/Tintin-au-Congo.jpg', 'BD')",
            "INSERT INTO PRODUITS VALUES ('6', 'Peugeot 208', '10000', 'Peugeot occasion, en bonne état, 50 000km', 'https://lh3.googleusercontent.com/proxy/DX4b2G8nfGvU8GEUPs0OCke5iFACCdpci5Og-wF3gdcOkRHuNAECZJjkHgi63mRXYgpQYAgVxt0QNT_iHXm-yGbD5UAO3A4V33wUFVYxUcN4VNSd4WqSaywt', 'Voiture')",
            "INSERT INTO PRODUITS VALUES ('7', 'Renault CLio 4', '11000', 'Clio occasion, en bonne état, 30 000km', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQi2KQfuis52hoVFykNZv5nFfrm4hYa_aVeA&usqp=CAU', 'Voiture')",
            "INSERT INTO PRODUITS VALUES ('8', 'Bugatti veyron', '2500000', 'Occasion en parfaite état, 13000km', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT7tffHD0biW0BKbiEZw_P3Xl-Wcy3EKlssOg&usqp=CAU', 'Bugatti')",
           
                
            "INSERT INTO CATEGORIES VALUES ('Livre')",
            "INSERT INTO CATEGORIES VALUES ('BD')",
            "INSERT INTO CATEGORIES VALUES ('Roman')",
            "INSERT INTO CATEGORIES VALUES ('Console')",
            "INSERT INTO CATEGORIES VALUES ('Voiture')",
            "INSERT INTO CATEGORIES VALUES ('Voiture de sport')",
            "INSERT INTO CATEGORIES VALUES ('Bugatti')",
            "INSERT INTO CATEGORIES VALUES ('Meubles')",
            "INSERT INTO CATEGORIES VALUES ('Peintures')",
            "INSERT INTO CATEGORIES VALUES ('Bijoux')",
            "INSERT INTO CATEGORIES VALUES ('Timbres')",
            "INSERT INTO CATEGORIES VALUES ('Guitares')",
            "INSERT INTO CATEGORIES VALUES ('Autographes')",
            "INSERT INTO CATEGORIES VALUES ('Armes')",
            "INSERT INTO CATEGORIES VALUES ('Monnaies')",
            "INSERT INTO CATEGORIES VALUES ('Pendules')",
            "INSERT INTO CATEGORIES VALUES ('Montres')",

            
            "INSERT INTO APOURMERE VALUES ('BD', 'Livre')",
            "INSERT INTO APOURMERE VALUES ('Roman', 'Livre')",
            "INSERT INTO APOURMERE VALUES ('Bugatti', 'Voiture de sport')",
            "INSERT INTO APOURMERE VALUES ('Voiture de sport', 'Voiture')",

            "INSERT INTO UTILISATEURS VALUES ('1')",
            "INSERT INTO UTILISATEURS VALUES ('2')",
            "INSERT INTO UTILISATEURS VALUES ('3')",
            "INSERT INTO UTILISATEURS VALUES ('4')",
            "INSERT INTO UTILISATEURS VALUES ('5')",

            "INSERT INTO COMPTES VALUES ('1', 'micheldupont@gmail.com', 'micheldupont', 'dupont', 'michel', '2 rue Belgrade Belgique')",
            "INSERT INTO COMPTES VALUES ('2', 'lauriepetit@gmail.com', 'lauriepetit', 'petit', 'laurie', '2 rue Marcel Pagnol Grenoble')",
            "INSERT INTO COMPTES VALUES ('3', 'philippedubois@grenoble-inp.org', 'philippedubois', 'dubois', 'philippe', '7 rue de la Paix Paris')",
            "INSERT INTO COMPTES VALUES ('4', 'clairelefevre@gmail.com', 'clairelefevre', 'lefevre', 'claire', '9 Avenue de Valmy')",
            "INSERT INTO COMPTES VALUES ('5', 'richardleroy@gmail.com', 'richardleroy', 'leroy', 'richard', '13 Rue Arago')",
            


            "INSERT INTO CARACTERISTIQUES VALUES ('1', 'poids', '600g')",
            "INSERT INTO CARACTERISTIQUES VALUES ('1', 'couleur', 'noir')",
            "INSERT INTO CARACTERISTIQUES VALUES ('2', 'poids', '4 Kg')",
            "INSERT INTO CARACTERISTIQUES VALUES ('2', 'couleur', 'noir et blanc')",
            "INSERT INTO CARACTERISTIQUES VALUES ('3', 'nombre de pages', '1023')",
            "INSERT INTO CARACTERISTIQUES VALUES ('3', 'dimension', '10.5x4.2x18')",
            "INSERT INTO CARACTERISTIQUES VALUES ('4', 'nombre de pages', '960')",
            "INSERT INTO CARACTERISTIQUES VALUES ('4', 'dimension', '10.5x4.2x18')",
            "INSERT INTO CARACTERISTIQUES VALUES ('5', 'nombre de pages', '59')",
            "INSERT INTO CARACTERISTIQUES VALUES ('6', 'Type de moteur', 'Diesel')",
            "INSERT INTO CARACTERISTIQUES VALUES ('6', 'Puisssance', '100 ch')",
            "INSERT INTO CARACTERISTIQUES VALUES ('6', 'Puisssance fiscale', '5 CV')",
            "INSERT INTO CARACTERISTIQUES VALUES ('7', 'Puisssance', '90 ch')",
            "INSERT INTO CARACTERISTIQUES VALUES ('7', 'Puisssance fiscale', '5 CV')",
            "INSERT INTO CARACTERISTIQUES VALUES ('8', 'Puisssance', '1001 ch')",
            "INSERT INTO CARACTERISTIQUES VALUES ('8', 'Puisssance fiscale', '119 CV')",
            
            "INSERT INTO OFFRES VALUES ('6', '05/12/2021', '15:34:45.087845637', '10100', '1')",
            "INSERT INTO OFFRES VALUES ('6', '05/12/2021', '20:56:12.015841236', '10250', '2')",
            "INSERT INTO OFFRES VALUES ('6', '05/12/2021', '09:18:05.120474478', '10325', '3')",
            "INSERT INTO OFFRES VALUES ('6', '05/12/2021', '17:05:24.987421214', '10400', '2')"

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
