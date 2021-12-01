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

    private String idUser;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWindow frame = new MainWindow("", "");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainWindow(String prenom, String nom){

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

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
                    ForgetMeWindow frame = new ForgetMeWindow();
                    frame.setVisible(true);
        	        new ForgetMeWindow();
        		}catch(Exception er) {
                    System.err.println("Cannot access database or respond to the request");
                    er.printStackTrace(System.err);
                }
        	}
        });
        contentPane.add(btnNewButton);

    }



}

