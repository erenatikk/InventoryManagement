import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginPage extends JFrame {
    private JLabel usernameLabel, passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Connection conn;

    public LoginPage(Connection conn) {
        this.conn = conn;
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));


        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();


        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticateUser(username, password)) {
                    openInventoryManagementPanel();
                } else {
                    JOptionPane.showMessageDialog(LoginPage.this, "Geçersiz kullanıcı adı veya şifre",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    clearFields();
                }
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());
        add(loginButton);
    }

    private boolean authenticateUser(String username, String password) {

        return username.equals("admin") && password.equals("12345");

    }

    private void openInventoryManagementPanel() {
        setVisible(false); // Mevcut JFrame'i gizle

        JFrame inventoryFrame = new JFrame("Inventory Management"); // Yeni bir JFrame oluştur
        inventoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inventoryFrame.getContentPane().add(new InventoryManagementUI(conn));
        inventoryFrame.pack();
        inventoryFrame.setLocationRelativeTo(null); // Pencereyi ekranın ortasına yerleştirme
        inventoryFrame.setVisible(true);
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Veritabanı bağlantısını sağla
                Connection conn = null;
                try {
                    conn = DatabaseConnector.connectToDatabase();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Veritabanına bağlanırken bir hata oluştu: " + e.getMessage(),
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }

                // Ana pencereyi oluştur
                LoginPage loginPage = new LoginPage(conn);
                loginPage.setTitle("Inventory Management");
                loginPage.pack();
                loginPage.setLocationRelativeTo(null); // Pencereyi ekranın ortasına yerleştirme
                loginPage.setVisible(true);
            }
        });
    }
}
