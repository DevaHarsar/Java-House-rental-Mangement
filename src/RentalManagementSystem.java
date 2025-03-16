import java.util.Scanner;

public class RentalManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PropertyManager propertyManager = new PropertyManager();
    private static final TenantManager tenantManager = new TenantManager();
    private static final Tenant Tenant = new Tenant();

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n=== Housing Rental Management System ===");
            System.out.println("1. Add Property");
            System.out.println("2. List Properties");
            System.out.println("3. Add Tenant");
            System.out.println("4. List Tenants");
            System.out.println("5. Update Property Availability");
            System.out.println("6. Mark Payment as Paid");
            System.out.println("7. List Unpaid Tenants");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> propertyManager.addProperty();
                case 2 -> propertyManager.listProperties();
                case 3 -> tenantManager.addTenant();
                case 4 -> Tenant.listTenants();
                case 5 -> propertyManager.updatePropertyAvailability();
                case 6 -> Tenant.updatePaymentStatus();
                case 7 -> Tenant.listUnpaidTenants();
                case 8 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);
    }
}
