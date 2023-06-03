import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public ProductDAO(Connection conn) {
    }

    public static void addProduct(Product product) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;


        try {
            conn = DatabaseConnector.connectToDatabase();
            String query = "INSERT INTO product (name, description, quantity, price) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                product.setId(generatedId);
            }

            System.out.println("Ürün başarıyla eklendi.");
        } catch (SQLException e) {
            System.out.println("Ürün ekleme işleminde bir hata oluştu: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Veritabanı bağlantısı kapatılırken bir hata oluştu: " + ex.getMessage());
            }
        }
    }

    public static void updateProduct(Product product) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnector.connectToDatabase();
            String query = "UPDATE product SET  name=? , description = ?, quantity = ?, price = ? WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getId());
            stmt.executeUpdate();
            System.out.println("Ürün başarıyla güncellendi.");
        } catch (SQLException e) {
            System.out.println("Ürün güncelleme işleminde bir hata oluştu: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Veritabanı bağlantısı kapatılırken bir hata oluştu: " + ex.getMessage());
            }
        }
    }

    public static void deleteProduct(int productId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnector.connectToDatabase();
            String query = "DELETE FROM product WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, productId);
            stmt.executeUpdate();
            System.out.println("Ürün başarıyla silindi.");
        } catch (SQLException e) {
            System.out.println("Ürün silme işleminde bir hata oluştu: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Veritabanı bağlantısı kapatılırken bir hata oluştu: " + ex.getMessage());
            }
        }
    }
    public List<Product> searchProducts(String keyword) throws SQLException {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM product WHERE name LIKE ? OR description LIKE ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;

        try {
            DatabaseConnector connector = new DatabaseConnector();
            conn = connector.connectToDatabase(); // Database bağlantısı al

            statement = conn.prepareStatement(query);
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                Product product = new Product(id, name, description, quantity, price);
                productList.add(product);
            }
        } finally {
            // Kaynakları serbest bırak
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return productList;
    }



    public static List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnector.connectToDatabase();
            String query = "SELECT id,name,description,quantity,price FROM product";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString("name");
                String description = rs.getString("description");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                Product product = new Product(id , name, description, quantity, price);
                productList.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Ürünleri sorgulama işleminde bir hata oluştu: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Veritabanı bağlantısı kapatılırken bir hata oluştu: " + ex.getMessage());
            }
        }

        return productList;
    }

    public static Product getProductById(int productId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Product product = null;

        try {
            conn = DatabaseConnector.connectToDatabase();
            String query = "SELECT * FROM product WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                product = new Product(id, name, description, quantity, price);
            }
        } catch (SQLException e) {
            System.out.println("Ürün sorgulama işleminde bir hata oluştu: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Veritabanı bağlantısı kapatılırken bir hata oluştu: " + ex.getMessage());
            }
        }

        return product;
    }
}
