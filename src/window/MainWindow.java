package window;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MainWindow extends JFrame {

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;

    // To connect to a certain database
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";

    public MainWindow(String userID, int NUMBER_OF_OFFER) {
        // Frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(15, 15, 600, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Button for categories. Graphic elements and processing
        btnNewButton = new JButton("Categories");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNewButton.setBounds(10, 50, 200, 40);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    dispose();
                    ParcoursOffres frameOffres = new ParcoursOffres(userID, NUMBER_OF_OFFER);
                    frameOffres.setVisible(true);
                } catch (Exception er) {
                    System.err.println("Connot charge offers");
                    er.printStackTrace(System.err);
                }
            }
        });
        contentPane.add(btnNewButton);

        // Button for recommandations. Graphic elements and processing
        btnNewButton = new JButton("Recommandations");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNewButton.setBounds(10, 100, 200, 40);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    dispose();
                    RecommandationsWindow frameOffres = new RecommandationsWindow(userID, NUMBER_OF_OFFER);
                    frameOffres.setVisible(true);
                } catch (Exception er) {
                    System.err.println("Connot charge recommandations");
                    er.printStackTrace(System.err);
                }
            }
        });
        contentPane.add(btnNewButton);

        // Button for delete the account. Graphic elements and processing
        btnNewButton = new JButton("Supprimer mes données");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNewButton.setBounds(10, 150, 200, 40);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    dispose();
                    ForgetMeWindow frame = new ForgetMeWindow(userID, NUMBER_OF_OFFER);
                    frame.setVisible(true);
                } catch (Exception er) {
                    System.err.println("Cannot access database or respond to the request");
                    er.printStackTrace(System.err);
                }
            }
        });
        contentPane.add(btnNewButton);

        // Connection to the database to print the name/surname of the user
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

            PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT nom, prenom FROM COMPTES WHERE idUtilisateur = ?");
            stmt_interrogation.setString(1, userID);
            ResultSet rset = stmt_interrogation.executeQuery();
            conn.commit();

            if (rset.next()) {
                JLabel lblNewLabel = new JLabel("Bienvenue " + rset.getString(2) + " " + rset.getString(1));
                lblNewLabel.setForeground(Color.BLACK);
                lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
                lblNewLabel.setBounds(10, 10, 500, 40);
                contentPane.add(lblNewLabel);
            }

            stmt_interrogation.close();
            rset.close();
            conn.close();
        } catch (Exception er) {
            System.err.println("Cannot access database or respond to the request");
            er.printStackTrace(System.err);
        }


    }
}