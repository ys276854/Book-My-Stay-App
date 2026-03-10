import java.util.*;

// Reservation class
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}


// Inventory Service
class InventoryService {

    private HashMap<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decreaseAvailability(String roomType) {
        int count = inventory.get(roomType);
        inventory.put(roomType, count - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + " : " + inventory.get(key));
        }
    }
}


// Booking Service (handles allocation)
class BookingService {

    private Queue<Reservation> requestQueue;
    private InventoryService inventory;

    // Tracks allocated room IDs
    private Set<String> allocatedRoomIds;

    // Maps room type → assigned room IDs
    private HashMap<String, Set<String>> roomAllocationMap;

    public BookingService(InventoryService inventory) {

        this.inventory = inventory;
        requestQueue = new LinkedList<>();

        allocatedRoomIds = new HashSet<>();
        roomAllocationMap = new HashMap<>();
    }

    // Add booking request
    public void submitRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Request received from " + reservation.getGuestName());
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String prefix = roomType.substring(0, 2).toUpperCase();
        String roomId;

        do {
            roomId = prefix + (100 + allocatedRoomIds.size());
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    // Process requests in FIFO order
    public void processBookings() {

        System.out.println("\nProcessing Booking Requests...\n");

        while (!requestQueue.isEmpty()) {

            Reservation reservation = requestQueue.poll();
            String roomType = reservation.getRoomType();

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = generateRoomId(roomType);

                // Add to global set
                allocatedRoomIds.add(roomId);

                // Add to room-type map
                roomAllocationMap
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                // Decrease inventory
                inventory.decreaseAvailability(roomType);

                System.out.println("Reservation Confirmed!");
                System.out.println("Guest: " + reservation.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println("--------------------------");

            } else {

                System.out.println("Reservation Failed for "
                        + reservation.getGuestName()
                        + " (No rooms available for "
                        + roomType + ")");
                System.out.println("--------------------------");
            }
        }
    }

    // Display allocated rooms
    public void displayAllocations() {

        System.out.println("\nRoom Allocation Summary:");

        for (String type : roomAllocationMap.keySet()) {
            System.out.println(type + " → " + roomAllocationMap.get(type));
        }
    }
}


// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();

        BookingService bookingService = new BookingService(inventory);

        // Submit booking requests
        bookingService.submitRequest(new Reservation("Ajay", "Single Room"));
        bookingService.submitRequest(new Reservation("Rahul", "Single Room"));
        bookingService.submitRequest(new Reservation("Sneha", "Double Room"));
        bookingService.submitRequest(new Reservation("Kiran", "Suite Room"));
        bookingService.submitRequest(new Reservation("Priya", "Suite Room"));

        // Process queue
        bookingService.processBookings();

        // Show final results
        bookingService.displayAllocations();

        inventory.displayInventory();
    }
}