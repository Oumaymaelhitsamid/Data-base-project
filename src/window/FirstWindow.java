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

public class FirstWindow extends JFrame{

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JButton btnNewButton;
    private JButton btnNewButton2;
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

    public FirstWindow(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(15, 15, 600, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Welcome");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        lblNewLabel.setBounds(10, 10, 150, 40);
        contentPane.add(lblNewLabel);

        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnNewButton.setBounds(10, 50, 150, 40);

        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                AccesWindow accessWindow = new AccesWindow();
                accessWindow.setVisible(true);
            }
        });
        contentPane.add(btnNewButton);

        btnNewButton2 = new JButton("Register");
        btnNewButton2.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnNewButton2.setBounds(10, 100, 150, 40);

        btnNewButton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                CreateAccountWindow createAccountWindow = new CreateAccountWindow();
                createAccountWindow.setVisible(true);
            }
        });
        contentPane.add(btnNewButton2);

        label = new JLabel("");
        label.setBounds(0, 0, 8, 2);
        contentPane.add(label);
    }


}