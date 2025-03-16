import java.sql.*;
import java.util.Scanner;
public class PropertyManager {
    public void addProperty() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter property type (1bhk, 2bhk, 3bhk): ");
            String type = scanner.nextLine().toUpperCase();
            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            System.out.print("Enter rent: ");
            double rent = scanner.nextDouble();
            scanner.nextLine();

            String sql = "INSERT INTO properties (address, rent, available, type) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, address);
                stmt.setDouble(2, rent);
                stmt.setBoolean(3, true);
                stmt.setString(4, type);
                stmt.executeUpdate();
                System.out.println("Property added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listProperties() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM properties")) {
            while (rs.next()) {
                Property property = new Property(
                        rs.getInt("property_id"),
                        rs.getString("address"),
                        rs.getDouble("rent"),
                        rs.getBoolean("available"),
                        rs.getString("type")
                );
                property.displayPropertyDetails();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePropertyAvailability() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter property ID: ");
        int propertyId = scanner.nextInt();
        System.out.print("Enter availability (true/false): ");
        boolean available = scanner.nextBoolean();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE properties SET available = ? WHERE property_id = ?")) {
            stmt.setBoolean(1, available);
            stmt.setInt(2, propertyId);
            stmt.executeUpdate();
            System.out.println("Property availability updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
