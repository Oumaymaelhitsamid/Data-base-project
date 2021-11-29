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

public class ForgetMeWindow extends JFrame {

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;

    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ForgetMeWindow frame = new ForgetMeWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ForgetMeWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Pls connect if you want we forget you");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 46));
        lblNewLabel.setBounds(30, 13, 1000, 93);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        textField.setBounds(481, 170, 281, 68);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(481, 286, 281, 68);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("Email");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblUsername.setBounds(250, 166, 193, 52);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblPassword.setBounds(250, 286, 193, 52);
        contentPane.add(lblPassword);

        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(545, 392, 162, 73);

        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String email = textField.getText();
                String password = passwordField.getText();
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

                    PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT idUtilisateur FROM COMPTES WHERE email = ? AND mdp = ?");
                    stmt_interrogation.setString(1, email);
                    stmt_interrogation.setString(2, password);
                    ResultSet rset = stmt_interrogation.executeQuery();

                    if (rset.next()) {
                        try {
                            System.out.println("jarrive bien ici");
                            stmt_interrogation = conn.prepareStatement("DELETE FROM COMPTES WHERE idUtilisateur = ?");
                            stmt_interrogation.setString(1, rset.getString(1));
                            stmt_interrogation.executeQuery();
                            stmt_interrogation.close();

                            PreparedStatement stmt_interrogation_max1 = conn.prepareStatement("SELECT max(idUtilisateur) FROM COMPTES GROUP BY idUtilisateur");
                            ResultSet rset_max1 = stmt_interrogation_max1.executeQuery();
                            stmt_interrogation_max1.close();

                            if (rset_max1.next()) {

                                // Insert the new utilisateur
                                PreparedStatement stmt_insertion = conn.prepareStatement("INSERT INTO UTILISATEUR VALUES (?)");
                                stmt_insertion.setInt(1, Integer.parseInt(rset.getString(1)) + 1);
                                stmt_insertion.executeQuery();
                                stmt_insertion.close();

                                // Update the table
                                PreparedStatement stmt_update = conn.prepareStatement("UPDATE OFFRES SET idUtilisateur = (?) WHERE idUtilisateur = (?)");
                                stmt_update.setString(1, rset.getString(1));
                                stmt_update.setInt(2, Integer.parseInt(rset.getString(1)) + 1);
                                stmt_update.executeQuery();
                                stmt_update.close();
                                conn.commit();

                            }

                        } catch (Exception er) {
                            er.printStackTrace();
                        }
                        System.out.println("Delete successfully");
                    } else {
                        System.out.println("Not well connected");
                    }
                    rset.close();
                    conn.close();
                } catch (SQLException er) {

                    System.err.println("Cannot delete an existing account");
                    er.printStackTrace(System.err);
                }
            }
        });

        contentPane.add(btnNewButton);

        label = new JLabel("");
        label.setBounds(0, 0, 1008, 562);
        contentPane.add(label);


    }
}
