import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class InventoryManagementUI extends JPanel {
    private JLabel nameLabel, descriptionLabel, quantityLabel, priceLabel,searchLabel;
    private JTextField nameField, descriptionField, quantityField, priceField,searchField;
    private JButton addButton, updateProduct,deleteButton,searchButton;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductDAO productDAO;

    public InventoryManagementUI(Connection conn) {
        productDAO = new ProductDAO(conn);
        initializeUI();
        refreshProductTable();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setPreferredSize(new Dimension(1200, 600));

        // Ürün ekleme paneli
        JPanel addProductPanel = new JPanel();
        addProductPanel.setLayout(new GridBagLayout());

        nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField(20);
        quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField(20);
        priceLabel = new JLabel("Price:");
        priceField = new JTextField(20);


        addButton = new JButton("Add Product");
        updateProduct = new JButton("Update Product");
        deleteButton = new JButton("Ürün Sil");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        addProductPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        addProductPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addProductPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        addProductPanel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addProductPanel.add(quantityLabel, gbc);

        gbc.gridx = 1;
        addProductPanel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addProductPanel.add(priceLabel, gbc);

        gbc.gridx = 1;
        addProductPanel.add(priceField, gbc);



        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 0, 0);
        addProductPanel.add(addButton, gbc);

        // Ürün güncelleme butonu
        gbc.gridy = 5;
        updateProduct.setPreferredSize(addButton.getPreferredSize());
        addProductPanel.add(updateProduct, gbc);

        gbc.gridy=6;
        deleteButton.setPreferredSize(addButton.getPreferredSize());
        addProductPanel.add(deleteButton,gbc);

        //ARAMA PANELİ
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");


        gbc.gridx = 0;
        gbc.gridy = 1;
        searchPanel.add(searchLabel,gbc);

        gbc.gridx=1;
        searchPanel.add(searchField,gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 0, 0);
        searchPanel.add(searchButton, gbc);

        // Ürün tablosu paneli
        JPanel productTablePanel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Description");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Price");

        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        productTablePanel.add(scrollPane, BorderLayout.CENTER);

        // Ana paneli oluştur
        JPanel mainPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.SOUTHWEST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 10, 10); // Boşluk için insets değerlerini ayarlayın
        mainPanel.add(addProductPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NORTH;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 10, 5); // Boşluk için insets değerlerini ayarlayın
        mainPanel.add(searchPanel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.SOUTHEAST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 5, 10, 10); // Boşluk için insets değerlerini ayarlayın
        mainPanel.add(productTablePanel, gbc);





        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(mainPanel, gbc);



        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = 0;
                String name = nameField.getText();
                String description = descriptionField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());

                try {
                    // Ürün ekleme işlemini gerçekleştir
                    productDAO.addProduct(new Product(null, name, description, quantity, price));
                    refreshProductTable(); // Tabloyu güncelle
                    clearFields(); // Alanları temizle
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(InventoryManagementUI.this, "Ürün ekleme işleminde bir hata oluştu: " + ex.getMessage(),
                            "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        updateProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Seçili satırın indeksini al
                int selectedRow = productTable.getSelectedRow();

                // Seçili satır varsa devam et
                if (selectedRow != -1) {
                    // Seçili ürünün bilgilerini al
                    int id = (Integer) tableModel.getValueAt(selectedRow, 0);
                    String name = nameField.getText();
                    String description = descriptionField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    double price = Double.parseDouble(priceField.getText());

                    try {
                        // Ürün güncelleme işlemini gerçekleştir
                        productDAO.updateProduct(new Product(id, name, description, quantity, price));
                        refreshProductTable(); // Tabloyu güncelle
                        clearFields(); // Alanları temizle
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(InventoryManagementUI.this, "Ürün güncelleme işleminde bir hata oluştu: " + ex.getMessage(),
                                "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(InventoryManagementUI.this, "Lütfen güncellenecek bir ürün seçin.",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Ürün silme butonu

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Seçili satırın indeksini al
                int selectedRow = productTable.getSelectedRow();

                // Seçili satır varsa devam et
                if (selectedRow != -1) {
                    // Seçili ürünün ID'sini al
                    int productId = (Integer) tableModel.getValueAt(selectedRow, 0);

                    try {
                        // Ürünü silme işlemini gerçekleştir
                        productDAO.deleteProduct(productId);
                        refreshProductTable(); // Tabloyu güncelle
                        clearFields(); // Alanları temizle
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(InventoryManagementUI.this, "Ürün silme işleminde bir hata oluştu: " + ex.getMessage(),
                                "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(InventoryManagementUI.this, "Lütfen silinecek bir ürün seçin.",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                try {
                    List<Product> productList = productDAO.searchProducts(keyword);
                    tableModel.setRowCount(0); // Tabloyu temizle
                    for (Product product : productList) {
                        tableModel.addRow(new Object[]{product.getId(), product.getName(), product.getDescription(),
                                product.getQuantity(), product.getPrice()});
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(InventoryManagementUI.this, "Ürün arama işleminde bir hata oluştu: " + ex.getMessage(),
                            "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

// Arama bileşenlerini arama paneline ekle






    }


    private void refreshProductTable() {
        // Ürünleri sorgula ve tabloyu güncelle
        tableModel.setRowCount(0);
        try {
            List<Product> productList = productDAO.getAllProducts();
            for (Product product : productList) {
                tableModel.addRow(new Object[]{product.getId(), product.getName(), product.getDescription(),
                        product.getQuantity(), product.getPrice()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ürünleri sorgulama işleminde bir hata oluştu: " + e.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        descriptionField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }
}
