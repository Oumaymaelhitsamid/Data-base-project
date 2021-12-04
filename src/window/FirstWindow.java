package window;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FirstWindow extends JFrame{

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JButton btnNewButton;
    private JButton btnNewButton2;
    private JPanel contentPane;

    public FirstWindow(int NUMBER_OF_OFFER){
        // Frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(15, 15, 600, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Text
        JLabel lblNewLabel = new JLabel("Bienvenue");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        lblNewLabel.setBounds(10, 10, 150, 40);
        contentPane.add(lblNewLabel);

        // Button if the user already has an account
        btnNewButton = new JButton("Se connecter");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNewButton.setBounds(10, 50, 150, 40);

        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
                AccesWindow accessWindow = new AccesWindow(NUMBER_OF_OFFER);
                accessWindow.setVisible(true);
            }
        });
        contentPane.add(btnNewButton);

        // Button to create an account
        btnNewButton2 = new JButton("S'inscrire");
        btnNewButton2.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNewButton2.setBounds(10, 100, 150, 40);

        btnNewButton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                CreateAccountWindow createAccountWindow = new CreateAccountWindow(NUMBER_OF_OFFER);
                createAccountWindow.setVisible(true);
            }
        });
        contentPane.add(btnNewButton2);
    }


}