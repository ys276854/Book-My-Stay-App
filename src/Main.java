import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Nights: " + nights;
    }
}

// Inventory Manager
class InventoryManager {
    private Map<String, Integer> roomInventory;

    public InventoryManager() {
        roomInventory = new HashMap<>();
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 1);
        roomInventory.put("Suite", 1);
    }

    public void validateAndReserve(String roomType) throws InvalidBookingException {
        // Validate room type
        if (!roomInventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type selected: " + roomType);
        }

        int available = roomInventory.get(roomType);

        // Validate availability
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        // Reserve room (state change only after validation)
        roomInventory.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : roomInventory.keySet()) {
            System.out.println(type + ": " + roomInventory.get(type));
        }
    }
}

// Booking Service
class BookingService {
    private InventoryManager inventoryManager;

    public BookingService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public void createBooking(Reservation reservation) {
        try {
            // Validate nights
            if (reservation.getNights() <= 0) {
                throw new InvalidBookingException("Number of nights must be greater than zero.");
            }

            // Validate and reserve inventory
            inventoryManager.validateAndReserve(reservation.getRoomType());

            // If all validations pass
            System.out.println("\nBooking Confirmed:");
            System.out.println(reservation);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("\nBooking Failed: " + e.getMessage());
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        InventoryManager inventoryManager = new InventoryManager();
        BookingService bookingService = new BookingService(inventoryManager);

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Reservation ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Guest Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Room Type (Standard/Deluxe/Suite): ");
        String roomType = scanner.nextLine();

        System.out.print("Enter Number of Nights: ");
        int nights = scanner.nextInt();

        Reservation reservation = new Reservation(id, name, roomType, nights);

        // Attempt booking
        bookingService.createBooking(reservation);

        // Display inventory after attempt
        inventoryManager.displayInventory();

        scanner.close();
    }
}