import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Logger;

// CLASS PRODUCT
class Product {
    private String name;
    private String category;
    private double price;

    public Product(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " | " + category + " | Rp " + price;
    }
}

// CLASS STACK MANAGEMENT
class ProductStack {
    private Stack<Product> stack = new Stack<>();
    private LinkedList<Product> list = new LinkedList<>();

    Logger logger = Logger.getLogger("StockLog");

    // TAMBAH PRODUK
    public void push(Product p) {
        stack.push(p);
        list.add(p);

        logger.info("Produk ditambahkan: " + p.getName());
    }

    // HAPUS PRODUK
    public Product pop() throws EmptyStackException {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }

        Product p = stack.pop();
        list.remove(p);

        logger.info("Produk dihapus: " + p.getName());

        return p;
    }

    // GET LIST
    public LinkedList<Product> getProducts() {
        return list;
    }

    // SORTING HARGA
    public void sortByPrice() {
        Collections.sort(list, Comparator.comparingDouble(Product::getPrice));
    }

    // SORTING KATEGORI
    public void sortByCategory() {
        Collections.sort(list, Comparator.comparing(Product::getCategory));
    }

    // SEARCHING
    public LinkedList<Product> searchByName(String keyword) {
        LinkedList<Product> result = new LinkedList<>();

        for (Product p : list) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(p);
            }
        }

        return result;
    }
}

// GUI CLASS
public class Main extends JFrame {

    private ProductStack productStack = new ProductStack();

    private JTextField txtName;
    private JTextField txtCategory;
    private JTextField txtPrice;
    private JTextField txtSearch;

    private JTextArea area;

    public Main() {

        setTitle("Aplikasi Manajemen Produk");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // PANEL INPUT
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Nama Produk"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Kategori"));
        txtCategory = new JTextField();
        panel.add(txtCategory);

        panel.add(new JLabel("Harga"));
        txtPrice = new JTextField();
        panel.add(txtPrice);

        panel.add(new JLabel("Cari Produk"));
        txtSearch = new JTextField();
        panel.add(txtSearch);

        // BUTTON
        JButton btnAdd = new JButton("Tambah");
        JButton btnDelete = new JButton("Hapus");
        JButton btnSortPrice = new JButton("Sort Harga");
        JButton btnSortCategory = new JButton("Sort Kategori");
        JButton btnSearch = new JButton("Cari");

        panel.add(btnAdd);
        panel.add(btnDelete);

        // TEXT AREA
        area = new JTextArea();
        JScrollPane scroll = new JScrollPane(area);

        // PANEL BAWAH
        JPanel bottomPanel = new JPanel();

        bottomPanel.add(btnSortPrice);
        bottomPanel.add(btnSortCategory);
        bottomPanel.add(btnSearch);

        add(panel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // EVENT TAMBAH
        btnAdd.addActionListener(e -> addProduct());

        // EVENT HAPUS
        btnDelete.addActionListener(e -> deleteProduct());

        // EVENT SORT HARGA
        btnSortPrice.addActionListener(e -> {
            productStack.sortByPrice();
            displayProducts(productStack.getProducts());
        });

        // EVENT SORT KATEGORI
        btnSortCategory.addActionListener(e -> {
            productStack.sortByCategory();
            displayProducts(productStack.getProducts());
        });

        // EVENT SEARCH
        btnSearch.addActionListener(e -> {
            String key = txtSearch.getText();

            LinkedList<Product> result =
                    productStack.searchByName(key);

            displayProducts(result);
        });
    }

    // METHOD TAMBAH
    private void addProduct() {

        try {
            String name = txtName.getText();
            String category = txtCategory.getText();
            double price = Double.parseDouble(txtPrice.getText());

            Product p = new Product(name, category, price);

            productStack.push(p);

            displayProducts(productStack.getProducts());

            clearField();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Harga harus angka!");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Terjadi error!");
        }
    }

    // METHOD HAPUS
    private void deleteProduct() {

        try {

            Product p = productStack.pop();

            JOptionPane.showMessageDialog(this,
                    "Produk dihapus: " + p.getName());

            displayProducts(productStack.getProducts());

        } catch (EmptyStackException e) {

            JOptionPane.showMessageDialog(this,
                    "Stok kosong! Tidak bisa menghapus.");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan.");
        }
    }

    // DISPLAY DATA
    private void displayProducts(LinkedList<Product> products) {

        area.setText("");

        for (Product p : products) {
            area.append(p.toString() + "\n");
        }
    }

    // CLEAR INPUT
    private void clearField() {
        txtName.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
    }

    // MAIN
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}