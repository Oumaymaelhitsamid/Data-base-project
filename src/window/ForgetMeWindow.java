package window;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ForgetMeWindow extends JFrame {

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JButton btnNewButton;
    private JPanel contentPane;

    // To connect to a certain database
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";
    
    public ForgetMeWindow(String userID, int NUMBER_OF_OFFER){
        // frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(15, 15, 600, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // First text
        JTextArea labelArea = new JTextArea("Voulez-vous supprimer vos données ?");
        labelArea.setEditable(false);
        labelArea.setForeground(Color.BLACK);
        labelArea.setBackground(Color.CYAN);
        labelArea.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        labelArea.setBounds(10, 10, 600, 40);
        contentPane.add(labelArea);

        // Second text
        JTextArea labelArea2 = new JTextArea("Ceci est irréversible");
        labelArea2.setEditable(false);
        labelArea2.setForeground(Color.BLACK);
        labelArea2.setBackground(Color.CYAN);
        labelArea2.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        labelArea2.setBounds(10, 50, 600, 40);
        contentPane.add(labelArea2);

        // button
        btnNewButton = new JButton("Supprimer");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnNewButton.setBounds(10, 90, 140, 40);

        // Action performed when the button is clicked
    	btnNewButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

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

                // Delete the account of the user
                conn.beginRequest();
                PreparedStatement stmt_interrogation = conn.prepareStatement("DELETE FROM COMPTES WHERE idUtilisateur = ?");
                stmt_interrogation.setString(1, userID);
                stmt_interrogation.executeQuery();
                stmt_interrogation.close();
                conn.commit();
                conn.close();

                dispose();
                FirstWindow retour = new FirstWindow(NUMBER_OF_OFFER);
                retour.setVisible(true);


            } catch (SQLException er) {

                System.err.println("Cannot delete the account");
                er.printStackTrace(System.err);
            }
        }
    });
    contentPane.add(btnNewButton);

    }
}