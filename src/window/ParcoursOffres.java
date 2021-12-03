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
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;
    private ArrayList<String> path = new ArrayList<String>();

    // For database
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ParcoursOffres frame = new ParcoursOffres("2");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ParcoursOffres(String accountID) throws SQLException {



        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Choisissez une catégorie de produits");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 46));
        lblNewLabel.setBounds(150, 13, 800, 93);
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

        PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT NOMCATEGORIE FROM CATEGORIES MINUS SELECT NOMCATEGORIEFILLE from APOURMERE");
        ResultSet rset = stmt_interrogation.executeQuery();
        conn.commit();
        ArrayList<String> results = new ArrayList<String>();

        while (rset.next()) {
            results.add(rset.getString(1));
        }

        rset.close();
        stmt_interrogation.close();
        conn.close();

        int position = 0;
        for (String result : results) {
            btnNewButton = new JButton(result);
            btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
            btnNewButton.setBounds(50 + (200 * position) % 800, 100 + 100 * (position / 5), 162, 73);
            contentPane.add(btnNewButton);
            position += 1;

            btnNewButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        path.add(result);
                        ParcoursOffres2 parcours = new ParcoursOffres2(result, path, accountID);
                        parcours.setVisible(true);
                        dispose();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }

                }

            });
        }

        btnNewButton = new JButton("back");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(0, 0, 50, 30);
        contentPane.add(btnNewButton);


        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    MainWindow frame = new MainWindow(accountID);
                    frame.setVisible(true);
                    dispose();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

            }

        });

    }
}

