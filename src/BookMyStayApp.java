// Abstract class representing a generic Room
abstract class Room {

    // Encapsulated attributes
    private String roomType;
    private int numberOfBeds;
    private int size;
    private double price;

    // Constructor
    public Room(String roomType, int numberOfBeds, int size, double price) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }

    // Getter methods
    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    // Method to display room details
    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + numberOfBeds);
        System.out.println("Size      : " + size + " sq.ft");
        System.out.println("Price     : $" + price);
    }
}


// Single Room Class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 200, 100.0);
    }
}


// Double Room Class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 350, 180.0);
    }
}


// Suite Room Class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 600, 350.0);
    }
}


// Main Application Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Creating Room objects using polymorphism
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static availability variables
        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;

        System.out.println("===== Book My Stay - Room Availability =====\n");

        // Display Single Room
        singleRoom.displayRoomDetails();
        System.out.println("Available : " + singleRoomAvailability);
        System.out.println("--------------------------------------");

        // Display Double Room
        doubleRoom.displayRoomDetails();
        System.out.println("Available : " + doubleRoomAvailability);
        System.out.println("--------------------------------------");

        // Display Suite Room
        suiteRoom.displayRoomDetails();
        System.out.println("Available : " + suiteRoomAvailability);
        System.out.println("--------------------------------------");

        System.out.println("\nApplication Terminated.");
    }
}