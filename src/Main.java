import java.util.*;

// Booking Request
class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
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

// Shared Inventory (Critical Resource)
class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public Inventory() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 1);
        rooms.put("Suite", 1);
    }

    // Critical Section (Thread-safe)
    public synchronized boolean allocateRoom(String roomType) {
        int available = rooms.getOrDefault(roomType, 0);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName() +
                    " allocated " + roomType + " room");
            rooms.put(roomType, available - 1);
            return true;
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED to allocate " + roomType);
            return false;
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Worker Thread (Simulates Guest Processing)
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private Inventory inventory;

    public BookingProcessor(BookingQueue queue, Inventory inventory, String name) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request;

            // Critical section for queue access
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) break;

            // Allocate room (thread-safe)
            inventory.allocateRoom(request.getRoomType());

            try {
                Thread.sleep(100); // simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        Inventory inventory = new Inventory();

        // Simulate multiple booking requests
        queue.addRequest(new BookingRequest("Alice", "Standard"));
        queue.addRequest(new BookingRequest("Bob", "Standard"));
        queue.addRequest(new BookingRequest("Charlie", "Standard"));
        queue.addRequest(new BookingRequest("David", "Deluxe"));
        queue.addRequest(new BookingRequest("Eve", "Suite"));

        // Multiple threads (guests)
        Thread t1 = new BookingProcessor(queue, inventory, "Thread-1");
        Thread t2 = new BookingProcessor(queue, inventory, "Thread-2");
        Thread t3 = new BookingProcessor(queue, inventory, "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventory.displayInventory();
    }
}