import java.sql.*;
public class Property {
    private int id;
    private String address;
    private double rent;
    private boolean available;
    private String type;

    public Property(int id, String address, double rent, boolean available, String type) {
        this.id = id;
        this.address = address;
        this.rent = rent;
        this.available = available;
        this.type = type;
    }
   public static Boolean isPropertyAvailable(int propertyId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT available FROM properties WHERE property_id = ?")) {
            stmt.setInt(1, propertyId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("available");
            } else {
                return null; // Property does not exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void displayPropertyDetails() {
        System.out.printf("ID: %d, Type: %s, Address: %s, Rent: %.2f, Available: %b%n",
                id, type, address, rent, available);
    }
}
