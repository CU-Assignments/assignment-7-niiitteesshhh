
import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {

    static final String URL = "jdbc:mysql://localhost:3306/your_database_name";
    static final String USER = "root";
    static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Scanner scanner = new Scanner(System.in)
        ) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn.setAutoCommit(false); // Enable transaction handling

            int choice;

            do {
                System.out.println("\n=== Product CRUD Menu ===");
                System.out.println("1. Create Product");
                System.out.println("2. Read Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        createProduct(conn, scanner);
                        break;
                    case 2:
                        readProducts(conn);
                        break;
                    case 3:
                        updateProduct(conn, scanner);
                        break;
                    case 4:
                        deleteProduct(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }

            } while (choice != 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CREATE
    private static void createProduct(Connection conn, Scanner scanner) {
        String sql = "INSERT INTO Product (ProductID, ProductName, Price, Quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.print("Enter Product ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter Product Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();

            System.out.print("Enter Quantity: ");
            int qty = scanner.nextInt();

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setDouble(3, price);
            ps.setInt(4, qty);

            ps.executeUpdate();
            conn.commit();
            System.out.println("Product added successfully.");

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    // READ
    private static void readProducts(Connection conn) {
        String sql = "SELECT * FROM Product";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nProductID | ProductName | Price | Quantity");
            System.out.println("--------------------------------------------");

            while (rs.next()) {
                System.out.printf("%d\t%s\t%.2f\t%d\n",
                    rs.getInt("ProductID"),
                    rs.getString("ProductName"),
                    rs.getDouble("Price"),
                    rs.getInt("Quantity"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
    }

    // UPDATE
    private static void updateProduct(Connection conn, Scanner scanner) {
        String sql = "UPDATE Product SET ProductName=?, Price=?, Quantity=? WHERE ProductID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.print("Enter Product ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter New Product Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter New Price: ");
            double price = scanner.nextDouble();

            System.out.print("Enter New Quantity: ");
            int qty = scanner.nextInt();

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, qty);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            conn.commit();

            if (rows > 0)
                System.out.println("Product updated successfully.");
            else
                System.out.println("Product ID not found.");

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    // DELETE
    private static void deleteProduct(Connection conn, Scanner scanner) {
        String sql = "DELETE FROM Product WHERE ProductID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.print("Enter Product ID to delete: ");
            int id = scanner.nextInt();

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            conn.commit();

            if (rows > 0)
                System.out.println("Product deleted successfully.");
            else
                System.out.println("Product ID not found.");

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }
}
