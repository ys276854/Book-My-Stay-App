import java.util.HashMap;
import java.util.Map;

// RoomInventory class (Version 3.0)
class RoomInventory {

    // Centralized inventory using HashMap
    private HashMap<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Register room types with availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type does not exist in inventory.");
        }
    }

    // Method to display full inventory
    public void displayInventory() {
        System.out.println("===== Current Room Inventory =====");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }

        System.out.println("----------------------------------");
    }
}


// Main Application Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize inventory system
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Retrieve availability example
        System.out.println("\nChecking availability for Double Room...");
        int available = inventory.getAvailability("Double Room");
        System.out.println("Available Double Rooms: " + available);

        // Update availability example
        System.out.println("\nUpdating availability for Double Room...");
        inventory.updateAvailability("Double Room", 2);

        // Display updated inventory
        System.out.println("\nInventory After Update:");
        inventory.displayInventory();

        System.out.println("Application Terminated.");
    }
}