package window;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.sql.*;
import java.util.*;

public class RecommandationsWindow extends JFrame{

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JButton btnNewButton;
    private JPanel contentPane;

    // For database
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";

    // Define a comparator to sort the TreeMap of the categories
    public static <String, Float extends Comparable<java.lang.Float> > Map<java.lang.String, java.lang.Float> valueSort(final Map<java.lang.String, java.lang.Float> map){
        Comparator<java.lang.String> valueComparator = new Comparator<java.lang.String>(){
            public int compare(java.lang.String k1, java.lang.String k2){
                int comp = map.get(k2).compareTo(map.get(k1));
                if(comp==0){
                    return 1;
                } else {
                    return comp;
                }
            }
        };
        Map<java.lang.String, java.lang.Float> sorted = new TreeMap<java.lang.String, java.lang.Float>(valueComparator);
        sorted.putAll(map);
        return sorted;
    }


    public RecommandationsWindow(String accountID, int NUMBER_OF_OFFER) throws SQLException {

        // Frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(15, 15, 600, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // General recommandations
        generalRecommandations(accountID, NUMBER_OF_OFFER);

        // Print personal recommandations
        personalRecommandations(accountID, NUMBER_OF_OFFER);
    }

    private void generalRecommandations(String accountID, int NUMBER_OF_OFFER){
        try{
            // Text
            JLabel lblNewLabel = new JLabel("Recommandations générales :");
            lblNewLabel.setForeground(Color.BLACK);
            lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            lblNewLabel.setBounds(10, 250, 600, 40);
            contentPane.add(lblNewLabel);

            // Loading of the Oracle Driver
            System.out.print("Loading Oracle driver... ");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("loaded");

            // Connection to the database
            System.out.print("Connecting to the database... ");
            Connection conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
            conn.setAutoCommit(false);
            System.out.println("connected");

            // Number of offer for each category
            PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT idProduit, COUNT(*) FROM OFFRES GROUP BY idProduit");
            ResultSet rset_interrogation = stmt_interrogation.executeQuery();

            // Parameters for store categories recommanded
            PreparedStatement stmt_cat;
            ResultSet rset_cat;
            TreeMap<String, Float> categories = new TreeMap<String, Float>();

            while(rset_interrogation.next()){
                stmt_cat = conn.prepareStatement("SELECT nomCategorie FROM PRODUITS WHERE idProduit = ?");
                stmt_cat.setString(1, rset_interrogation.getString(1));
                rset_cat = stmt_cat.executeQuery();
                if(rset_cat.next() && categories.containsKey(rset_cat.getString(1))){
                    categories.replace(rset_cat.getString(1), categories.get(rset_cat.getString(1)) + rset_interrogation.getInt(2));
                } else{
                    categories.put(rset_cat.getString(1), rset_interrogation.getFloat(2));
                }
                stmt_cat.close();
                rset_cat.close();
            }

            conn.commit();
            stmt_interrogation.close();
            rset_interrogation.close();

            // Each category number is divided by the number of product (to have the mean)
            stmt_interrogation = conn.prepareStatement("SELECT nomCategorie, COUNT(*) FROM PRODUITS GROUP BY nomCategorie");
            rset_interrogation = stmt_interrogation.executeQuery();
            // Parameters for store categories recommanded
            while(rset_interrogation.next()){
                if(categories.containsKey(rset_interrogation.getString(1))){
                    categories.replace(rset_interrogation.getString(1), categories.get(rset_interrogation.getString(1)) / rset_interrogation.getFloat(2));
                }
            }
            conn.commit();
            stmt_interrogation.close();
            rset_interrogation.close();
            conn.close();



            Map sortedMap = valueSort(categories);
            Set set = sortedMap.entrySet();
            Iterator i = set.iterator();

            // One button for each category
            int position = 0;
            while(i.hasNext()){
                Map.Entry mp = (Map.Entry)i.next();
                btnNewButton = new JButton(mp.getKey().toString());
                btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
                btnNewButton.setBounds(10 + (140* position ) % 420, 300 + 50 * (position / 3), 140, 40);
                contentPane.add(btnNewButton);
                position += 1;

                System.out.print(mp.getKey() + ": ");
                System.out.println(mp.getValue());

                btnNewButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try{
                            dispose();
                            ParcoursOffres2 parcours = new ParcoursOffres2(mp.getKey().toString(), new ArrayList<String>(), accountID, NUMBER_OF_OFFER);
                            parcours.setVisible(true);
                        }   catch (Exception ee) {
                            ee.printStackTrace();
                        }

                    }

                });

            }
        } catch(Exception ee){
            ee.printStackTrace();
        }
    }

    private void personalRecommandations(String accountID, int NUMBER_OF_OFFER){
        try{
            // Text
            JLabel lblNewLabel = new JLabel("Recommandations personnelles :");
            lblNewLabel.setForeground(Color.BLACK);
            lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            lblNewLabel.setBounds(10, 10, 600, 40);
            contentPane.add(lblNewLabel);

            // Loading of the Oracle Driver
            System.out.print("Loading Oracle driver... ");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("loaded");

            // Connection to the database
            System.out.print("Connecting to the database... ");
            Connection conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
            conn.setAutoCommit(false);
            System.out.println("connected");

            // Select the categories in order desc by number of offer without buying the product
            PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT nomCategorie, COUNT(*) FROM " +
                    "((SELECT PRODUITS.idProduit, PRODUITS.nomCategorie FROM PRODUITS JOIN OFFRES ON PRODUITS.idProduit = OFFRES.idProduit WHERE idUtilisateur = ?)" +
                    "MINUS (SELECT estRemportePar.idProduit, PRODUITS.nomCategorie FROM estRemportePar JOIN PRODUITS ON PRODUITS.idProduit = estRemportePar.idProduit))" +
                    " GROUP BY nomCategorie ORDER BY COUNT(*) DESC");
            stmt_interrogation.setString(1, accountID);
            ResultSet rset_interrogation = stmt_interrogation.executeQuery();

            // Parameters for store categories recommanded
            ArrayList<String> categories = new ArrayList<String>();
            ArrayList<Integer> number = new ArrayList<Integer>();

            while(rset_interrogation.next()){
                categories.add(rset_interrogation.getString(1));
                number.add(rset_interrogation.getInt(2));
            }

            conn.commit();
            stmt_interrogation.close();
            rset_interrogation.close();
            conn.close();

            // One button for each category recommanded
            int position = 0;
            System.out.println("Personal Offers:");
            for(int i=0; i< categories.size(); i++){
                btnNewButton = new JButton(categories.get(i));
                btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
                btnNewButton.setBounds(10 + (140* position ) % 420, 50 + 50 * (position / 3), 140, 40);
                contentPane.add(btnNewButton);
                position += 1;

                // Print in the console the number of proposition for each category
                System.out.print(categories.get(i) + ": ");
                System.out.println(number.get(i));
                final int cpt = i;
                btnNewButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try{
                            ParcoursOffres2 parcours = new ParcoursOffres2(categories.get(cpt), new ArrayList<String>(), accountID, NUMBER_OF_OFFER);
                            parcours.setVisible(true);
                        }   catch (Exception ee) {
                            ee.printStackTrace();
                        }

                    }

                });

            }
        } catch(Exception ee){
            ee.printStackTrace();
        }

        // Back button
        btnNewButton = new JButton("back");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnNewButton.setBounds(500, 10, 70, 40);
        contentPane.add(btnNewButton);


        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    MainWindow frame = new MainWindow(accountID, NUMBER_OF_OFFER);
                    frame.setVisible(true);
                    dispose();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

            }

        });


    }
}
