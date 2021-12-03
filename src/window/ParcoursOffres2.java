package window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class ParcoursOffres2 extends JFrame{

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;

    // For database
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";



    public ParcoursOffres2(String result, ArrayList<String> path, String accountID) throws SQLException {


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(15, 15, 600, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Choisissez un produit ou une catégorie de produits");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        lblNewLabel.setBounds(10, 10, 550, 40);
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

        PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT Nomcategoriefille from apourmere where nomcategoriemere = ?");
        stmt_interrogation.setString(1, result);
        ResultSet rset = stmt_interrogation.executeQuery();
        conn.commit();
        ArrayList<String> results = new ArrayList<String>();

        while (rset.next()){
            results.add(rset.getString(1));

        }

        rset.close();
        stmt_interrogation.close();
        conn.close();


        int position = 0;
        for (String query : results) {
            btnNewButton = new JButton(query);
            btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
            btnNewButton.setBounds(10 + (140 * position) % 600, 50 + 40 * (position / 4), 135, 40);
            contentPane.add(btnNewButton);
            position += 1;

            btnNewButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        path.add(query);
                        ParcoursOffres2 parcours = new ParcoursOffres2(query, path, accountID);
                        parcours.setVisible(true);
                        dispose();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }

                }

            });
        }


        // Loading of the Oracle Driver
        System.out.print("Loading Oracle driver... ");
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        System.out.println("loaded");

        // Connection to the database
        System.out.print("Connecting to the database... ");
        Connection conn2 = DriverManager.getConnection(CONN_URL, USER, PASSWD);
        conn2.setAutoCommit(false);
        System.out.println("connected");


        PreparedStatement stmt_interrogation2 = conn2.prepareStatement("SELECT intitule, idproduit from produits where nomcategorie = ?");

        stmt_interrogation2.setString(1, result);
        ResultSet rset2 = stmt_interrogation2.executeQuery();
        conn2.commit();
        ArrayList<String> results2 = new ArrayList<String>();
        ArrayList<String> idprod = new ArrayList<String>();

        while (rset2.next()){
            results2.add(rset2.getString(1));
            idprod.add(rset2.getString(2));
        }

        rset2.close();
        stmt_interrogation2.close();
        conn2.close();

        int cpt = 0;


        for (String result2 : results2){
            cpt += 1;
            btnNewButton = new JButton(result2);
            btnNewButton.setFont(new Font("Tahoma", Font.ITALIC, 10));
            btnNewButton.setBounds(10 + (140 * position) % 600, 50 + 40 * (position / 4), 135, 40);
            contentPane.add(btnNewButton);
            position += 1;


            int finalCpt = cpt;


            btnNewButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        path.add(result2);
                        ProductWindow product = new ProductWindow(accountID, idprod.get(finalCpt-1), 5);
                        product.setVisible(true);
                        dispose();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }

                }

            });




        };

        for (String chemin : path){
            System.out.println(chemin);
        }

        System.out.println("Le chemin est" + path.get(path.size()-1));


        // Back button
        btnNewButton = new JButton("back");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnNewButton.setBounds(500, 10, 70, 40);
        contentPane.add(btnNewButton);

        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try{
                    if (path.isEmpty() || path.size() ==1) {
                        ParcoursOffres frame = new ParcoursOffres(accountID);
                        frame.setVisible(true);
                        dispose();
                    }else{
                        ParcoursOffres2 frame = new ParcoursOffres2(path.remove(path.size()-2),path, accountID);
                        for (String chemin : path){
                            System.out.println(chemin);
                        }
                        frame.setVisible(true);
                        dispose();
                    }

                    }catch (Exception ee) {
                        ee.printStackTrace();
                    }

                }

            });
    }
}
