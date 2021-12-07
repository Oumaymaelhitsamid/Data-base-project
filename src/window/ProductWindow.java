package window;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.awt.Color;
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
    private JPanel contentPane;
    private JButton btnNewButton;

    // For database
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "arvyp";
    static final String PASSWD = "arvyp";

    public ProductWindow(String accountID, String productID, int NUMBER_OF_OFFER){
        // Frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(15, 15, 600, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Auction field and text
        enchereField = new JTextField();
        enchereField.setFont(new Font("Tahoma", Font.PLAIN, 18));
        enchereField.setBounds(10, 10, 200, 40);
        contentPane.add(enchereField);
        enchereField.setColumns(10);

        // Button to propose a price (processing is made later)
        propositionButton = new JButton("Proposer un prix");
        propositionButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        propositionButton.setBounds(220, 10, 200, 40);

        // For specific caracteristics of the product
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

            // Parameters of the product
            PreparedStatement stmt_graphic = conn.prepareStatement("SELECT * FROM PRODUITS WHERE idProduit = ?");
            stmt_graphic.setString(1, productID);
            ResultSet rset = stmt_graphic.executeQuery();
            // Commit for concurrent access to the database
            conn.commit();

            if(rset.next()){
                // Name
                JLabel lblNewLabel = new JLabel(rset.getString(2));
                lblNewLabel.setForeground(Color.BLACK);
                lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
                lblNewLabel.setBounds(10, 50, 590, 40);
                contentPane.add(lblNewLabel);
                // Last price
                JLabel listPrice = new JLabel("Dernière enchère : " + rset.getString(3));
                listPrice.setForeground(Color.BLACK);
                listPrice.setBackground(Color.CYAN);
                listPrice.setFont(new Font("Tahoma", Font.PLAIN, 18));
                listPrice.setBounds(10, 90, 590, 40);
                contentPane.add(listPrice);
                // Description
                JLabel description = new JLabel("Description : " + rset.getString(4));
                description.setForeground(Color.BLACK);
                description.setBackground(Color.CYAN);
                description.setFont(new Font("Tahoma", Font.PLAIN, 18));
                description.setBounds(10, 130, 590, 40);
                contentPane.add(description);
                // URL
                JLabel url = new JLabel("URL photo : " + rset.getString(5));
                url.setForeground(Color.BLACK);
                url.setBackground(Color.CYAN);
                url.setFont(new Font("Tahoma", Font.PLAIN, 18));
                url.setBounds(10, 170, 590, 40);
                contentPane.add(url);
            }
            stmt_graphic.close();
            rset.close();


            // Select all the caracteristics of a certain product
            PreparedStatement stmt_graphic2 = conn.prepareStatement("SELECT caracteristique, valeurCarac FROM CARACTERISTIQUES WHERE idProduit = ?");
            stmt_graphic2.setString(1, productID);
            ResultSet rset2 = stmt_graphic2.executeQuery();
            // Commit for concurrent access to the database
            conn.commit();

            // Print them
            int position = 0;
            while(rset2.next()){
                JLabel carac = new JLabel(rset2.getString(1) + " : " + rset2.getString(2));
                carac.setForeground(Color.BLACK);
                carac.setBackground(Color.CYAN);
                carac.setFont(new Font("Tahoma", Font.PLAIN, 18));
                carac.setBounds(10 + position * 100, 210, 200, 40);
                contentPane.add(carac);
                position = position + 1;
            }

            stmt_graphic2.close();
            rset2.close();
            conn.close();

        } catch (SQLException er) {

            System.err.println("Cannot find caracteristics of the product");
            er.printStackTrace(System.err);
        }


        // Transaction to propose a new offer
        propositionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Date of the offer
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
                        PopUp info = new PopUp("La dernière enchère pour ce produit est déjà la votre.");
                        info.setVisible(true);
                    }
                    else if(rset_verif2.next() && Integer.parseInt(rset_verif2.getString(1)) >= Integer.parseInt(priceProposed)){
                        PopUp info2 = new PopUp("Le montant de votre proposition doit être plus haut que la dernière proposition");
                        info2.setVisible(true);
                    }
                    else{
                        // Verify if the product is already bought
                        PreparedStatement stmt_interrogation = conn.prepareStatement("SELECT * FROM ESTREMPORTEPAR WHERE idProduit = ?");
                        stmt_interrogation.setString(1, productID);
                        ResultSet rset_interrogation = stmt_interrogation.executeQuery();

                        if(rset_interrogation.next()){
                            System.out.println("The product is already bought");
                        } else {
                            PreparedStatement stmt_interrogation2 = conn.prepareStatement("SELECT COUNT(*) FROM OFFRES WHERE idProduit = ?");
                            stmt_interrogation2.setString(1, productID);
                            ResultSet rset_interrogation2 = stmt_interrogation2.executeQuery();
                            // Insert into estremportepar if the offer is the NUMBER_OF_OFFERth
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
                                    PopUp victory = new PopUp("Bravo, vous avez remporté ce produit !");
                                    victory.setVisible(true);
                                }
                            }
                            stmt_interrogation2.close();
                            rset_interrogation2.close();
                            // Insertion of the offer
                            PreparedStatement stmt_insertion2 = conn.prepareStatement("INSERT INTO OFFRES VALUES (?, ?, ?, ?, ?)");
                            stmt_insertion2.setString(1, productID);
                            stmt_insertion2.setString(2, date);
                            stmt_insertion2.setString(3, time);
                            stmt_insertion2.setString(4, priceProposed);
                            stmt_insertion2.setString(5, accountID);
                            stmt_insertion2.executeQuery();
                            stmt_insertion2.close();
                            // Update of the current price
                            PreparedStatement stmt_update = conn.prepareStatement("UPDATE PRODUITS SET prixCourant = ? WHERE idProduit = ?");
                            stmt_update.setString(1, priceProposed);
                            stmt_update.setString(2, productID);
                            stmt_update.executeQuery();
                            stmt_update.close();


                            conn.commit();
                            PopUp infoEnchere = new PopUp("Votre enchere a bien été acceptée");
                            infoEnchere.setVisible(true);
                            dispose();
                            ProductWindow frame = new ProductWindow(accountID, productID, NUMBER_OF_OFFER);
                            frame.setVisible(true);
                        }
                        stmt_interrogation.close();
                        rset_interrogation.close();
                    }
                    stmt_verif1.close();
                    stmt_verif2.close();
                    rset_verif1.close();
                    rset_verif2.close();
                    conn.close();
                } catch (SQLException er) {
                    System.err.println("Cannot connect to the database or cannot add the offer");
                    er.printStackTrace(System.err);
                }

            }
        });
        contentPane.add(propositionButton);

        // Back button
        btnNewButton = new JButton("back");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnNewButton.setBounds(500, 10, 70, 40);
        contentPane.add(btnNewButton);

        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try{
                    ParcoursOffres frame = new ParcoursOffres(accountID, NUMBER_OF_OFFER);
                    frame.setVisible(true);
                    dispose();
                }catch (Exception ee) {
                    ee.printStackTrace();
                }

            }

        });
    }


}
