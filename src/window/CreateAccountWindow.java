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

public class CreateAccountWindow extends JFrame{

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JTextField emailField;
    private JTextField surnameField;
    private JTextField nameField;
    private JTextField adresseField;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton btnNewButton;
    private JPanel contentPane;

    // For database
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";

    public CreateAccountWindow(int NUMBER_OF_OFFER){
        // Frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(15, 15, 600, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Text
        JLabel lblNewLabel = new JLabel("Créer un compte");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        lblNewLabel.setBounds(10, 10, 800, 40);
        contentPane.add(lblNewLabel);

        // Field for surname + Text
        surnameField = new JTextField();
        surnameField.setFont(new Font("Tahoma", Font.PLAIN, 18));
        surnameField.setBounds(250, 60, 280, 40);
        contentPane.add(surnameField);
        surnameField.setColumns(10);

        JLabel lblSurname = new JLabel("Prenom");
        lblSurname.setBackground(Color.BLACK);
        lblSurname.setForeground(Color.BLACK);
        lblSurname.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblSurname.setBounds(10, 60, 240, 40);
        contentPane.add(lblSurname);

        // Field for name + Text
        nameField = new JTextField();
        nameField.setFont(new Font("Tahoma", Font.PLAIN, 18));
        nameField.setBounds(250, 110, 280, 40);
        contentPane.add(nameField);
        nameField.setColumns(20);

        JLabel lblName = new JLabel("Nom");
        lblName.setBackground(Color.BLACK);
        lblName.setForeground(Color.BLACK);
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblName.setBounds(10, 110, 240, 40);
        contentPane.add(lblName);

        // Field for the adresse + Text
        adresseField = new JTextField();
        adresseField.setFont(new Font("Tahoma", Font.PLAIN, 18));
        adresseField.setBounds(250, 160, 280, 40);
        contentPane.add(adresseField);
        adresseField.setColumns(10);

        JLabel lblAdresse = new JLabel("Adresse");
        lblAdresse.setBackground(Color.BLACK);
        lblAdresse.setForeground(Color.BLACK);
        lblAdresse.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblAdresse.setBounds(10, 160, 240, 40);
        contentPane.add(lblAdresse);

        // Field for email + Text
        emailField = new JTextField();
        emailField.setFont(new Font("Tahoma", Font.PLAIN, 18));
        emailField.setBounds(250, 210, 280, 40);
        contentPane.add(emailField);
        emailField.setColumns(10);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBackground(Color.BLACK);
        lblEmail.setForeground(Color.BLACK);
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblEmail.setBounds(10, 210, 240, 40);
        contentPane.add(lblEmail);

        // Field for password + Text
        passwordField1 = new JPasswordField();
        passwordField1.setFont(new Font("Tahoma", Font.PLAIN, 18));
        passwordField1.setBounds(250, 260, 280, 40);
        contentPane.add(passwordField1);

        JLabel lblPassword = new JLabel("Mot de passe");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblPassword.setBounds(10, 260, 240, 40);
        contentPane.add(lblPassword);

        // Field for password confirmation + Text
        passwordField2 = new JPasswordField();
        passwordField2.setFont(new Font("Tahoma", Font.PLAIN, 18));
        passwordField2.setBounds(250, 310, 280, 40);
        contentPane.add(passwordField2);

        JLabel lblPasswordC = new JLabel("Confirmer");
        lblPasswordC.setForeground(Color.BLACK);
        lblPasswordC.setBackground(Color.CYAN);
        lblPasswordC.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblPasswordC.setBounds(10, 310, 240, 40);
        contentPane.add(lblPasswordC);

        // Button which interacts with database to add the new account (if it is correct)
        btnNewButton = new JButton("S'inscrire");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnNewButton.setBounds(10, 360, 520, 40);

        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String password = passwordField1.getText();
                String confirm = passwordField2.getText();
                if (password.length() <= 7){
                    PopUp info = new PopUp("The length of the password must be > 7");
                    info.setVisible(true);
                }
                else if (password.equals(confirm) && emailField.getText().length() != 0 && nameField.getText().length() != 0
                    && surnameField.getText().length() != 0 && adresseField.getText().length() != 0){
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

                        // Get the max from Utilisateurs
                        PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT COUNT(*) FROM UTILISATEURS");
                        ResultSet rset_interrogation = stmt_interrogation.executeQuery();
                        if (rset_interrogation.next()){
                            conn.beginRequest();
                            String newUser = Integer.toString(rset_interrogation.getInt(1) + 1);
                            // Insertion in COMPTES
                            PreparedStatement stmt_insertion = conn.prepareStatement("INSERT INTO COMPTES VALUES (?, ?, ?, ?, ?, ?)");
                            stmt_insertion.setString(1, newUser);
                            stmt_insertion.setString(2, emailField.getText());
                            stmt_insertion.setString(3, password);
                            stmt_insertion.setString(4, nameField.getText());
                            stmt_insertion.setString(5, surnameField.getText());
                            stmt_insertion.setString(6, adresseField.getText());
                            stmt_insertion.executeQuery();
                            stmt_insertion.close();

                            // Insertion in UTILISATEURS
                            stmt_insertion = conn.prepareStatement("INSERT INTO UTILISATEURS VALUES (?)");
                            stmt_insertion.setString(1, newUser);
                            stmt_insertion.executeQuery();
                            stmt_insertion.close();
                            conn.commit();
                            System.out.println("Account create successfully");
                        }
                        stmt_interrogation.close();
                        rset_interrogation.close();
                        conn.close();

                        dispose();
                        FirstWindow firstFrame = new FirstWindow(NUMBER_OF_OFFER);
                        firstFrame.setVisible(true);

                    }   catch (SQLException er) {
                        System.err.println("Cannot create the account, sorry");
                        er.printStackTrace(System.err);
                    }
                }
            }
        });
        contentPane.add(btnNewButton);

        // Back button and processing
        btnNewButton = new JButton("back");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnNewButton.setBounds(500, 10, 70, 40);
        contentPane.add(btnNewButton);


        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    FirstWindow frame = new FirstWindow(NUMBER_OF_OFFER);
                    frame.setVisible(true);
                    dispose();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

            }

        });
    }


}
