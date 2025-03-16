import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Tenant {


    public void listTenants()
    {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tenants")) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("\nTenants Details:");
            while (rs.next()) {
                System.out.printf("Tenant ID: %d, Name: %s, Phone: %s, Property ID: %d,Payment Status: %s%n",
                        rs.getInt("tenant_id"), rs.getString("name"), rs.getString("phone"), rs.getInt("property_id"),rs.getString("payment_status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void listUnpaidTenants() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tenants WHERE payment_status = 'UNPAID'")) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("\nUnpaid Tenants:");
            while (rs.next()) {
                System.out.printf("Tenant ID: %d, Name: %s, Phone: %s, Property ID: %d%n",
                        rs.getInt("tenant_id"), rs.getString("name"), rs.getString("phone"), rs.getInt("property_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePaymentStatus() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter tenant ID to update payment status: ");
        int tenantId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE tenants SET payment_status = 'PAID' WHERE tenant_id = ?")) {
            stmt.setInt(1, tenantId);
            int rowsUpdated = stmt.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "Payment status updated to PAID." : "Tenant ID not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
