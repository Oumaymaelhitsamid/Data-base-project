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
    private String accountID;
    private String prenom;

    // For database
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "trouchda";
    static final String PASSWD = "trouchda";



    public ParcoursOffres2(String prenom, String nom) throws SQLException {

        accountID = null;
        this.prenom = prenom;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Choisissez un produit ou une catégorie de produits");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
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

        PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT Nomcategoriefille from apourmere where nomcategoriemere = ?");
        stmt_interrogation.setString(1, prenom);
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
        for (String result : results){
            btnNewButton = new JButton(result);
            btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
            btnNewButton.setBounds(50 + (200*position)%800, 100 + 100*(position/5), 162, 73);
            contentPane.add(btnNewButton);
            position += 1;

            btnNewButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try{
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
                        try {
                            System.out.println("test");
                        } catch (Exception er) {
                            er.printStackTrace();
                        }
                        System.out.println("Login Successfully");


                        rset.close();
                        stmt_interrogation.close();
                        conn.close();
                    }   catch (SQLException er) {
                        System.err.println("Cannot access database or respond to the request");
                        er.printStackTrace(System.err);
                    }

                }

            });

        };
    }
}