import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// System State class (Serializable)
class SystemState implements Serializable {
    private Map<String, Integer> inventory;
    private List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public List<Reservation> getBookingHistory() {
        return bookingHistory;
    }
}

// Persistence Service
class PersistenceService {
    private static final String FILE_NAME = "system_state.ser";

    // Save system state
    public static void saveState(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load system state
    public static SystemState loadState() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading system state: " + e.getMessage());
            return null;
        }
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        // Load previous state if exists
        SystemState state = PersistenceService.loadState();

        Map<String, Integer> inventory;
        List<Reservation> bookingHistory;

        if (state != null) {
            inventory = state.getInventory();
            bookingHistory = state.getBookingHistory();
        } else {
            // Initialize fresh data
            inventory = new HashMap<>();
            inventory.put("Single", 2);
            inventory.put("Double", 2);

            bookingHistory = new ArrayList<>();
        }

        // Simulate a booking
        System.out.println("\n--- Booking Simulation ---");

        String reservationId = "R" + (bookingHistory.size() + 1);
        String guestName = "Guest" + (bookingHistory.size() + 1);
        String roomType = "Single";

        if (inventory.get(roomType) > 0) {
            inventory.put(roomType, inventory.get(roomType) - 1);

            Reservation reservation = new Reservation(reservationId, guestName, roomType);
            bookingHistory.add(reservation);

            System.out.println("Booking successful: " + reservation);
        } else {
            System.out.println("No rooms available for type: " + roomType);
        }

        // Display current state
        System.out.println("\n--- Current Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }

        System.out.println("\n--- Booking History ---");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }

        // Save state before exit
        SystemState newState = new SystemState(inventory, bookingHistory);
        PersistenceService.saveState(newState);

        System.out.println("\nSystem shutting down... Data persisted.");
    }
}