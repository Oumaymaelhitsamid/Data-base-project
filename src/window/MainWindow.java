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


public class MainWindow extends JFrame{

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;
    
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "guiziova";
    static final String PASSWD = "guiziova";
    

    private String idUser;
    private String prenom;
    private String nom;

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

    public MainWindow(String id) throws SQLException{
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // On va chercher nom et prenom de iduser
        
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
        
        if(rset.next()) {
        	try {
                this.nom = rset.getString(1);
                this.prenom = rset.getString(2);
                System.out.print(nom);
        	}catch (Exception er) {
                er.printStackTrace();
            }
        }

            
          
        JLabel lblNewLabel = new JLabel("Welcome " + prenom + " " + nom);
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 46));
        lblNewLabel.setBounds(350, 13, 600, 93);
        contentPane.add(lblNewLabel);


        btnNewButton = new JButton("Offres");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(200, 200, 150, 73);
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			    ParcoursOffres frame = new ParcoursOffres("", "");
                  frame.setVisible(true);
        	        new ParcoursOffres(prenom, nom);

        		}catch(SQLException er) {
                    System.err.println("Cannot access database or respond to the request");
                    er.printStackTrace(System.err);
                }
        	}
        });
        contentPane.add(btnNewButton);


        btnNewButton = new JButton("Droit à l'oubli");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(500, 200, 250, 73);
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
                    ForgetMeWindow frame = new ForgetMeWindow(id);
                    frame.setVisible(true);
                    frame.Forget();
        		}catch(Exception er) {
                    System.err.println("Cannot access database or respond to the request");
                    er.printStackTrace(System.err);
                }
        	}
        });
        contentPane.add(btnNewButton);

    }



}