import java.sql.*;
import java.util.*;


public class RentalManagementSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Housing Rental Management System");
            System.out.println("1. Add Property");
            System.out.println("2. List Properties");
            System.out.println("3. Add Tenant");
            System.out.println("4. List Tenants");
            System.out.println("5. Update Property Availability");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addProperty(scanner);
                    break;
                case 2:
                    listProperties();
                    break;
                case 3:
                    addTenant(scanner);
                    break;
                case 4:
                    listTenants();
                    break;
                case 5:
                    updatePropertyAvailability(scanner);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);

        scanner.close();
    }

    private static void addProperty(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter property type (1bhk, 2bhk, 3bhk): ");
            String type = scanner.nextLine().toUpperCase();
            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            System.out.print("Enter rent: ");
            double rent = scanner.nextDouble();
            if (!isRentValid(rent)) {
                System.out.println("Invalid Input");
                return;
            }

            scanner.nextLine(); // consume newline

            String sql = "INSERT INTO properties (address, rent, available, type) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, address);
                stmt.setDouble(2, rent);
                stmt.setBoolean(3, true); // available by default
                stmt.setString(4, type);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Property added successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listProperties() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM properties";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    System.out.printf("ID: %d, Address: %s, Rent: %.2f, Available: %b, Type: %s%n",
                            rs.getInt("property_id"),
                            rs.getString("address"),
                            rs.getDouble("rent"),
                            rs.getBoolean("available"),
                            rs.getString("type"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addTenant(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter name (letters only): ");
            String name = scanner.nextLine();

            if (!isValidName(name)) {
                System.out.println("Invalid name");
                return;
            }

            System.out.print("Enter phone number (e.g., 123-456-7890): ");
            String phone = scanner.nextLine();

            if (!isValidPhoneNumber(phone)) {
                System.out.println("Invalid phone number format. Please enter a valid phone number (e.g., 123-456-7890).");
                return;
            }

            System.out.print("Enter property ID: ");
            int propertyId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (!isPropertyAvailable(propertyId)) {
                System.out.println("Property is already booked. Check any other property");
                return;
            }
            String sql = "INSERT INTO tenants (name, phone, property_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setInt(3, propertyId);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Tenant added successfully.");
                    updatePropertyAvailability(propertyId, false); // Set property to unavailable
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listTenants() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT t.tenant_id, t.name, t.phone, t.property_id, p.type " +
                    "FROM tenants t " +
                    "INNER JOIN properties p ON t.property_id = p.property_id";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    System.out.printf("ID: %d, Name: %s, Phone: %s, Property ID: %d, Type: %s%n",
                            rs.getInt("tenant_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getInt("property_id"),
                            rs.getString("type"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updatePropertyAvailability(Scanner scanner) {
        System.out.print("Enter property ID: ");
        int propertyId = scanner.nextInt();
        System.out.print("Enter availability (true/false): ");
        boolean available = scanner.nextBoolean();
        updatePropertyAvailability(propertyId, available);
    }

    private static void updatePropertyAvailability(int propertyId, boolean available) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE properties SET available = ? WHERE property_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setBoolean(1, available);
                stmt.setInt(2, propertyId);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Property availability updated successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean isPropertyAvailable(int propertyId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT available FROM properties WHERE property_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, propertyId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getBoolean("available");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private static boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{3}-\\d{3}-\\d{4}");
    }

    private static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        name = name.trim();
        return name.matches("[a-zA-Z]+([\\s][a-zA-Z]+)*");
    }

    private static boolean isRentValid(double rent) {
        return rent >= 0;
    }
}
