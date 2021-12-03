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

public class AccesWindow extends JFrame{

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
 
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AccesWindow frame = new AccesWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AccesWindow(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(15, 15, 600, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Email");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        lblNewLabel.setBounds(10, 10, 100, 40);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 18));
        textField.setBounds(110, 10, 250, 40);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 18));
        passwordField.setBounds(110, 60, 250, 40);
        contentPane.add(passwordField);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblPassword.setBounds(10, 60, 100, 40);
        contentPane.add(lblPassword);

        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnNewButton.setBounds(10, 110, 350, 40);

        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String userName = textField.getText();
                String password = passwordField.getText();
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

                    PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT mdp, idUtilisateur FROM COMPTES WHERE email = ?");
                    stmt_interrogation.setString(1, userName);
                    ResultSet rset = stmt_interrogation.executeQuery();
                    conn.commit();
                    if (rset.next() && rset.getString(1).equals(password)){
                        try {
                            setVisible(false);
                            System.out.println(rset.getString(2));
                            MainWindow mainFrame = new MainWindow(rset.getString(2));
                            mainFrame.setVisible(true);
                        } catch (Exception er) {
                            er.printStackTrace();
                        }
                        System.out.println("Login Successfully");
                    } else {
                        System.out.println("Login Failed, the email and password don't match");
                    }

                    rset.close();
                    stmt_interrogation.close();
                    conn.close();
                }   catch (SQLException er) {
                    System.err.println("Cannot access database or respond to the request");
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