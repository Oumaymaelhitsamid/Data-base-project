package window;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.sql.SQLException;


public class MainWindow extends JFrame {

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;

    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";

    private String prenom;
    private String nom;

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWindow frame = new MainWindow("");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainWindow(String id) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(15, 15, 600, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // On va chercher nom et prenom de iduser
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
            stmt_interrogation.setString(1, id);
            ResultSet rset = stmt_interrogation.executeQuery();

            if (rset.next()) {
                this.prenom = rset.getString(2);
                this.nom = rset.getString(1);
                JLabel lblNewLabel = new JLabel("Welcome " + this.prenom + " " + this.nom);
                lblNewLabel.setForeground(Color.BLACK);
                lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
                lblNewLabel.setBounds(10, 10, 500, 40);
                contentPane.add(lblNewLabel);

                btnNewButton = new JButton("Offres");
                btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
                btnNewButton.setBounds(10, 50, 150, 40);
                btnNewButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            setVisible(false);
                            ParcoursOffres frameOffres = new ParcoursOffres(id);
                            frameOffres.setVisible(true);
                        } catch (Exception er) {
                            System.err.println("Connot charge offers");
                            er.printStackTrace(System.err);
                        }
                    }
                });
                contentPane.add(btnNewButton);

                btnNewButton = new JButton("Delete account");
                btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
                btnNewButton.setBounds(10, 100, 150, 40);
                btnNewButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            setVisible(false);
                            ForgetMeWindow frame = new ForgetMeWindow(id);
                            frame.setVisible(true);
                            frame.Forget();
                        } catch (Exception er) {
                            System.err.println("Cannot access database or respond to the request");
                            er.printStackTrace(System.err);
                        }
                    }
                });
                contentPane.add(btnNewButton);
            }

            label = new JLabel("");
            label.setBounds(0, 0, 8, 2);
            contentPane.add(label);
        } catch (Exception er) {
            System.err.println("Cannot access database or respond to the request");
            er.printStackTrace(System.err);
        }


    }
}