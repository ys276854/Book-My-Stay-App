import java.util.*;

// Reservation Class
class Reservation {
    private String reservationId;
    private String roomType;
    private String roomId;
    private boolean isActive;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void cancel() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId +
                ", Status: " + (isActive ? "ACTIVE" : "CANCELLED");
    }
}

// Inventory Manager
class InventoryManager {
    private Map<String, Integer> inventory;

    public InventoryManager() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Cancellation Service (Core Logic)
class CancellationService {

    private Map<String, Reservation> reservations;
    private Stack<String> rollbackStack;
    private InventoryManager inventoryManager;

    public CancellationService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        this.reservations = new HashMap<>();
        this.rollbackStack = new Stack<>();
    }

    // Add reservation (simulate confirmed booking)
    public void addReservation(Reservation reservation) {
        reservations.put(reservation.getReservationId(), reservation);
        inventoryManager.decrement(reservation.getRoomType());
    }

    // Cancel reservation
    public void cancelReservation(String reservationId) {

        // Validate existence
        if (!reservations.containsKey(reservationId)) {
            System.out.println("\n❌ Cancellation Failed: Reservation does not exist.");
            return;
        }

        Reservation res = reservations.get(reservationId);

        // Prevent duplicate cancellation
        if (!res.isActive()) {
            System.out.println("\n❌ Cancellation Failed: Already cancelled.");
            return;
        }

        // Step 1: Push roomId to rollback stack
        rollbackStack.push(res.getRoomId());

        // Step 2: Restore inventory
        inventoryManager.increment(res.getRoomType());

        // Step 3: Update booking state
        res.cancel();

        System.out.println("\n✅ Cancellation Successful for Reservation ID: " + reservationId);
    }

    public void displayReservations() {
        System.out.println("\n--- Reservations ---");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

// Main Class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        InventoryManager inventoryManager = new InventoryManager();
        CancellationService service = new CancellationService(inventoryManager);

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("R101", "Standard", "S1");
        Reservation r2 = new Reservation("R102", "Deluxe", "D1");

        service.addReservation(r1);
        service.addReservation(r2);

        service.displayReservations();
        inventoryManager.displayInventory();

        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter Reservation ID to cancel: ");
        String id = scanner.nextLine();

        service.cancelReservation(id);

        service.displayReservations();
        inventoryManager.displayInventory();
        service.displayRollbackStack();

        scanner.close();
    }
}