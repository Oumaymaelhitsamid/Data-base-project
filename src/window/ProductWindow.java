package window;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ProductWindow extends JFrame{

    // Graphical elements
    private static final long serialVersionUID = 1L;
    private JTextField enchereField;
    private JButton propositionButton;
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
                    ProductWindow frame = new ProductWindow("2","2", 5);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ProductWindow(String accountID, String productID, int NUMBER_OF_OFFER){
        // Partie graphique...
        // Pour la fenêtre principale
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(450, 190, 1000, 300);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Pour le champ à remplir
        enchereField = new JTextField();
        enchereField.setFont(new Font("Tahoma", Font.PLAIN, 20));
        enchereField.setBounds(10, 50, 300, 40);
        contentPane.add(enchereField);
        enchereField.setColumns(10);

        // Pour le bouton
        propositionButton = new JButton("Proposer un prix");
        propositionButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        propositionButton.setBounds(315, 50, 350, 40);

        // Pour les caractéristiques d'un produit en particulier
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

            PreparedStatement stmt_graphic = conn.prepareStatement("SELECT * FROM PRODUITS WHERE idProduit = ?");
            stmt_graphic.setString(1, productID);
            ResultSet rset = stmt_graphic.executeQuery();

            if(rset.next()){
                // Pour l'intitulé du produit
                JLabel lblNewLabel = new JLabel(rset.getString(2));
                lblNewLabel.setForeground(Color.BLACK);
                lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
                lblNewLabel.setBounds(10, 10, 600, 40);
                contentPane.add(lblNewLabel);
                // Pour le prix de la dernière enchère proposée
                JLabel prixCourant = new JLabel("Dernière enchère : " + rset.getString(3));
                prixCourant.setForeground(Color.BLACK);
                prixCourant.setBackground(Color.CYAN);
                prixCourant.setFont(new Font("Tahoma", Font.PLAIN, 18));
                prixCourant.setBounds(10, 90, 700, 40);
                contentPane.add(prixCourant);
                // Pour la description du produit
                JLabel description = new JLabel("Description : " + rset.getString(4));
                description.setForeground(Color.BLACK);
                description.setBackground(Color.CYAN);
                description.setFont(new Font("Tahoma", Font.PLAIN, 18));
                description.setBounds(10, 130, 900, 40);
                contentPane.add(description);
                // Pour l'URL d'une photo
                JLabel url = new JLabel("URL photo : " + rset.getString(5));
                url.setForeground(Color.BLACK);
                url.setBackground(Color.CYAN);
                url.setFont(new Font("Tahoma", Font.PLAIN, 18));
                url.setBounds(10, 170, 900, 40);
                contentPane.add(url);
            }

            PreparedStatement stmt_graphic2 = conn.prepareStatement("SELECT caracteristique, valeurCarac FROM CARACTERISTIQUES WHERE idProduit = ?");
            stmt_graphic2.setString(1, productID);
            ResultSet rset2 = stmt_graphic2.executeQuery();

            int cpt = 1;
            while(rset2.next()){
                JLabel carac = new JLabel(rset2.getString(1) + " : " + rset2.getString(2) + ", ");
                carac.setForeground(Color.BLACK);
                carac.setBackground(Color.CYAN);
                carac.setFont(new Font("Tahoma", Font.PLAIN, 18));
                carac.setBounds(10 + (cpt - 1) * 150, 210, 200, 40);
                contentPane.add(carac);
                cpt = cpt + 1;
            }

            stmt_graphic.close();

        } catch (SQLException er) {

            System.err.println("Cannot find caracteristics of the product");
            er.printStackTrace(System.err);
        }


        // Transaction to propose a new offer
        propositionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Parsing of the date
                String date = LocalDate.now().toString();
                String time = LocalTime.now().toString();
                System.out.println(time);

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

                    // Price Proposed by the user
                    String priceProposed = enchereField.getText();

                    PreparedStatement stmt_verif1 = conn.prepareStatement("SELECT idUtilisateur FROM OFFRES WHERE idProduit = ? AND prixPropose = (SELECT MAX(prixPropose) FROM OFFRES WHERE idProduit = ?)");
                    stmt_verif1.setString(1, productID);
                    stmt_verif1.setString(2, productID);
                    ResultSet rset_verif1 = stmt_verif1.executeQuery();

                    PreparedStatement stmt_verif2 = conn.prepareStatement("SELECT prixCourant FROM PRODUITS WHERE idProduit = ?");
                    stmt_verif2.setString(1, productID);
                    ResultSet rset_verif2 = stmt_verif2.executeQuery();

                    if(rset_verif1.next() && rset_verif1.getString(1).equals(accountID)){
                        System.out.println("Last proposition is already yours.");
                    }
                    else if(rset_verif2.next() && Integer.parseInt(rset_verif2.getString(1)) >= Integer.parseInt(priceProposed)){
                        System.out.println("Your proposition must be higher than the previous one.");
                    }
                    else{
                        PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT * FROM ESTREMPORTEPAR WHERE idProduit = ?");
                        stmt_interrogation.setString(1, productID);
                        ResultSet rset_interrogation = stmt_interrogation.executeQuery();

                        if(rset_interrogation.next()){
                            System.out.println("The product is already bought");
                        } else {
                            PreparedStatement stmt_interrogation2 = conn.prepareStatement("SELECT COUNT(*) FROM OFFRES WHERE idProduit = ?");
                            stmt_interrogation2.setString(1, productID);
                            ResultSet rset_interrogation2 = stmt_interrogation2.executeQuery();
                            if (rset_interrogation2.next()){
                                int offerNumber = Integer.parseInt(rset_interrogation2.getString(1)) + 1;
                                if(offerNumber == NUMBER_OF_OFFER){
                                    PreparedStatement stmt_insertion = conn.prepareStatement("INSERT INTO ESTREMPORTEPAR VALUES (?, ?, ?)");
                                    stmt_insertion.setString(1, productID);
                                    stmt_insertion.setString(2, date);
                                    stmt_insertion.setString(3, time);
                                    stmt_insertion.executeQuery();
                                    stmt_insertion.close();
                                    conn.commit();
                                }
                            }

                            PreparedStatement stmt_insertion2 = conn.prepareStatement("INSERT INTO OFFRES VALUES (?, ?, ?, ?, ?)");
                            stmt_insertion2.setString(1, productID);
                            stmt_insertion2.setString(2, date);
                            stmt_insertion2.setString(3, time);
                            stmt_insertion2.setString(4, priceProposed);
                            stmt_insertion2.setString(5, accountID);
                            stmt_insertion2.executeQuery();

                            PreparedStatement stmt_insertion3 = conn.prepareStatement("UPDATE PRODUITS SET prixCourant = ? WHERE idProduit = ?");
                            stmt_insertion3.setString(1, priceProposed);
                            stmt_insertion3.setString(2, productID);
                            stmt_insertion3.executeQuery();
                            stmt_insertion3.close();

                            conn.commit();
                            System.out.println("Your proposition is accepted");
                        }
                    }
                    conn.close();


                } catch (SQLException er) {

                    System.err.println("Cannot connect to the database or cannot add the offer");
                    er.printStackTrace(System.err);
                }

            }
        });

        contentPane.add(propositionButton);

        label = new JLabel("");
        label.setBounds(0, 0, 1008, 562);
        contentPane.add(label);
    }


}
