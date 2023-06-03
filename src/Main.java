import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    Connection conn = DatabaseConnector.connectToDatabase();
                    System.out.println("Veritabanına başarılı bir şekilde bağlandınız!");
                    new LoginPage(conn).setVisible(true);
                    // Veritabanı işlemlerini burada gerçekleştirin




                } catch (SQLException e) {
                    System.out.println("Veritabanına bağlanırken bir hata oluştu: " + e.getMessage());
                }
            }
        });
    }
}
