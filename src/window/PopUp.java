package window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PopUp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel lblNewLabel;
    private JPanel contentPane;
    private String message;

    public PopUp (String message) {

        this.message = message;

        // Frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 150);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Text
        JLabel lblNewLabel = new JLabel(message);
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        lblNewLabel.setBounds(10, 10, 500, 60);
        contentPane.add(lblNewLabel);
    }





}
