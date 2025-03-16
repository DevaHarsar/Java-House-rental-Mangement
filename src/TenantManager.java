import java.sql.*;
import java.util.Scanner;

public class TenantManager {
    public void addTenant() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter phone number: ");
            String phone = scanner.nextLine();
            System.out.print("Enter property ID: ");
            int propertyId = scanner.nextInt();
            scanner.nextLine();

            Boolean available =  Property.isPropertyAvailable(propertyId);

            if (available == null) {
                System.out.println("Error: Property ID not found.");
                return;
            } else if (!available) {
                System.out.println("Property is already booked.");
                return;
            }

            String sql = "INSERT INTO tenants (name, phone, property_id, payment_status) VALUES (?, ?, ?, 'UNPAID')";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setInt(3, propertyId);
                stmt.executeUpdate();
                System.out.println("Tenant added successfully. Payment status: UNPAID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
