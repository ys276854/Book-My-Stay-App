import java.util.*;

// Represents an Add-On Service
class AddOnService {
    private String serviceId;
    private String serviceName;
    private double cost;

    public AddOnService(String serviceId, String serviceName, double cost) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Manages Add-On Services for Reservations
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add services to a reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total additional cost
    public double calculateTotalServiceCost(String reservationId) {
        double total = 0.0;
        List<AddOnService> services = getServices(reservationId);

        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Add-On Services for Reservation " + reservationId + ":");
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }
    }
}

// Main Class
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        // Sample Reservation ID (assumed from previous use case)
        System.out.print("Enter Reservation ID: ");
        String reservationId = scanner.nextLine();

        // Predefined Services (Extensible)
        AddOnService wifi = new AddOnService("S1", "WiFi", 200);
        AddOnService breakfast = new AddOnService("S2", "Breakfast", 500);
        AddOnService airportPickup = new AddOnService("S3", "Airport Pickup", 1000);
        AddOnService extraBed = new AddOnService("S4", "Extra Bed", 800);

        boolean running = true;

        while (running) {
            System.out.println("\nSelect Add-On Services:");
            System.out.println("1. WiFi (₹200)");
            System.out.println("2. Breakfast (₹500)");
            System.out.println("3. Airport Pickup (₹1000)");
            System.out.println("4. Extra Bed (₹800)");
            System.out.println("5. Finish Selection");

            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manager.addService(reservationId, wifi);
                    break;
                case 2:
                    manager.addService(reservationId, breakfast);
                    break;
                case 3:
                    manager.addService(reservationId, airportPickup);
                    break;
                case 4:
                    manager.addService(reservationId, extraBed);
                    break;
                case 5:
                    running = false;
                    continue;
                default:
                    System.out.println("Invalid choice!");
                    continue;
            }

            System.out.println("Service added successfully!");
        }

        // Display Selected Services
        System.out.println("\n===== SUMMARY =====");
        manager.displayServices(reservationId);

        double totalCost = manager.calculateTotalServiceCost(reservationId);
        System.out.println("Total Add-On Cost: ₹" + totalCost);

        scanner.close();
    }
}
