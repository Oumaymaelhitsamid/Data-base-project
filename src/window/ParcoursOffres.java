package window;

import com.sun.tools.javac.Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.sql.*;
import java.util.ArrayList;


public class ParcoursOffres extends JFrame {

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JButton btnNewButton;
    private JPanel contentPane;
    private ArrayList<String> path = new ArrayList<String>();

    // For database
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";

    public ParcoursOffres(String accountID, int NUMBER_OF_OFFER) throws SQLException {
        // Frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(15, 15, 600, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Text
        JLabel lblNewLabel = new JLabel("Choisissez une catégorie de produits");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        lblNewLabel.setBounds(15, 15, 500, 40);
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

        // Select each category without mother
        PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT NOMCATEGORIE FROM CATEGORIES MINUS SELECT NOMCATEGORIEFILLE from APOURMERE");
        ResultSet rset = stmt_interrogation.executeQuery();
        // Commit for concurrent access to the database
        conn.commit();
        ArrayList<String> results = new ArrayList<String>();

        // Add the different categories without mother to the ArrayList
        while (rset.next()) {
            results.add(rset.getString(1));
        }

        rset.close();
        stmt_interrogation.close();
        conn.close();

        // Add a button for each category selected previously
        int position = 0;
        for (String result : results) {
            btnNewButton = new JButton(result);
            btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
            btnNewButton.setBounds(10 + (140 * position) % 600, 50 + 40 * (position / 4), 135, 40);
            contentPane.add(btnNewButton);
            position += 1;

            btnNewButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        // path to move back in the application
                        path.add(result);
                        // new frame for daughters
                        ParcoursOffres2 daughters = new ParcoursOffres2(result, path, accountID, NUMBER_OF_OFFER);
                        daughters.setVisible(true);
                        dispose();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }

                }

            });
        }

        // Back button and processing
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

