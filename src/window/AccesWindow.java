package window;
import javax.swing.*;

import java.awt.event.*;

public class AccesWindow {



	private String passwd;

	public AccesWindow() {
        final JFrame frame = new JFrame("Bienvenu");


        JLabel lblUser = new JLabel("User Name:");
        JTextField tfUser = new JTextField(20);
        lblUser.setLabelFor(tfUser);

        JLabel lblPassword = new JLabel("Password:");
        final JPasswordField pfPassword = new JPasswordField(20);
        lblPassword.setLabelFor(pfPassword);

        JButton btnGet = new JButton("Display Password");
        btnGet.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        String password = new String(pfPassword.getPassword());
                        JOptionPane.showMessageDialog(frame,
                                "Password is " + password);
                    }
                });
        
        this.passwd = new String(pfPassword.getPassword());
        System.out.println(passwd);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        String password = new String(pfPassword.getPassword());
                        if (passwd == "p") {
                        	JOptionPane.showMessageDialog(frame,
                                    "Password is " + password);
                        }
                        else {
                        	System.out.println(passwd);
                        	JOptionPane.showMessageDialog(frame,
                                    "Wrong password ");
                        	frame.dispose();
                        }
                    }
                });

        JPanel panel = new JPanel();
        panel.setLayout(new SpringLayout());

        panel.add(lblUser);
        panel.add(tfUser);
        panel.add(lblPassword);
        panel.add(pfPassword);
        panel.add(btnLogin);
        panel.add(btnGet);

        SpringUtilities.makeCompactGrid(panel,
                3, 2, //rows, cols
                6, 6, //initX, initY
                6, 6); //xPad, yPad
	
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 120);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
	 }

	public String getPasswd() {
		return passwd;
	}

	 
	 

      
}
